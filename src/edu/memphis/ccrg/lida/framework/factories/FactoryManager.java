/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.initialization.FactoryDef;
import edu.memphis.ccrg.lida.framework.initialization.FactoryDef.XmlConfig;
import edu.memphis.ccrg.lida.framework.initialization.XmlUtils;

/**
 * The FactoryManager provides a registry for storing and retrieving factory
 * objects.
 * 
 * @author Sean Kugele
 * @author Javier Snaider
 */
public class FactoryManager {

    private static final Logger logger = Logger.getLogger(FactoryManager.class
            .getCanonicalName());

    /*
     * Sole instance of this class that will be used.
     */
    private static FactoryManager instance = new FactoryManager();

    /*
     * Map of Factory interfaces to registered implementations of those
     * interfaces
     */
    private Map<Class<? extends Factory>, Object> factories = new HashMap<Class<? extends Factory>, Object>();

    /*
     * Private constructor to prevent instantiation
     */
    private FactoryManager() {
    }

    /**
     * Returns the sole instance of this class. Implements the Singleton
     * pattern.
     * 
     * @return sole instance of this class
     */
    public static FactoryManager getInstance() {
        return instance;
    }

    /**
     * Returns the implementation for the specified {@link Factory} interface,
     * or {@code null} if no such implementation was found.
     * 
     * @param type
     *            a {@link Factory} interface for which an implementation will
     *            be returned
     * @return an implementation for the specified interface
     */
    public <T extends Factory> T getFactory(Class<T> type) {
        @SuppressWarnings("unchecked")
        T factory = (T) factories.get(type);

        if (factory == null) {
            logger.log(Level.WARNING, "Unable to find factory implementation for type {0}.",
                    new Object[] { type });
        }

        return factory;
    }

    /**
     * Initializes the FactoryManager and its factories based on the contents of
     * the supplied properties file.
     * 
     * @param p
     *            a properties file containing factory config information
     */
    public void init(Properties p) {
        FactoryManagerInitializer initializer = new FactoryManagerInitializer(p);
        initializer.init();
    }

    // An inner class to initialize the FactoryManager and its factories
    private class FactoryManagerInitializer {
        private static final String FACTORY_CONFIG_PROPERTY_NAME = "lida.factory.config";
        private static final String DEFAULT_FACTORY_CONFIG_XML_FILE_PATH = "configs/lidaFactoryConfig.xml";
        private static final String DEFAULT_FACTORY_CONFIG_SCHEMA_FILE_PATH = "edu/memphis/ccrg/lida/framework/initialization/config/LidaFactories.xsd";

        private final String factoryConfigFilename;

        private final FactoryManager manager = FactoryManager.this;

        public FactoryManagerInitializer(Properties p) {
            factoryConfigFilename = p.getProperty(FACTORY_CONFIG_PROPERTY_NAME,
                    DEFAULT_FACTORY_CONFIG_XML_FILE_PATH);
        }

        public void init() {
            org.w3c.dom.Document d = XmlUtils.parseXmlFile(factoryConfigFilename,
                    DEFAULT_FACTORY_CONFIG_SCHEMA_FILE_PATH);

            if (d == null) {
                logger.log(Level.SEVERE,
                        "Failed to parse factories XML document. Factory data will not be loaded.");
                return;
            }

            loadFactories(d);
        }

        private void loadFactories(org.w3c.dom.Document d) {
            org.w3c.dom.Element root = d.getDocumentElement();
            Map<String, FactoryDef> factoryMap = getFactories(root);

            for (FactoryDef def : factoryMap.values()) {
                initFactory(def, factoryMap);
            }
        }

        private void initFactory(FactoryDef def, Map<String, FactoryDef> factoryMap) {
            if (isFactoryInitialized(def)) {
                return;
            }

            Set<String> dependencies = def.getDependencies();
            if (dependencies != null) {
                initDependencies(def, dependencies, factoryMap);
            }

            InitializableFactory factoryObject = createFactory(def);
            Class<? extends Factory> factoryInterface = getFactoryInterface(def);

            if (factoryObject == null || factoryInterface == null) {
                logger.log(Level.WARNING, "Failed to initialize factory with name {0}.",
                        new Object[] { def.getName() });
                return;
            }

            // Initialize factory with factory data
            factoryObject.init(def);

            // Add factory to FactoryManager registry
            manager.factories.put(factoryInterface, factoryObject);
        }

        private void initDependencies(FactoryDef def, Set<String> dependencies,
                Map<String, FactoryDef> factoryMap) {
            if (hasCircularDependency(def, factoryMap, null)) {
                logger.log(
                        Level.WARNING,
                        "Circular dependency discovered for factory {0}.  Factory will not be initialized.",
                        new Object[] { def.getName() });
                return;
            }

            for (String factoryDependency : dependencies) {
                FactoryDef dependDef = factoryMap.get(factoryDependency);
                if (dependDef == null) {
                    logger.log(
                            Level.WARNING,
                            "Unable to resolve factory dependency definition {0} for factory {1}.",
                            new Object[] { factoryDependency, def.getName() });
                    continue;
                } else {
                    initFactory(dependDef, factoryMap);
                }
            }
        }

        private boolean hasCircularDependency(FactoryDef def,
                Map<String, FactoryDef> factoryMap, Stack<String> predecessors) {
            if (def == null) {
                return false;
            }

            if (predecessors == null) {
                predecessors = new Stack<String>();
            } else {
                if (predecessors.contains(def.getName())) {
                    return true;
                }
            }

            predecessors.push(def.getName());
            Set<String> dependencies = def.getDependencies();
            if (dependencies == null) {
                return false;
            } else {
                for (String dependency : dependencies) {
                    FactoryDef dependDef = factoryMap.get(dependency);
                    if (hasCircularDependency(dependDef, factoryMap, predecessors)) {
                        return true;
                    }
                }
            }
            predecessors.pop();

            return false;
        }

        @SuppressWarnings("unchecked")
        private Class<? extends Factory> getFactoryInterface(FactoryDef def) {
            String type = def.getType();

            Class<? extends Factory> fInterface = null;
            try {
                fInterface = (Class<? extends Factory>) Class.forName(type);
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "Unable to find factory type {0}.",
                        new Object[] { def.getType() });
            }

            return fInterface;
        }

        private boolean isFactoryInitialized(FactoryDef def) {
            Class<? extends Factory> fInterface = getFactoryInterface(def);
            if (manager.getFactory(fInterface) != null) {
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        private InitializableFactory createFactory(FactoryDef def) {
            InitializableFactory factory = null;

            try {
                Class<? extends InitializableFactory> clazz = (Class<? extends InitializableFactory>) Class
                        .forName(def.getClassname());

                // Call method to create singleton
                Method getInstanceMethod = clazz.getMethod("getInstance", clazz);
                factory = (InitializableFactory) getInstanceMethod.invoke(null);
            } catch (Exception e) {
                logger.log(
                        Level.SEVERE,
                        "Unable to instantiate class {0} of factory type {1}. Factory will not be instantiated.",
                        new Object[] { def.getClassname(), def.getType() });
                return null;
            }

            return factory;
        }

        private Map<String, FactoryDef> getFactories(org.w3c.dom.Element root) {

            Map<String, FactoryDef> factories = new HashMap<String, FactoryDef>();
            List<org.w3c.dom.Element> list = XmlUtils.getChildrenInGroup(root, "factories",
                    "factory");
            if (list != null && list.size() > 0) {
                for (org.w3c.dom.Element e : list) {
                    FactoryDef factory = getFactoryDef(e);
                    factories.put(factory.getName(), factory);
                }
            }

            return factories;
        }

        private FactoryDef getFactoryDef(org.w3c.dom.Element e) {
            FactoryDef factory = new FactoryDef();

            factory.setName(getFactoryName(e));
            factory.setType(getFactoryType(e));
            factory.setClassname(getFactoryClassname(e));
            factory.setConfig(getFactoryConfig(e));
            factory.setDependencies(getFactoryDependencies(e));
            factory.setParams(getFactoryParams(e));

            return factory;
        }

        private String getFactoryName(org.w3c.dom.Element e) {
            String factoryName = e.getAttribute("name");
            return factoryName;
        }

        private String getFactoryType(org.w3c.dom.Element e) {
            return XmlUtils.getTextValue(e, "type");
        }

        private String getFactoryClassname(org.w3c.dom.Element e) {
            return XmlUtils.getTextValue(e, "class");
        }

        private XmlConfig getFactoryConfig(org.w3c.dom.Element e) {
            org.w3c.dom.Element configEle = XmlUtils.getChild(e, "config");
            if (configEle == null) {
                return null;
            }
            XmlConfig newConfig = new XmlConfig();

            newConfig.setFilename(getFactoryConfigFilename(configEle));
            newConfig.setSchema(getFactoryConfigSchema(configEle));

            return newConfig;
        }

        private String getFactoryConfigFilename(org.w3c.dom.Element e) {
            return XmlUtils.getTextValue(e, "filename");
        }

        private String getFactoryConfigSchema(org.w3c.dom.Element e) {
            return XmlUtils.getTextValue(e, "schema");
        }

        private Set<String> getFactoryDependencies(org.w3c.dom.Element e) {
            Set<String> dependencies = new HashSet<String>();
            List<org.w3c.dom.Element> list = XmlUtils.getChildrenInGroup(e, "dependencies",
                    "dependency");
            if (list != null && list.size() > 0) {
                for (org.w3c.dom.Element dependEle : list) {
                    String dependency = dependEle.getNodeValue();
                    if (dependency == null || dependency.isEmpty()) {
                        continue;
                    }
                    dependencies.add(dependency);
                }
            }

            return dependencies;
        }

        private Map<String, Object> getFactoryParams(org.w3c.dom.Element e) {
            return XmlUtils.getTypedParams(e);
        }
    }
}