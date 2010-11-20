/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * @author Javier Snaider
 *
 */
public class LearnableImpl extends ActivatibleImpl implements Learnable {
	private double baseLevelActivation;
	private ExciteStrategy baseLevelExciteStrategy;
	private DecayStrategy baseLevelDecayStrategy;
	private static Logger logger = Logger.getLogger(LearnableImpl.class.getCanonicalName());

	public LearnableImpl(double activation, ExciteStrategy exciteStrategy, 
									DecayStrategy decayStrategy) {
		super(activation, exciteStrategy, decayStrategy);
		this.baseLevelExciteStrategy = exciteStrategy;
		this.baseLevelDecayStrategy = decayStrategy;
	}

	public LearnableImpl() {
		super();
		baseLevelActivation = 0.0;
		baseLevelDecayStrategy = new LinearDecayStrategy();
		baseLevelExciteStrategy = new DefaultExciteStrategy();
	}

	@Override
	public void decayBaseLevelActivation(long ticks) {
		if (baseLevelDecayStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getActualTick());
			synchronized(this){
				baseLevelActivation = baseLevelDecayStrategy.decay(baseLevelActivation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getActualTick());
		}		
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return baseLevelExciteStrategy;
	}

	@Override
	public void setBaseLevelExciteStrategy(ExciteStrategy baseLevelExciteStrategy) {
		this.baseLevelExciteStrategy = baseLevelExciteStrategy;
	}

	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return baseLevelDecayStrategy;
	}

	@Override
	public void setBaseLevelDecayStrategy(DecayStrategy baseLevelDecayStrategy) {
		this.baseLevelDecayStrategy = baseLevelDecayStrategy;
	}

	@Override
	public double getBaseLevelActivation() {
		return baseLevelActivation;
	}
	@Override
	public void reinforceBaseLevelActivation(double amount) {
		if (baseLevelExciteStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before reinforce has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getActualTick());
			synchronized(this){
				baseLevelActivation = baseLevelExciteStrategy.excite(baseLevelActivation, amount);
			}
			logger.log(Level.FINEST,this.toString() + " after reinforce has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getActualTick());
		}		
	}

	@Override
	public void setBaseLevelActivation(double activation) {
			this.baseLevelActivation=activation;		
	}

	@Override
	public double getTotalActivation() { //TODO: Normalization is needed!!!!!
	    return getActivation() + baseLevelActivation;
	}


}
