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
 * Strategy determining how a conflictor is inhibited.
 * @author Ryan J. McCall
 *
 */
public class ConflictorExciteStrategy extends StrategyImpl implements BehaviorExciteStrategy {

	private static final Logger logger = Logger.getLogger(ConflictorExciteStrategy.class.getCanonicalName());
	
	private final double DEFAULT_EXCITATION_FACTOR = 0.6;
	
	private double conflictorExcitationFactor = DEFAULT_EXCITATION_FACTOR;
	
	@Override
	public void init(Map<String, ?> parameters) {
		conflictorExcitationFactor = (Double) parameters.get("factor");
	}
	
	@Override
	public void exciteBehavior(Behavior sourceBehavior,
			Behavior recipientBehavior) {
		double inhibitionAmount = -(sourceBehavior.getActivation() * conflictorExcitationFactor)
		/ sourceBehavior.getContextSize();
		recipientBehavior.excite(inhibitionAmount);
		logger.log(Level.FINEST, sourceBehavior.getLabel() + " inhibits " + 
					recipientBehavior.getLabel() + " amount " + inhibitionAmount,
					TaskManager.getCurrentTick());

	}

}
