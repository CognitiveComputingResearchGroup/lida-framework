/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class AggregateBehaviorActivationTrigger implements
		ActionSelectionTrigger {

	private static final Logger logger = Logger.getLogger(AggregateBehaviorActivationTrigger.class.getCanonicalName());
	protected ActionSelection as;
	protected double threshold;
/**
 * This method is executed each time a new behavior enters the AS.
 * 
 * @param behaviors a Set with all the behaviors in the AS.
 */
	@Override
	public void checkForTrigger(Collection<Behavior> behaviors) {
		double acc=0;
		for(Behavior c:behaviors){
			acc=acc+c.getActivation();
		}
		if(acc>threshold){
			logger.log(Level.FINE,"Aggregate Activation trigger ",LidaTaskManager.getCurrentTick());
			//System.out.println("aggregate ");
			as.triggerActionSelection();
		}
	}

	@Override
	public void reset() {
		// not applicable
	}


	@Override
	public void start() {
		// not applicable
	}

	@Override
	public void setUp(Map<String, Object> parameters, ActionSelection as) {
		this.as=as;
		Object o = parameters.get("threshold");
		if ((o != null)&& (o instanceof Double)) {
			threshold= (Double)o;
		}
		
	}

}