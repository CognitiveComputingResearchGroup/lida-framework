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
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultTotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * 
 * Default implementation of Learnable.
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public class LearnableImpl extends ActivatibleImpl implements Learnable {

	private static final Logger logger = Logger.getLogger(LearnableImpl.class.getCanonicalName());
	private static final LidaElementFactory factory = LidaElementFactory.getInstance();
	
	private double baseLevelActivation;
	private ExciteStrategy baseLevelExciteStrategy;
	private DecayStrategy baseLevelDecayStrategy;
	private TotalActivationStrategy totalActivationStrategy;

	private double learnableRemovableThreshold = DEFAULT_REMOVAL_THRESHOLD;

	/**
	 * @param activation current activation
	 * @param activatibleRemovalThreshold activation threshold to remain in a {@link NodeStructure}
	 * @param baseLevelActivation base-level activation for learning
	 * @param learnableRemovableThreshold base-level activation threshold to remain in a {@link NodeStructure} of {@link PamNode} and {@link PamLink}
	 * @param exciteStrategy {@link ExciteStrategy} for exciting {@link ActivatibleImpl} activation.
	 * @param decayStrategy {@link DecayStrategy} for decaying {@link ActivatibleImpl} activation.
	 * @param baseLevelExciteStrategy {@link ExciteStrategy} for reinforcing {@link LearnableImpl} base-level activation.
	 * @param baseLevelDecayStrategy {@link DecayStrategy} for decaying {@link LearnableImpl} base-level activation.
	 * @param taStrategy {@link TotalActivationStrategy} how this LearnableImpl will calculate its total activation.
	 */
	public LearnableImpl(double activation, double activatibleRemovalThreshold, double baseLevelActivation, double learnableRemovableThreshold,
			ExciteStrategy exciteStrategy, DecayStrategy decayStrategy, ExciteStrategy baseLevelExciteStrategy, DecayStrategy baseLevelDecayStrategy, TotalActivationStrategy taStrategy) {
		super(activation, activatibleRemovalThreshold, exciteStrategy, decayStrategy);
		
		this.baseLevelActivation = baseLevelActivation;
		this.learnableRemovableThreshold=learnableRemovableThreshold;
		this.baseLevelExciteStrategy = baseLevelExciteStrategy;
		this.baseLevelDecayStrategy = baseLevelDecayStrategy;
		this.totalActivationStrategy = taStrategy;
	}

	/**
	 * 
	 */
	public LearnableImpl() {
		super();
		baseLevelActivation = 0.0;
		this.learnableRemovableThreshold=0.0;
		baseLevelDecayStrategy = factory.getDefaultDecayStrategy();
		baseLevelExciteStrategy = factory.getDefaultExciteStrategy();
		//FIXME move to the Factory!!!!
		totalActivationStrategy = new DefaultTotalActivationStrategy();
	}
	
	/**
	 * @param l Another Learnable
	 */
	public LearnableImpl(Learnable l) {
		this(l.getActivation(), l.getActivatibleRemovalThreshold(),  l.getBaseLevelActivation(), l.getLearnableRemovalThreshold(),
			l.getExciteStrategy(), l.getDecayStrategy(),l.getBaseLevelExciteStrategy(), l.getBaseLevelDecayStrategy(), l.getTotalActivationStrategy());
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#decay(long)
	 */
	@Override
	public void decay(long ticks){
		decayBaseLevelActivation(ticks);
		super.decay(ticks);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#isRemovable()
	 */
	@Override
	public boolean isRemovable() {
		return baseLevelActivation <= learnableRemovableThreshold;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#getTotalActivation()
	 */
	@Override
	public double getTotalActivation() { 
	    return totalActivationStrategy.calculateTotalActivation(baseLevelActivation, getActivation());
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
	public void setBaseLevelRemovalThreshold(double threshold) {
		this.learnableRemovableThreshold = threshold;
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
	
}
