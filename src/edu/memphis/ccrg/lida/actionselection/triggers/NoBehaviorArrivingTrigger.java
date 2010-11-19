/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

public class NoBehaviorArrivingTrigger extends NoActionSelectionOccurringTrigger {

	/**
	 * Every time a new behavior is added 'checkForTrigger' is called for each trigger.
	 * In the case of this trigger the 'reset()' method inherited from NoActionSelectionOcurringTrigger
	 * is called which resets the timer task object.  
	 * 
	 * So this trigger fires when x ms has passed w/o a new action selection. 
	 * 
	 *
	 */
	//TODO Double check parameters
	public void checkForTrigger(Set<Behavior> behaviors, double maxActivation) {
		reset();
	}
}
