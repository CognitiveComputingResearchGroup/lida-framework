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
package edu.memphis.ccrg.lida.framework.shared.activation;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultTotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * 
 * Default implementation of Learnable.
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public class LearnableImpl extends ActivatibleImpl implements Learnable {

	private static final Logger logger = Logger.getLogger(LearnableImpl.class.getCanonicalName());
	private static LidaElementFactory factory=LidaElementFactory.getInstance();
	
	private double baseLevelActivation;
	private ExciteStrategy baseLevelExciteStrategy;
	private DecayStrategy baseLevelDecayStrategy;
	private TotalActivationStrategy totalActivationStrategy;

	private static final double DEFAULT_REMOVABLE_THRESHOLD = -1.0;
	private double learnableRemovableThreshold = DEFAULT_REMOVABLE_THRESHOLD;

	public LearnableImpl(double activation, ExciteStrategy exciteStrategy, 
									DecayStrategy decayStrategy, TotalActivationStrategy taStrategy, double learnebleRemovableThreshold) {
		super(activation, exciteStrategy, decayStrategy, 0.0);
		this.baseLevelExciteStrategy = exciteStrategy;
		this.baseLevelDecayStrategy = decayStrategy;
		this.totalActivationStrategy = taStrategy;
		this.learnableRemovableThreshold=learnebleRemovableThreshold;
	}

	public LearnableImpl() {
		super();
		baseLevelActivation = 0.0;
		baseLevelDecayStrategy = factory.getDecayStrategy(factory.getDefaultDecayType());
		baseLevelExciteStrategy = factory.getExciteStrategy(factory.getDefaultExciteType());
//FIXME move to the Factory!!!!
		totalActivationStrategy = new DefaultTotalActivationStrategy();
	}
	
	public LearnableImpl(Learnable l) {
		this(l.getActivation(), l.getExciteStrategy(), l.getDecayStrategy(), l.getTotalActivationStrategy(), l.getLearnableRemovalThreshold());
	}

	@Override
	public void decay(long ticks){
		decayBaseLevelActivation(ticks);
		super.decay(ticks);
	}

	@Override
	public void decayBaseLevelActivation(long ticks) {
		if (baseLevelDecayStrategy != null) {
			logger.log(Level.FINEST, toString() + " before decay has a BaseLevelAct: " +
						baseLevelActivation,LidaTaskManager.getCurrentTick());
			synchronized(this){
				baseLevelActivation = baseLevelDecayStrategy.decay(baseLevelActivation,ticks);
			}
			logger.log(Level.FINEST, toString() + " after decay has a BaseLevelAct: " + 
					baseLevelActivation,LidaTaskManager.getCurrentTick());
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
	public void reinforceBaseLevelActivation(double amount) {
		if (baseLevelExciteStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before reinforce has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getCurrentTick());
			synchronized(this){
				baseLevelActivation = baseLevelExciteStrategy.excite(baseLevelActivation, amount);
			}
			logger.log(Level.FINEST,this.toString() + " after reinforce has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getCurrentTick());
		}		
	}

	@Override
	public void setBaseLevelActivation(double activation) {
		this.baseLevelActivation=activation;		
	}
	
	@Override
	public double getBaseLevelActivation() {
		return baseLevelActivation;
	}

	@Override
	public double getLearnableRemovalThreshold() {
		return learnableRemovableThreshold;
	}

	@Override
	public void setLearnableRemovalThreshold(double threshold) {
		this.learnableRemovableThreshold = threshold;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#isRemovable()
	 */
	@Override
	public boolean isRemovable() {
		return baseLevelActivation <= learnableRemovableThreshold;
	}
	
	@Override
	public TotalActivationStrategy getTotalActivationStrategy() {
		return totalActivationStrategy;
	}

	@Override
	public void setTotalActivationStrategy(
			TotalActivationStrategy totalActivationStrategy) {
		this.totalActivationStrategy = totalActivationStrategy;
	}
	

	@Override
	public double getTotalActivation() { 
	    return totalActivationStrategy.calculateTotalActivation(baseLevelActivation, getActivation());
	}
}
