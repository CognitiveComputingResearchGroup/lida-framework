/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

public class PamLinkImpl extends LinkImpl implements PamLink {
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PamLinkImpl.class.getCanonicalName());
	
	private final Learnable learnable = new LearnableImpl();
	
	public PamLinkImpl() {
		super();
		groundingPamLink = this;
	}
	
	@Override
	public double getTotalActivation(){
		return learnable.getTotalActivation();
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
	public double getBaseLevelExcitation() {
		return learnable.getBaseLevelExcitation();
	}

	@Override
	public void setBaseLevelExcitation(double excitation) {
		learnable.setBaseLevelExcitation(excitation);
	}
	
	//TODO double check
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PamLinkImpl))
			return false;
		PamLinkImpl l = (PamLinkImpl) obj;
		return getExtendedId().equals(l.getExtendedId());
	}
	@Override
	public int hashCode() { 
	    return getExtendedId().hashCode();
	}

	@Override
	public String toString() {
		return PamLinkImpl.class.getSimpleName() +  " " + getSource().getLabel() + " " + getSink().getLabel();
	}
	
	@Override
	public String getFactoryLinkType() {
		return PamLinkImpl.class.getSimpleName();
	}
	
}
