/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;


/**
 * This triggers activates if 'delay' ticks has passed without
 * a broadcast.
 * 
 * @author Javier Snaider
 * 
 */
public class NoBroadcastOccurringTrigger implements BroadcastTrigger {

	/**
	 * How long since last broadcast before this trigger is activated
	 */
	private int delay;
	
	/**
	 * Java library class used to handle the timing
	 */
	private TriggerTask task;
	private GlobalWorkspace gw;
	private String name="";
	private LidaTaskManager tm;

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

	/**
	 * This init expects an Integer with name "delay" standing for trigger delay.
	 * Also a String, "name" of the trigger for logging purposes.
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#setUp(java.util.Map)
	 */
	@Override
	public void init(Map<String, Object> parameters, GlobalWorkspace gw) {
		this.gw=gw;
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
	@Override
	public void start() {
		task=new TriggerTask(delay,gw,name);
		gw.getAssistingTaskSpawner().addTask(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	@Override
	public void checkForTriggerCondition(Collection<Coalition> coalitions) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#reset()
	 */
	@Override
	public void reset() {
		if (task != null)
			gw.getAssistingTaskSpawner().cancelTask(task);
		start();
	}

}