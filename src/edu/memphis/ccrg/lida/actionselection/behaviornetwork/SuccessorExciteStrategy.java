/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.strategies.StrategyImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Strategy that determines how a successor is excited.
 * @author Ryan J. McCall
 *
 */
public class SuccessorExciteStrategy extends StrategyImpl implements BehaviorExciteStrategy {
	
	private static final Logger logger = Logger.getLogger(SuccessorExciteStrategy.class.getCanonicalName());
	
	private final double DEFAULT_EXCITATION_FACTOR = 0.6;
	
	private double successorExcitationFactor = DEFAULT_EXCITATION_FACTOR;
	
	@Override
	public void init(Map<String, ?> parameters) {
		successorExcitationFactor = (Double) parameters.get("factor");
	}

	@Override
	public void exciteBehavior(Behavior sourceBehavior, Behavior recipientBehavior) {
		double amount = sourceBehavior.getActivation() * successorExcitationFactor
						/ recipientBehavior.getUnsatisfiedContextCount();
		recipientBehavior.excite(amount);
		logger.log(Level.FINEST, sourceBehavior.getLabel() + " sends " + amount + 
				" to " + recipientBehavior.getLabel(), TaskManager.getCurrentTick());
//		double amount = behavior.getActivation() * successorExcitationFactor
//		/ successor.getUnsatisfiedContextCount();
//successor.excite(amount);
	}

}
