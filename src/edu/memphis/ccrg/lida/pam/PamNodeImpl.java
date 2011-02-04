/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

public class PamNodeImpl extends NodeImpl implements PamNode{
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PamNodeImpl.class.getCanonicalName());
	
	private final Learnable learnable = new LearnableImpl();
	
	public PamNodeImpl() {
		super();
		groundingPamNode = this;
	}

	public PamNodeImpl(PamNodeImpl p) {
		super(p);
	}
	
	@Override
	public double getTotalActivation(){
		return learnable.getTotalActivation();
	}
	
	@Override
	public TotalActivationStrategy getTotalActivationStrategy() {
		return learnable.getTotalActivationStrategy();
	}

	@Override
	public void setTotalActivationStrategy(TotalActivationStrategy strategy) {
		learnable.setTotalActivationStrategy(strategy);
	}

	@Override
	public void decayBaseLevelActivation(long ticks) {
		learnable.decayBaseLevelActivation(ticks);	
	}

	@Override
	public double getBaseLevelActivation() {
		return learnable.getBaseLevelActivation();
	}

	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return learnable.getBaseLevelDecayStrategy();
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return learnable.getBaseLevelExciteStrategy();
	}

	@Override
	public void reinforceBaseLevelActivation(double amount) {
		learnable.reinforceBaseLevelActivation(amount);
	}

	@Override
	public void setBaseLevelActivation(double amount) {
		learnable.setBaseLevelActivation(amount);
	}

	@Override
	public void setBaseLevelDecayStrategy(DecayStrategy strategy) {
		learnable.setBaseLevelDecayStrategy(strategy);
	}

	@Override
	public void setBaseLevelExciteStrategy(ExciteStrategy strategy) {
		learnable.setBaseLevelExciteStrategy(strategy);		
	}	
	
	@Override
	public double getLearnableRemovalThreshold() {
		return learnable.getLearnableRemovalThreshold();
	}

	@Override
	public void setLearnableRemovalThreshold(double threshold) {
		learnable.setLearnableRemovalThreshold(threshold);
	}
	
	@Override
	public boolean isRemovable() {
		return learnable.isRemovable();
	}

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
		return PamNodeImpl.class.getSimpleName() +  " " + getLabel() + " " + getId();
	}
	
	@Override
	public String getFactoryNodeType() {
		return PamNodeImpl.class.getSimpleName();
	}
}