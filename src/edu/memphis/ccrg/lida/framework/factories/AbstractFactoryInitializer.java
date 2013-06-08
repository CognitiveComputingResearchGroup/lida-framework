/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import edu.memphis.ccrg.lida.framework.initialization.FactoryDef;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.initialization.FactoryDef.XmlConfig;
import edu.memphis.ccrg.lida.framework.initialization.XmlUtils;

/**
 * 
 * @author Sean Kugele
 * 
 * @param <T>
 *            type of the factory to be initialized
 * 
 */
public abstract class AbstractFactoryInitializer<T extends InitializableFactory> implements
        FactoryInitializer<T> {

    private static final Logger logger = Logger.getLogger(FactoryInitializer.class
            .getCanonicalName());

    /*
     * Singleton instance of the FactoryManager -- used to retrieve factories
     * upon which this factory is depends
     */
    private FactoryManager factoryManager = FactoryManager.getInstance();

    private GlobalInitializer globalInitializer = GlobalInitializer.getInstance();

    /**
     * The {@link Factory} to be initialized
     */
    protected T factory;

    /**
     * The {@link FactoryDef} that will be used to perform the initialization
     */
    protected FactoryDef factoryDef;

    /**
     * Constructor.
     * 
     * @param factory
     *            the {@link Factory} to be initialized
     * @param factoryDef
     *            the {@link FactoryDef} that will be used referenced for
     *            initialization details
     */
    protected AbstractFactoryInitializer(T factory, FactoryDef factoryDef) {
        this.factory = factory;
        this.factoryDef = factoryDef;
    }

    @Override
    public void init() {
        checkPreconditions();
        loadData();
    }

    /**
     * An abstract method that derived classes must implement. This method
     * should load the factory data details into the
     * {@link InitializableFactory} that this {@link FactoryInitializer} is
     * designed to initialize.
     */
    protected abstract void loadData();

    /**
     * Returns a reference to the {@link Factory} that will be initialized by
     * this initializer.
     * 
     * @return the {@link Factory}
     */
    protected T getFactory() {
        return factory;
    }

    /**
     * Returns the {@link FactoryDef} that will be used to initialize the
     * {@link Factory} serviced by this initializer.
     * 
     * @return the {@link FactoryDef}
     */
    protected FactoryDef getFactoryDef() {
        return factoryDef;
    }

    private void checkPreconditions() {

        // Verify that the supplied FactoryDef matches the factory's class
        if (hasValidFactoryDef()) {
            logger.log(Level.INFO, "Factory definition verified for {0} initializer",
                    new Object[] { factory.getName() });
        } else {
            logger.log(Level.WARNING,
                    "Invalid factory definition encountered for {0} initializer",
                    new Object[] { factory.getName() });
        }

        // Verify that factory dependencies have been initialized
        if (hasInitializedDependencies()) {
            logger.log(Level.INFO,
                    "Factory dependencies have been initialized for {0} initializer",
                    new Object[] { factory.getName() });
        } else {
            logger.log(
                    Level.WARNING,
                    "Factory dependencies have not been initialized in FactoryManager for {0} initializer",
                    new Object[] { factory.getName() });
        }
    }

    private boolean hasValidFactoryDef() {
        if (factoryDef == null) {
            return false;
        }

        if (factoryDef.getClassname() == null || factoryDef.getConfig() == null
                || factoryDef.getName() == null || factoryDef.getType() == null) {
            return false;
        }

        return true;
    }

    /*
     * Returns true if all factories that this factory is dependent on have been
     * initialized in the FactoryManager; otherwise, false is returned and a
     * warning is logged.
     */
    private boolean hasInitializedDependencies() {
        if (factoryDef == null) {
            logger.log(Level.WARNING, "Null factory definition encountered.");
            return false;
        }

        Set<String> dependencies = factoryDef.getDependencies();
        if (dependencies == null || dependencies.isEmpty()) {
            return true;
        }

        for (String factoryName : dependencies) {
            @SuppressWarnings("unchecked")
            Class<? extends Factory> dependency = (Class<? extends Factory>) globalInitializer
                    .getAttribute(factoryName);

            if (dependency == null || factoryManager.getFactory(dependency) == null) {
                logger.log(Level.WARNING,
                        "Missing dependency {0} in factory definition for {1} initializer.",
                        new Object[] { factoryName, factory.getName() });
                return false;
            }
        }

        return true;
    }
}