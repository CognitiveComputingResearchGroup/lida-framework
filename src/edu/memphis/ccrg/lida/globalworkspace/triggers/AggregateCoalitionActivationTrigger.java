/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * Implements a trigger that is activated when the sum of all {@link Coalition}s
 * in {@link GlobalWorkspace} is greater than the threshold.
 * 
 * @author Javier Snaider
 * 
 */
public class AggregateCoalitionActivationTrigger implements BroadcastTrigger {

	private Logger logger = Logger
			.getLogger(AggregateCoalitionActivationTrigger.class
					.getCanonicalName());
	protected GlobalWorkspace gw;
	protected double threshold;

	/**
	 * This method is executed each time a new {@link Coalition} enters the {@link GlobalWorkspace} and
	 * calculates the total activation.  If it is over threshold then the broadcast is triggered.
	 * 
	 * @param coalitions
	 *            a Collection of all the {@link Coalition}s in the {@link GlobalWorkspace}.
	 */
	@Override
	public void checkForTriggerCondition(Collection<Coalition> coalitions) {
		double acc = 0;
		for (Coalition c : coalitions) {
			acc = acc + c.getActivation();
		}
		if (acc > threshold) {
			logger.log(Level.FINE, "Aggregate Activation trigger ",
					TaskManager.getCurrentTick());
			gw.triggerBroadcast(this);
		}
	}

	@Override
	public void reset() {
		// not applicable
	}

	@Override
	public void init(Map<String, Object> parameters, GlobalWorkspace gw) {
		this.gw = gw;
		Object o = parameters.get("threshold");
		if ((o != null) && (o instanceof Double)) {
			threshold = (Double) o;
		}
	}

	@Override
	public void start() {
		// not applicable
	}

}