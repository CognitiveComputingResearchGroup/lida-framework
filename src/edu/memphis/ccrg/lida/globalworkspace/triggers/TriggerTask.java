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

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * TriggerTask is executed when the Timer object goes off. In this case the
 * {@link GlobalWorkspace} is told to trigger the broadcast
 * 
 * @author Javier
 */
public class TriggerTask extends LidaTaskImpl {

	private Logger logger = Logger.getLogger(TriggerTask.class
			.getCanonicalName());

	private String name;

	private TriggerListener gw;
	
	private BroadcastTrigger trigger;

	/**
	 * Constructor for creating a TriggerTask
	 * @param ticksForCycle ticks per step for this task
	 * @param gw Trigger listener which is the Global Workspace
	 * @param name Name of the trigger
	 * @param trigger The trigger which creates this task
	 */
	public TriggerTask(int ticksForCycle, TriggerListener gw, String name, BroadcastTrigger trigger) {
		super(ticksForCycle);
		this.gw = gw;
		this.name = name;
		this.trigger=trigger;
	}

	@Override
	protected void runThisLidaTask() {
		logger.log(Level.FINE, name, LidaTaskManager.getCurrentTick());
		gw.triggerBroadcast(trigger);
		setTaskStatus(LidaTaskStatus.FINISHED);
	}

	@Override
	public String toString() {
		return "TriggerTask " + name + " " + getTaskId();
	}

}
