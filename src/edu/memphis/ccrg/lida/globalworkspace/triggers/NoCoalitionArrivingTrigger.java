/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Collection;
import java.util.TimerTask;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;


/**
 * 
 * This trigger fires when 'delay' ms has passed without a new {@link Coalition} being added to the {@link GlobalWorkspace}.
 * Check the parent class NoBroadcastTrigger for full understanding.
 * 
 * @author Javier Snaider
 *
 */
public class NoCoalitionArrivingTrigger extends NoBroadcastOccurringTrigger {
	
	/**
	 * Called each time a new coalition is added to the {@link GlobalWorkspace} each {@link BroadcastTrigger}'s 
	 * 'checkForTriggerCondition' method is called.
	 * In the case of this trigger the 'reset()' method inherited from {@link NoBroadcastOccurringTrigger}
	 * is called which resets the {@link TimerTask} object.  
	 * 
	 * Thus this trigger fires when 'delay' ms has passed without a new {@link Coalition} entering the {@link GlobalWorkspace}. 
	 * 
	 * @param coalitions Coalitions trigger will check
	 */
	@Override
	public void checkForTriggerCondition(Collection<Coalition> coalitions) {
		reset();
	}

}
