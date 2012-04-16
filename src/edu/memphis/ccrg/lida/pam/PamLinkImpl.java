/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableActivatibleImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalValueStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Default implementation of {@link PamLink}.
 * 
 * @author Ryan J. McCall
 */
public class PamLinkImpl extends LinkImpl implements PamLink {
	
	private static final Logger logger = Logger.getLogger(PamLinkImpl.class.getCanonicalName());
	
	/*
	 * Private Learnable object used for all learnable methods
	 */
	private LearnableActivatibleImpl learnableImpl;
	
	/**
	 * Default constructor
	 * Only {@link ElementFactory} should be creating this Object.
	 */
	public PamLinkImpl() {
		super();
		groundingPamLink = this;
		learnableImpl = new LearnableActivatibleImpl();
	}
	/**
	 * Copy constructor
	 * @deprecated Use {@link ElementFactory#getLink(String, Node, Linkable, LinkCategory, String, String, double, double)} instead.
	 * @param pl source {@link PamLinkImpl}
	 */
	@Deprecated
	public PamLinkImpl(PamLinkImpl pl) {
		super(pl);
		groundingPamLink = this;
		learnableImpl=new LearnableActivatibleImpl((LearnableActivatibleImpl)pl.learnableImpl);
	}

	/** 
	 * Must call the {@link #init()} of the internal {@link LearnableActivatible}.
	 * @see LearnableActivatibleImpl#init()
	 * @see ElementFactory#getLink(String, Node, Linkable, LinkCategory, String, String, double, double)
	 */
	@Override
	public void init(){
		learnableImpl.init(getParameters());
	}

	//LINK
	@Override
	public void updateLinkValues(Link link) {
		if(link instanceof PamLinkImpl){
			PamLinkImpl pl = (PamLinkImpl) link;
			learnableImpl.setBaseLevelActivation(pl.getBaseLevelActivation());           
		}else if(link != null){
			logger.log(Level.FINEST, "Cannot set PamLinkImpl-specific values. Required: {1} \n Received: {2}",
					new Object[]{TaskManager.getCurrentTick(),PamLinkImpl.class.getCanonicalName(),link.getClass()});
		}
	}

	//OBJECT
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PamLinkImpl){
			PamLinkImpl l = (PamLinkImpl) obj;
			return getExtendedId().equals(l.getExtendedId());
		}
		return false;	
	}

	//LEARNABLE METHODS
	
	@Override
	public double getActivation() {
		return learnableImpl.getActivation();
	}

	@Override
	public void setActivation(double activation) {
		learnableImpl.setActivation(activation);
	}

	@Override
	public double getTotalActivation() {
		return learnableImpl.getTotalActivation();
	}

	@Override
	public void excite(double amount) {
		learnableImpl.excite(amount);
	}

	@Override
	public void setExciteStrategy(ExciteStrategy strategy) {
		learnableImpl.setExciteStrategy(strategy);
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		return learnableImpl.getExciteStrategy();
	}

	@Override
	public void decay(long ticks) {
		learnableImpl.decay(ticks);

	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
		learnableImpl.setDecayStrategy(strategy);
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		return learnableImpl.getDecayStrategy();
	}

	@Override
	public void setActivatibleRemovalThreshold(double threshold) {
		learnableImpl.setActivatibleRemovalThreshold(threshold);
	}

	@Override
	public double getActivatibleRemovalThreshold() {
		return learnableImpl.getActivatibleRemovalThreshold();
	}

	@Override
	public boolean isRemovable() {
		return learnableImpl.isRemovable();
	}

	@Override
	public double getBaseLevelActivation() {
		return learnableImpl.getBaseLevelActivation();
	}

	@Override
	public void setBaseLevelActivation(double amount) {
		learnableImpl.setBaseLevelActivation(amount);
	}

	@Override
	public void reinforceBaseLevelActivation(double amount) {
		learnableImpl.reinforceBaseLevelActivation(amount);
	}

	@Override
	public void setBaseLevelExciteStrategy(ExciteStrategy strategy) {
		learnableImpl.setBaseLevelExciteStrategy(strategy);
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return learnableImpl.getBaseLevelExciteStrategy();
	}

	@Override
	public void decayBaseLevelActivation(long ticks) {
		learnableImpl.decayBaseLevelActivation(ticks);
	}

	@Override
	public void setBaseLevelDecayStrategy(DecayStrategy strategy) {
		learnableImpl.setBaseLevelDecayStrategy(strategy);
	}

	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return learnableImpl.getBaseLevelDecayStrategy();
	}

	@Override
	public void setBaseLevelRemovalThreshold(double threshold) {
		learnableImpl.setBaseLevelRemovalThreshold(threshold);

	}

	@Override
	public double getLearnableRemovalThreshold() {
		return learnableImpl.getLearnableRemovalThreshold();
	}

	@Override
	public TotalValueStrategy getTotalValueStrategy() {
		return learnableImpl.getTotalValueStrategy();
	}

	@Override
	public void setTotalValueStrategy(TotalValueStrategy strategy) {
		learnableImpl.setTotalValueStrategy(strategy);
	}	
}