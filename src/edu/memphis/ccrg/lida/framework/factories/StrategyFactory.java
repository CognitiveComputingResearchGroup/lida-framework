/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.factories;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.initialization.FrameworkTaskDef;
import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;

/**
 * @author Sean Kugele
 * 
 */
public interface StrategyFactory extends InitializableFactory {

    /**
     * @param type
     * @return
     */
    public Strategy getStrategy(String type);

    /**
     * @param type
     * @param params
     * @return
     */
    public Strategy getStrategy(String type, Map<String, ? extends Object> params);

    /**
     * Adds the {@link StrategyDef} type.
     * 
     * @param strategyDef
     *            {@link StrategyDef}
     */
    public void addStrategyType(StrategyDef strategyDef);
    
    /**
     * Returns whether this factory contains specified {@link Strategy} type.
     * 
     * @param type
     *            name of strategy type
     * @return true if factory contains type or false if not
     */
    public boolean containsStrategy(String type);

    /**
     * @param decayType
     * @return
     */
    public DecayStrategy getDecayStrategy(String decayType);

    /**
     * @return
     */
    public DecayStrategy getDefaultDecayStrategy();

    /**
     * @return
     */
    public ExciteStrategy getDefaultExciteStrategy();

    /**
     * @return
     */
    public String getDefaultDecayType();

    /**
     * @return
     */
    public String getDefaultExciteType();

    /**
     * @param exciteName
     * @return
     */
    public ExciteStrategy getExciteStrategy(String exciteName);
}
