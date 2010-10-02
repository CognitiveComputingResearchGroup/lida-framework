/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class AggregateBehaviorActivationTrigger implements
		ActionSelectionTrigger {

	private Logger logger = Logger.getLogger("lida.actionselection.triggers");
	protected ActionSelection as;
	protected double threshold;
	protected ActionSelectionDriver asd;
/**
 * This method is executed each time a new behavior enters the AS.
 * 
 * @param behaviors a Set with all the behaviors in the AS.
 */
	public void checkForTrigger(Queue<Scheme> behaviors) {
		double acc=0;
		for(Scheme c:behaviors){
			acc=acc+c.getActivation();
		}
		if(acc>threshold){
			logger.log(Level.FINE,"Aggregate Activation trigger ",LidaTaskManager.getActualTick());
			//System.out.println("aggregate ");
			as.triggerActionSelection();
		}
	}//method

	public void reset() {
		// not applicable
	}


	public void start() {
		// not applicable
	}

	@Override
	public void setUp(Map<String, Object> parameters, ActionSelection as,ActionSelectionDriver asd) {
		this.as=as;
		this.asd=asd;
		Object o = parameters.get("threshold");
		if ((o != null)&& (o instanceof Double)) {
			threshold= (Double)o;
		}
		
	}

}//class