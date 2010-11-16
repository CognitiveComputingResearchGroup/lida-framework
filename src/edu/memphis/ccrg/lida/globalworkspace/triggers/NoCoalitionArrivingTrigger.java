/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;


/**
 * 
 * This trigger fires when x ms has passed w/o a new broadcast.
 * Check the parent class NoBroadcastTrigger for full understanding.
 * 
 * @author Javier Snaider
 *
 */
public class NoCoalitionArrivingTrigger extends NoBroadcastOccurringTrigger {
	
	/**
	 * Every time a new coalition is added 'checkForTrigger' is called for each trigger.
	 * In the case of this trigger the 'reset()' method inherited from NoBroadcastTrigger
	 * is called which resets the timer task object.  
	 * 
	 * So this trigger fires when x ms has passed w/o a new broadcast. 
	 * 
	 * @param coalitions Coalitions trigger will check
	 * TODO is this second variable a bug? 
	 * @param maxActivation 
	 */
	public void checkForTrigger(Set<Coalition> coalitions, double maxActivation) {
		reset();
	}

}
