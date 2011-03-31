/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared.activation;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Generic Activatible Implementation. Useful to inherit from it 
 * Activatible classes like nodes or codelets.
 *  
 * @author Javier Snaider
 * 
 */
public class ActivatibleImpl implements Activatible {
	
	private static LidaElementFactory factory = LidaElementFactory.getInstance();

	private double activation;
	private ExciteStrategy exciteStrategy;
	private DecayStrategy decayStrategy;
	private double removableThreshold = DEFAULT_REMOVABLE_THRESHOLD;
	
	/*
	 * this prevents activatible from being removed if its activation is 0.0. e.g. pam nodes, schemes' context.
	 */
	private static final double DEFAULT_REMOVABLE_THRESHOLD = -1.0;
	
	private static final Logger logger = Logger.getLogger(ActivatibleImpl.class.getCanonicalName());

	public ActivatibleImpl() {
		activation = 0.0;
		decayStrategy = factory.getDefaultDecayStrategy();
		exciteStrategy = factory.getDefaultExciteStrategy();
	}
	
	public ActivatibleImpl(double activation, double removableThreshold, ExciteStrategy eb, DecayStrategy db) {
		this.activation = activation;
		this.removableThreshold = removableThreshold;
		this.exciteStrategy = eb;
		this.decayStrategy = db;
	}

	@Override
	public void decay(long ticks) {	
		if (decayStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has " + activation,LidaTaskManager.getCurrentTick());
			synchronized(this){
				activation = decayStrategy.decay(activation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has " + activation,LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public void excite(double excitation) {	
		if (exciteStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before excite has " + activation,LidaTaskManager.getCurrentTick());
			synchronized(this){
				activation = exciteStrategy.excite(activation, excitation);
			}
			logger.log(Level.FINEST,this.toString() + " after excite has " + activation,LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public double getActivation() {
		return activation;
	}
	
	@Override
	public double getActivatibleRemovalThreshold() {
		return removableThreshold;
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		return decayStrategy;
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		return exciteStrategy;
	}

	@Override
	public synchronized void setActivation(double d) {
		this.activation = d;
	}
	
	@Override
	public void setActivatibleRemovalThreshold(double threshold) {
		this.removableThreshold = threshold;
	}

	@Override
	public void setDecayStrategy(DecayStrategy db) {
		this.decayStrategy = db;
	}

	@Override
	public void setExciteStrategy(ExciteStrategy eb) {
		this.exciteStrategy = eb;
	}
	
	@Override
	public double getTotalActivation() {
		return activation;
	}

	@Override
	public boolean isRemovable() {
		return activation <= removableThreshold;
	}

}
