/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalValueStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Default implementation of {@link LearnableActivatible}.
 * 
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public class LearnableActivatibleImpl extends ActivatibleImpl implements LearnableActivatible {

	private static final Logger logger = Logger.getLogger(LearnableActivatibleImpl.class.getCanonicalName());
	private static final ElementFactory factory = ElementFactory.getInstance();
	
	private double baseLevelActivation;
	private double learnableRemovalThreshold;
	private ExciteStrategy baseLevelExciteStrategy;
	private DecayStrategy baseLevelDecayStrategy;
	private TotalValueStrategy totalActivationStrategy;

	/**
	 * Constructs a new instance with default values.
	 */
	public LearnableActivatibleImpl() {
		super();
		baseLevelActivation = DEFAULT_BASE_LEVEL_ACTIVATION;
		learnableRemovalThreshold = DEFAULT_LEARNABLE_REMOVAL_THRESHOLD;
		baseLevelDecayStrategy = factory.getDefaultDecayStrategy();
		baseLevelExciteStrategy = factory.getDefaultExciteStrategy();
		totalActivationStrategy = factory.getDefaultTotalValueStrategy();
	}
	
	/**
	 * Copy constructor.
	 * @deprecated This functionality is subsumed by {@link ElementFactory}.
	 * @param l {@link LearnableActivatibleImpl}
	 */
	@Deprecated
	public LearnableActivatibleImpl(LearnableActivatibleImpl l) {
		this(l.getActivation(), l.getActivatibleRemovalThreshold(),  l.getBaseLevelActivation(), l.getLearnableRemovalThreshold(),
			l.getExciteStrategy(), l.getDecayStrategy(),l.getBaseLevelExciteStrategy(), l.getBaseLevelDecayStrategy(), l.getTotalValueStrategy());
	}
	
	/**
	 * Constructs a new instance with specified attributes.
	 * @deprecated This functionality is subsumed by {@link ElementFactory}.
	 * @param activation current activation
	 * @param activatibleRemovalThreshold activation threshold needed for this instance to remain active
	 * @param baseLevelActivation base-level activation for learning
	 * @param learnableRemovalThreshold base-level activation needed for this instance to remain active
	 * @param exciteStrategy {@link ExciteStrategy} for exciting {@link ActivatibleImpl} activation.
	 * @param decayStrategy {@link DecayStrategy} for decaying {@link ActivatibleImpl} activation.
	 * @param baseLevelExciteStrategy {@link ExciteStrategy} for reinforcing {@link LearnableActivatibleImpl} base-level activation.
	 * @param baseLevelDecayStrategy {@link DecayStrategy} for decaying {@link LearnableActivatibleImpl} base-level activation.
	 * @param taStrategy {@link TotalValueStrategy} how this instance will calculate its total activation.
	 */
	@Deprecated
	public LearnableActivatibleImpl(double activation, double activatibleRemovalThreshold, double baseLevelActivation, double learnableRemovalThreshold,
			ExciteStrategy exciteStrategy, DecayStrategy decayStrategy, ExciteStrategy baseLevelExciteStrategy, DecayStrategy baseLevelDecayStrategy, TotalValueStrategy taStrategy) {
		super(activation, activatibleRemovalThreshold, exciteStrategy, decayStrategy);
		
		this.baseLevelActivation = baseLevelActivation;
		this.learnableRemovalThreshold=learnableRemovalThreshold;
		this.baseLevelExciteStrategy = baseLevelExciteStrategy;
		this.baseLevelDecayStrategy = baseLevelDecayStrategy;
		this.totalActivationStrategy = taStrategy;
	}
	
	/**
	 * If this method is overridden, this init() must be called first! i.e. super.init();
	 * Will set parameters with the following names:<br/><br/>
     * 
     * <b>learnableActivatible.baseLevelActivation</b> initial base-level activation<br/>
     * <b>learnableActivatible.baseLevelRemovalThreshold</b> initial removal threshold<br/>
     * <b>learnableActivatible.baseLevelDecayStrategy</b> name of base-level decay strategy<br/>
     * <b>learnableActivatible.baseLevelExciteStrategy</b> name of base-level excite strategy<br/>
     * <b>learnableActivatible.totalActivationStrategy</b> name of total activation strategy<br/><br/>
     * If any parameter is not specified its default value will be used.
     * 
     * @see Initializable
	 */
	@Override
	public void init(){
		baseLevelActivation = (Double) getParam("learnableActivatible.baseLevelActivation",DEFAULT_BASE_LEVEL_ACTIVATION);
		learnableRemovalThreshold = (Double) getParam("learnableActivatible.baseLevelRemovalThreshold", DEFAULT_LEARNABLE_REMOVAL_THRESHOLD);
		String decayName = (String) getParam("learnableActivatible.baseLevelDecayStrategy", factory.getDefaultDecayType());
		baseLevelDecayStrategy = factory.getDecayStrategy(decayName);
		
		String exciteName = (String) getParam("learnableActivatible.baseLevelExciteStrategy", factory.getDefaultExciteType());
		baseLevelExciteStrategy = factory.getExciteStrategy(exciteName);
		
		String name = getParam("learnableActivatible.totalActivationStrategy", 
												factory.getDefaultTotalValueStrategyType());
		totalActivationStrategy = (TotalValueStrategy) factory.getStrategy(name);
		if(totalActivationStrategy == null){
			totalActivationStrategy = (TotalValueStrategy) factory.getStrategy(factory.getDefaultTotalValueStrategyType());
		}
	}

	@Override
	public void decay(long ticks){
		decayBaseLevelActivation(ticks);
		super.decay(ticks);
	}
	
	@Override
	public boolean isRemovable() {
		return getBaseLevelActivation() <= learnableRemovalThreshold;
	}

	@Override
	public double getTotalActivation() { 
	    return totalActivationStrategy.calculateTotalValue(getBaseLevelActivation(), getActivation());
	}

	@Override
	public void decayBaseLevelActivation(long ticks) {
		if (baseLevelDecayStrategy != null) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "Before decaying {1} has base-level activation: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getBaseLevelActivation()});
			}
			synchronized(this){
				baseLevelActivation = baseLevelDecayStrategy.decay(getBaseLevelActivation(),ticks);
			}
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "After decaying {1} has base-level activation: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getBaseLevelActivation()});
			}
		}		
	}
	
	@Override
	public void reinforceBaseLevelActivation(double amount) {
		if (baseLevelExciteStrategy != null) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "Before reinforcement {1} has base-level activation: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getBaseLevelActivation()});
			}
			synchronized(this){
				baseLevelActivation = baseLevelExciteStrategy.excite(getBaseLevelActivation(), amount);
			}
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "After reinforcement {1} has base-level activation: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getBaseLevelActivation()});
			}
		}		
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return baseLevelExciteStrategy;
	}

	@Override
	public synchronized void setBaseLevelExciteStrategy(ExciteStrategy s) {
		baseLevelExciteStrategy = s;
	}

	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return baseLevelDecayStrategy;
	}

	@Override
	public synchronized void setBaseLevelDecayStrategy(DecayStrategy d) {
		baseLevelDecayStrategy = d;
	}

	@Override
	public void setBaseLevelActivation(double a) {
		if(a < 0.0){
			synchronized (this) {
				baseLevelActivation = 0.0;
			}
		}else if(a > 1.0){
			synchronized (this) {
				baseLevelActivation = 1.0;
			}
		}else{
			synchronized (this) {
				baseLevelActivation = a;
			}
		}		
	}
	
	@Override
	public double getBaseLevelActivation() {
		return baseLevelActivation;
	}

	@Override
	public double getLearnableRemovalThreshold() {
		return learnableRemovalThreshold;
	}

	@Override
	public synchronized void setBaseLevelRemovalThreshold(double t) {
		learnableRemovalThreshold = t;
	}
	
	@Override
	public TotalValueStrategy getTotalValueStrategy() {
		return totalActivationStrategy;
	}

	@Override
	public synchronized void setTotalValueStrategy(TotalValueStrategy s) {
		totalActivationStrategy = s;
	}
}