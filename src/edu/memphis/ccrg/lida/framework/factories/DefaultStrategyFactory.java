/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.initialization.FactoryDef;
import edu.memphis.ccrg.lida.framework.initialization.FactoryDef.XmlConfig;
import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.initialization.XmlUtils;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * @author Sean Kugele
 * 
 */
public class DefaultStrategyFactory implements StrategyFactory {
    private static final Logger logger = Logger.getLogger(DefaultStrategyFactory.class
            .getCanonicalName());

    private static final String FACTORY_NAME = "StrategyFactory";

    /*
     * Sole instance of this class that will be used.
     */
    private static DefaultStrategyFactory instance = new DefaultStrategyFactory();

    /*
     * Map of all the strategies (of any type) available to this factory
     */
    private Map<String, StrategyDef> strategies = new HashMap<String, StrategyDef>();

    /*
     * Private constructor to prevent instantiation
     */
    private DefaultStrategyFactory() {
    }

    /**
     * Returns the sole instance of this factory. Implements the Singleton
     * pattern.
     * 
     * @return the sole {@link DefaultFrameworkTaskFactory} instance of this
     *         class
     */
    public static DefaultStrategyFactory getInstance() {
        return instance;
    }

    @Override
    public Strategy getStrategy(String type, Map<String, ? extends Object> params) {
        return getStrategy(type, null);
    }

    @Override
    public Strategy getStrategy(String type) {
        Strategy d = null;
        StrategyDef sd = strategies.get(type);
        if (sd != null) {
            d = sd.getInstance();
        } else {
            logger.log(Level.WARNING, "Factory does not contain strategy of type {1}",
                    new Object[] { TaskManager.getCurrentTick(), type });
        }
        return d;
    }

    @Override
    public boolean containsStrategy(String type) {
        return strategies.containsKey(type);
    }

    @Override
    public void addStrategyType(StrategyDef strategyDef) {
        strategies.put(strategyDef.getName(), strategyDef);
    }

    @Override
    public String getName() {
        return FACTORY_NAME;
    }

    @Override
    public void init(FactoryDef factoryDef) {
        FactoryInitializer<StrategyFactory> initializer = new Initializer(factoryDef);
        initializer.init();
    }

    /*
     * A private implementation of FactoryInitializer for this factory.
     */
    private class Initializer extends AbstractFactoryInitializer<StrategyFactory> {

        public Initializer(FactoryDef factoryDef) {
            super(DefaultStrategyFactory.this, factoryDef);
        }

        @Override
        public void loadData() {
            XmlConfig config = factoryDef.getConfig();

            Document dom = XmlUtils.parseXmlFile(config.getFilename(), config.getSchema());
            Element docEle = dom.getDocumentElement();

            Map<String, StrategyDef> strategies = getStrategies(docEle);
            fillStrategies(strategies);
        }

        private void fillStrategies(Map<String, StrategyDef> strategies) {
            for (StrategyDef sd : strategies.values()) {
                factory.addStrategyType(sd);
            }
        }

        /**
         * Reads in and creates all {@link StrategyDef}s specified in
         * {@link Element}
         * 
         * @param element
         *            Dom element
         * @return a Map with the {@link StrategyDef} indexed by name
         */
        private Map<String, StrategyDef> getStrategies(Element element) {
            Map<String, StrategyDef> strat = new HashMap<String, StrategyDef>();
            List<Element> list = XmlUtils.getChildrenInGroup(element, "strategies", "strategy");
            if (list != null && list.size() > 0) {
                for (Element e : list) {
                    StrategyDef strategy = getStrategyDef(e);
                    strat.put(strategy.getName(), strategy);
                }
            }
            return strat;
        }

        /**
         * @param e
         *            Dom element
         * @return the {@link Strategy} definition
         */
        private StrategyDef getStrategyDef(Element e) {
            StrategyDef strategy = new StrategyDef();
            String className = XmlUtils.getTextValue(e, "class");
            String name = e.getAttribute("name");
            String type = e.getAttribute("type");
            boolean fweight = Boolean.parseBoolean(e.getAttribute("flyweight"));
            Map<String, Object> params = XmlUtils.getTypedParams(e);

            strategy.setClassName(className.trim());
            strategy.setName(name.trim());
            strategy.setType(type.trim());
            strategy.setFlyWeight(fweight);
            strategy.setParams(params);

            return strategy;
        }
    }

    @Override
    public DecayStrategy getDecayStrategy(String decayType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DecayStrategy getDefaultDecayStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExciteStrategy getDefaultExciteStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDefaultDecayType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDefaultExciteType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExciteStrategy getExciteStrategy(String exciteName) {
        // TODO Auto-generated method stub
        return null;
    }
}