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

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class PamNodeImpl extends NodeImpl implements PamNode{
	
	private static Logger logger = Logger.getLogger(PamNodeImpl.class.getCanonicalName());
	
//	protected static final double MIN_ACTIVATION = 0.0;
//	protected static final double MAX_ACTIVATION = 1.0;
//	
//	/** Activation required for node to be part of the percept.
//	 *  Bounded by minActivation and maxActivation
//	 */
//	protected double selectionThreshold = 0.9;
	
	/**
	 * 
	 */
	protected double baseLevelActivation = 0.0;
	
	private DecayStrategy baseLevelDecayStrategy = new LinearDecayStrategy();
	private ExciteStrategy baseLevelExciteStrategy = new DefaultExciteStrategy();
	
	public PamNodeImpl() {
		super();
		groundingPamNode = this;
	}

	public PamNodeImpl(PamNodeImpl p) {
		super(p);
//		selectionThreshold = p.selectionThreshold;
		baseLevelActivation = p.baseLevelActivation;
	}
	
//	/**
//	  * Determines if this node is relevant. A node is relevant if its total
//	  * activation is greater or equal to the selection threshold.
//	  * 
//	  * @return     <code>true</code> if this node is relevant
//	  */
//	public boolean isOverThreshold() {
//	    return getTotalActivation() >= selectionThreshold;
//	}
//
//	/**
//	 * @param threshold amount needed to enter percept 
//	 */
//	public void setPerceptThreshold(double threshold) {
//		selectionThreshold = threshold;
//	}
//
//	/**
//	 * returns selection threshold
//	 * @return Selection threshold
//	 */
//	public double getPerceptThreshold() {
//	    return selectionThreshold;
//	}

	@Override
	public double getTotalActivation() {
	    return getActivation() + baseLevelActivation;
	}

//	public double getMaxActivation() {
//		return MAX_ACTIVATION;
//	}
//
//	public double getMinActivation() {
//		return MIN_ACTIVATION;
//	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PamNodeImpl))
			return false;
		return getId() == ((PamNodeImpl) obj).getId();
	}

	@Override
	public int hashCode() { 
	    return getId();
	}

	@Override
	public String toString() {
		return getId() + " total activation: " + getTotalActivation();	
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

	
}//class