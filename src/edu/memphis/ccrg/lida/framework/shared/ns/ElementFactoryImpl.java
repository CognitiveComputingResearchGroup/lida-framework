/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared.ns;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.actionselection.BehaviorFactory;
import edu.memphis.ccrg.lida.actionselection.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.factories.ElementFactory;
import edu.memphis.ccrg.lida.framework.factories.FrameworkTaskFactory;
import edu.memphis.ccrg.lida.framework.factories.StrategyFactory;
import edu.memphis.ccrg.lida.framework.initialization.LinkableDef;
import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContent;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * Standard factory for the basic elements of the framework. Support for
 * {@link Node}, {@link Link}, {@link FrameworkTask}, and {@link NodeStructure}
 * 
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public class ElementFactoryImpl implements ElementFactory, FrameworkTaskFactory, StrategyFactory, BehaviorFactory {

    private static final Logger logger = Logger.getLogger(ElementFactoryImpl.class
            .getCanonicalName());


    // TODO a Definition for behavior is needed
    private String defaultBehaviorClassName = BehaviorImpl.class.getCanonicalName();



    /*
     * Sole instance of this class that will be used.
     */
    private static final ElementFactoryImpl instance = new ElementFactoryImpl();

    /**
     * Returns the sole instance of this factory. Implements the Singleton
     * pattern.
     * 
     * @return the sole {@link ElementFactory} instance of this class
     */
    public static ElementFactory getInstance() {
        return instance;
    }

    /*
     * Creates the Factory and adds default Node, Link and Strategies to the
     * Maps in the Factory.
     */
    private ElementFactoryImpl() {

        // Nodes, Links
        Map<String, String> defaultStrategies = new HashMap<String, String>();
        defaultStrategies.put("decay", defaultDecayType);
        defaultStrategies.put("excite", defaultExciteType);

        params = new HashMap<String, Object>();
        params.put("learnable.baseLevelActivation", 0.0);
        params.put("learnable.baseLevelRemovalThreshold", -1.0);
        params.put("learnable.baseLevelDecayStrategy", "noDecay");
        params.put("learnable.baseLevelExciteStrategy", "noExcite");
        params.put("learnable.totalActivationStrategy", strategyName);

        // Nodes
        // Default node type
        addNodeType(defaultNodeType, defaultNodeClassName);

        // PamNodeImpl type
        LinkableDef newNodeDef = new LinkableDef(PamNodeImpl.class.getCanonicalName(),
                new HashMap<String, String>(), PamNodeImpl.class.getSimpleName(), params);
        addNodeType(newNodeDef);

        // Non-decaying PamNode
        newNodeDef = new LinkableDef(PamNodeImpl.class.getCanonicalName(), defaultStrategies,
                "NoDecayPamNode", params);
        addNodeType(newNodeDef);

        // Links
        // Default link type
        addLinkType(defaultLinkType, defaultLinkClassName);

        // PamLinkImpl type
        LinkableDef newLinkDef = new LinkableDef(PamLinkImpl.class.getCanonicalName(),
                new HashMap<String, String>(), PamLinkImpl.class.getSimpleName(), params);
        addLinkType(newLinkDef);

        newLinkDef = new LinkableDef(PamLinkImpl.class.getCanonicalName(), defaultStrategies,
                "NoDecayPamLink", params);
        addLinkType(newLinkDef);
    }


    /**
     * Gets decay strategy.
     * 
     * @param strategyTypeName
     *            name of DecayStrategy type
     * @return the decay strategy
     */
    public DecayStrategy getDecayStrategy(String strategyTypeName) {
        DecayStrategy d = null;
        StrategyDef sd = decayStrategies.get(strategyTypeName);
        if (sd == null) {
            sd = decayStrategies.get(defaultDecayType);
            logger.log(Level.WARNING,
                    "Decay strategy type {1} does not exist. Default type will be returned.",
                    new Object[] { TaskManager.getCurrentTick(), strategyTypeName });
        }
        d = (DecayStrategy) sd.getInstance();
        return d;
    }

    /**
     * Gets excite strategy.
     * 
     * @param strategyTypeName
     *            name of excite strategy type
     * @return the excite strategy
     */
    public ExciteStrategy getExciteStrategy(String strategyTypeName) {
        ExciteStrategy d = null;
        StrategyDef sd = exciteStrategies.get(strategyTypeName);
        if (sd == null) {
            sd = exciteStrategies.get(defaultExciteType);
            logger.log(Level.WARNING,
                    "Excite strategy type {1} does not exist. Default type will be returned.",
                    new Object[] { TaskManager.getCurrentTick(), strategyTypeName });
        }
        d = (ExciteStrategy) sd.getInstance();
        return d;
    }

    /**
     * Get a strategy by type.
     * 
     * @param typeName
     *            Name of sought strategy.
     * @return Strategy if found or null.
     */
    public Strategy getStrategy(String typeName) {
        Strategy d = null;
        StrategyDef sd = strategies.get(typeName);
        if (sd != null) {
            d = sd.getInstance();
        } else {
            logger.log(Level.WARNING, "Factory does not contain strategy of type {1}",
                    new Object[] { TaskManager.getCurrentTick(), typeName });
        }
        return d;
    }



    /*
     * Assigns specified decay and excite strategies to supplied Activatible
     */
    private void setActivatibleStrategies(Activatible activatible, String decayStrategy,
            String exciteStrategy) {
        DecayStrategy decayB = getDecayStrategy(decayStrategy);
        activatible.setDecayStrategy(decayB);
        ExciteStrategy exciteB = getExciteStrategy(exciteStrategy);
        activatible.setExciteStrategy(exciteB);
    }

    /**
     * Set the default Link type used by this factory.
     * 
     * @param linkTypeName
     *            type of links created by this factory
     */
    public void setDefaultLinkType(String linkTypeName) {
        if (linkClasses.containsKey(linkTypeName)) {
            defaultLinkType = linkTypeName;
        } else {
            logger.log(Level.WARNING,
                    "Factory does not contain Link type {1} so it cannot be used as default.",
                    new Object[] { TaskManager.getCurrentTick(), linkTypeName });
        }
    }

    /**
     * Set the default Node type used by this factory.
     * 
     * @param nodeTypeName
     *            type of nodes created by this factory
     */
    public void setDefaultNodeType(String nodeTypeName) {
        if (nodeClasses.containsKey(nodeTypeName)) {
            defaultNodeType = nodeTypeName;
        } else {
            logger.log(Level.WARNING,
                    "Factory does not contain Node type {1} so it cannot be used as default.",
                    new Object[] { TaskManager.getCurrentTick(), nodeTypeName });
        }
    }

    /**
     * Gets default decay type.
     * 
     * @return the defaultDecayType
     */
    public String getDefaultDecayType() {
        return defaultDecayType;
    }

    /**
     * Returns the default {@link DecayStrategy}
     * 
     * @return Factory's default {@link DecayStrategy}
     */
    public DecayStrategy getDefaultDecayStrategy() {
        return getDecayStrategy(defaultDecayType);
    }

    /**
     * Sets default decay type.
     * 
     * @param decayTypeName
     *            DecayType to be used
     */
    public void setDefaultDecayType(String decayTypeName) {
        if (decayStrategies.containsKey(decayTypeName)) {
            defaultDecayType = decayTypeName;
        } else {
            logger.log(
                    Level.WARNING,
                    "Factory does not contain decay strategy type {1} so it cannot be used as default.",
                    new Object[] { TaskManager.getCurrentTick(), decayTypeName });
        }
    }

    /**
     * Gets default excite type.
     * 
     * @return defaultExciteType ExciteType to be used
     */
    public String getDefaultExciteType() {
        return defaultExciteType;
    }

    /**
     * Returns the default {@link ExciteStrategy}
     * 
     * @return Factory's default excite strategy
     */
    public ExciteStrategy getDefaultExciteStrategy() {
        return getExciteStrategy(defaultExciteType);
    }

    /**
     * Sets default excite type.
     * 
     * @param exciteTypeName
     *            the defaultExciteType to set
     */
    public void setDefaultExciteType(String exciteTypeName) {
        if (exciteStrategies.containsKey(exciteTypeName)) {
            defaultExciteType = exciteTypeName;
        } else {
            logger.log(
                    Level.WARNING,
                    "Factory does not contain excite strategy type {1} so it cannot be used as default.",
                    new Object[] { TaskManager.getCurrentTick(), exciteTypeName });
        }
    }

    /**
     * 
     * Returns a new default NodeStructure.
     * 
     * @return a new NodeStructure with default {@link Node} type and default
     *         {@link Link} type.
     */
    public NodeStructure getNodeStructure() {
        return getNodeStructure(defaultNodeType, defaultLinkType);
    }

    /**
     * Returns a new NodeStructure with specified {@link Node} and {@link Link}
     * types.
     * 
     * @param nodeType
     *            type of node in returned {@link NodeStructure}
     * @param linkType
     *            type of Link in returned {@link NodeStructure}
     * @return a new NodeStructure with specified node type and specified link
     *         type or null if types do not exist in this factory.
     */
    public NodeStructure getNodeStructure(String nodeType, String linkType) {
        if (containsNodeType(nodeType)) {
            if (containsLinkType(linkType)) {
                return new NodeStructureImpl(nodeType, linkType);
            }
            logger.log(Level.WARNING,
                    "Cannot get NodeStructure. Factory does not contain link type {1}",
                    new Object[] { TaskManager.getCurrentTick(), linkType });
        }
        logger.log(Level.WARNING,
                "Cannot get NodeStructure. Factory does not contain node type {1}",
                new Object[] { TaskManager.getCurrentTick(), nodeType });
        return null;
    }

    /**
     * Returns a new Behavior based on specified {@link Scheme} of default
     * behavior type.
     * 
     * @param s
     *            a {@link Scheme}
     * @return a new {@link Behavior}
     */
    public Behavior getBehavior(Scheme s) {
        return getBehavior(s, defaultBehaviorClassName);
    }

    /**
     * Returns a new {@link Behavior} of specified class based on specified
     * {@link Scheme}.
     * 
     * @param s
     *            the {@link Scheme} generating the behavior.
     * @param className
     *            qualified name of the desired {@link Behavior} class
     * @return a new {@link Behavior}
     */
    @SuppressWarnings("static-method")
    public Behavior getBehavior(Scheme s, String className) {
        if (s == null) {
            logger.log(Level.WARNING, "Cannot create a Behavior with null Scheme.",
                    TaskManager.getCurrentTick());
            return null;
        }
        if (className == null) {
            logger.log(Level.WARNING,
                    "Cannot create a Behavior, specified class name is null.",
                    TaskManager.getCurrentTick());
            return null;
        }
        Behavior b = null;
        try {
            b = (Behavior) Class.forName(className).newInstance();
            b.setId(behaviorIdCount++);
            b.setScheme(s);
            b.setActivation(s.getTotalActivation());
        } catch (InstantiationException e) {
            logger.log(Level.WARNING,
                    "InstantiationException encountered creating object of class {1}.",
                    new Object[] { TaskManager.getCurrentTick(), className });
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING,
                    "IllegalAccessException encountered creating object of class {1}.",
                    new Object[] { TaskManager.getCurrentTick(), className });
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING,
                    "ClassNotFoundException encountered creating object of class {1}.",
                    new Object[] { TaskManager.getCurrentTick(), className });
        }
        return b;
    }

    @Override
    public CognitiveContent getCognitiveContent(String type,
            Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContentStructure getCognitiveContentStructure(String type,
            Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContent getCognitiveContent(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContentStructure getCognitiveContentStructure(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Behavior getBehavior(Scheme s, String type, Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Strategy getStrategy(String type, Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsStrategy(String type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FrameworkTask getFrameworkTask(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FrameworkTask getFrameworkTask(String type, Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FrameworkTask getFrameworkTask(String taskType,
            Map<String, ? extends Object> params, Map<ModuleName, FrameworkModule> modules) {
        // TODO Auto-generated method stub
        return null;
    }

}
