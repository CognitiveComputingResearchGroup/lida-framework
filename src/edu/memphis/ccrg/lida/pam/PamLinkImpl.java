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
import edu.memphis.ccrg.lida.framework.shared.activation.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * Default implementation of PamLink.
 * 
 * @author Ryan J. McCall
 */
public class PamLinkImpl extends LinkImpl implements PamLink {
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PamLinkImpl.class.getCanonicalName());
	
	private final Learnable learnable = new LearnableImpl();
	
	/**
	 * Instantiates a new pam link impl.
	 */
	public PamLinkImpl() {
		super();
		groundingPamLink = this;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#getTotalActivation()
	 */
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

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#decayBaseLevelActivation(long)
	 */
	@Override
	public void decayBaseLevelActivation(long ticks) {
		learnable.decayBaseLevelActivation(ticks);	
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#getBaseLevelActivation()
	 */
	@Override
	public double getBaseLevelActivation() {
		return learnable.getBaseLevelActivation();
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#getBaseLevelDecayStrategy()
	 */
	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return learnable.getBaseLevelDecayStrategy();
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#getBaseLevelExciteStrategy()
	 */
	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return learnable.getBaseLevelExciteStrategy();
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#reinforceBaseLevelActivation(double)
	 */
	@Override
	public void reinforceBaseLevelActivation(double amount) {
		learnable.reinforceBaseLevelActivation(amount);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#setBaseLevelActivation(double)
	 */
	@Override
	public void setBaseLevelActivation(double amount) {
		learnable.setBaseLevelActivation(amount);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#setBaseLevelDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)
	 */
	@Override
	public void setBaseLevelDecayStrategy(DecayStrategy strategy) {
		learnable.setBaseLevelDecayStrategy(strategy);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#setBaseLevelExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)
	 */
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
	
	//ACTIVATIBLE
	
	@Override
	public boolean isRemovable() {
		return learnable.isRemovable();
	}
	
	@Override
	public double getActivation(){
		return learnable.getActivation();
	}
	
	@Override
    public void setActivation(double activation){
		learnable.setActivation(activation);
	}
    
	@Override
    public void excite(double amount){
		learnable.excite(amount);
	}
	
	@Override
	public void setExciteStrategy(ExciteStrategy strategy){
		learnable.setExciteStrategy(strategy);
	}
	
	@Override
	public ExciteStrategy getExciteStrategy(){
		return learnable.getExciteStrategy();
	}
	
	@Override
	public void decay(long ticks){
		learnable.decay(ticks);
	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy){
		learnable.setDecayStrategy(strategy);
	}
	
	@Override
	public DecayStrategy getDecayStrategy(){
		return learnable.getDecayStrategy();
	}
	
	@Override
	public void setActivatibleRemovalThreshold(double threshold){
		learnable.setActivatibleRemovalThreshold(threshold);
	}
	
	@Override
	public double getActivatibleRemovalThreshold(){
		return learnable.getActivatibleRemovalThreshold();
	}
	        
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

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.Learnable#setBaseLevelExcitation(double)
	 */
	@Override
	public String toString() {
		return PamLinkImpl.class.getSimpleName() +  " " + getSource().getLabel() + " " + getSink().getLabel();
	}
	
	@Override
	public String getFactoryLinkType() {
		return PamLinkImpl.class.getSimpleName();
	}
	
}