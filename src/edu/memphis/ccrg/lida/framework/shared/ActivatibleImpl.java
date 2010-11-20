/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Generic Activatible Implementation. Useful to inherit from it 
 * Activatible classes like nodes or codelets.
 *  
 * @author Javier Snaider
 * 
 */
public class ActivatibleImpl implements Activatible {
	
	private double activation;
	private ExciteStrategy exciteStrategy;
	private DecayStrategy decayStrategy;
	private static Logger logger = Logger.getLogger(ActivatibleImpl.class.getCanonicalName());

	public ActivatibleImpl() {
		activation = 0.0;
		exciteStrategy = new DefaultExciteStrategy();
		decayStrategy = new LinearDecayStrategy();
	}
	
	public ActivatibleImpl(double activation, ExciteStrategy eb, DecayStrategy db) {
		this.activation = activation;
		this.exciteStrategy = eb;
		this.decayStrategy = db;
	}

	public void decay(long ticks) {	
		if (decayStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has " + activation,LidaTaskManager.getActualTick());
			synchronized(this){
				activation = decayStrategy.decay(activation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has " + activation,LidaTaskManager.getActualTick());
		}
	}

	public void excite(double excitation) {	
		if (exciteStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before excite has " + activation,LidaTaskManager.getActualTick());
			synchronized(this){
				activation = exciteStrategy.excite(activation, excitation);
			}
			logger.log(Level.FINEST,this.toString() + " after excite has " + activation,LidaTaskManager.getActualTick());
		}
	}

	public double getActivation() {
		return activation;
	}

	public DecayStrategy getDecayStrategy() {
		return decayStrategy;
	}

	public ExciteStrategy getExciteStrategy() {
		return exciteStrategy;
	}

	public synchronized void setActivation(double d) {
		this.activation = d;
	}

	public synchronized void setDecayStrategy(DecayStrategy db) {
		this.decayStrategy = db;
	}

	public synchronized void setExciteStrategy(ExciteStrategy eb) {
		this.exciteStrategy = eb;
	}

	@Override
	public double getTotalActivation() {
		return activation;
	}

}//
