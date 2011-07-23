/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * If any coalition is above threshold, this trigger will fire.
 * 
 * @author Javier Snaider
 *
 */
public class IndividualCoaltionActivationTrigger extends AggregateCoalitionActivationTrigger {
	private Logger logger = Logger.getLogger(IndividualCoaltionActivationTrigger.class.getCanonicalName());
	
	/**
	 * Triggers a broadcast if any {@link Coalition} has activation over threshold.
	 * @param coalitions Coalitions
	 */
	@Override
	public void checkForTriggerCondition(Collection<Coalition> coalitions) {
		for(Coalition c:coalitions){
			if(c.getActivation()>threshold){
				logger.log(Level.FINE,"Individual Activation trigger ",TaskManager.getCurrentTick());

				gw.triggerBroadcast(this);
				break;
			}
		}
	}

}
