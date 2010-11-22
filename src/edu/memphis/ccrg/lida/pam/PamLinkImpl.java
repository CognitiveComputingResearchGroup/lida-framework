/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class PamLinkImpl extends LinkImpl implements PamLink {
	
	private static Logger logger = Logger.getLogger(PamLinkImpl.class.getCanonicalName());
	
	protected static final double MIN_ACTIVATION = 0.0;
	protected static final double MAX_ACTIVATION = 1.0;
	protected double selectionThreshold = 0.5;
	protected double baseLevelActivation = 0.0;
	
	private DecayStrategy baseLevelDecayStrategy = new LinearDecayStrategy();
	private ExciteStrategy baseLevelExciteStrategy = new DefaultExciteStrategy();

	public PamLinkImpl() {
		super();
		//TODO make this field private?
		super.groundingPamLink = this;
	}
	
	@Override
	public double getMaxActivation() {
		return MAX_ACTIVATION;
	}

	@Override
	public double getMinActivation() {
		return MIN_ACTIVATION;
	}

	@Override
	public double getPerceptThreshold() {
		return selectionThreshold;
	}

	@Override
	public boolean isOverThreshold() {
		return getTotalActivation() >= selectionThreshold;
	}
	
	public double getTotalActivation(){
		return getActivation() + baseLevelActivation;
	}

	@Override
	public void setPerceptThreshold(double threshold) {
		this.selectionThreshold = threshold;		
	}

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
	public double getBaseLevelActivation() {
		return this.baseLevelActivation;
	}

	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return this.baseLevelDecayStrategy;
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return this.baseLevelExciteStrategy;
	}

	@Override
	public void reinforceBaseLevelActivation(double amount) {
		baseLevelActivation = baseLevelExciteStrategy.excite(baseLevelActivation, amount);
	}

	@Override
	public void setBaseLevelActivation(double amount) {
		baseLevelActivation = amount;
	}

	@Override
	public void setBaseLevelDecayStrategy(DecayStrategy strategy) {
		baseLevelDecayStrategy = strategy;
	}

	@Override
	public void setBaseLevelExciteStrategy(ExciteStrategy strategy) {
		baseLevelExciteStrategy = strategy;		
	}	
}
