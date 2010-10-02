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

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class NoActionSelectionOccurringTrigger implements ActionSelectionTrigger {
	
	/**
	 * How long since last action selection before this trigger is activated
	 */
	protected int delay = 100;
	
	/**
	 * Java library class used to handle the timing
	 */
	private TriggerTask task;
	protected String name="";
	private LidaTaskManager tm;
	protected ActionSelection as;
	protected ActionSelectionDriver asd;

	/**
	 * @return the lidaTaskManager
	 */
	public LidaTaskManager getLidaTaskManager() {
		return tm;
	}

	/**
	 * @param lidaTaskManager the lidaTaskManager to set
	 */
	public void setLidaTaskManager(LidaTaskManager lidaTaskManager) {
		this.tm = lidaTaskManager;
	}

	public void setUp(Map<String, Object> parameters, ActionSelection as,ActionSelectionDriver asd) {
		this.as=as;
		this.asd=asd;
		Object o = parameters.get("delay");
		if ((o != null)&& (o instanceof Integer)) {
			delay= (Integer)o;
		}
		
		o = parameters.get("name");
		if ((o != null)&& (o instanceof String)) {
			name= (String)o;
		}		
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#start()
	 */
	public void start() {					
		task=new TriggerTask(delay,as,asd,name);		
		asd.addTask(task);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	public void checkForTrigger(Queue<Scheme> behaviors) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#reset()
	 */
	public void reset() {	
		if (task != null)
			asd.cancelTask(task);
		start();
	}
	
}
