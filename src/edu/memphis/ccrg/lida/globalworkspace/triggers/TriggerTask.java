/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * TriggerTask is executed when the Timer object goes off. In this case the
 * {@link GlobalWorkspace} is told to trigger the broadcast
 * 
 * @author Javier Snaider
 */
public class TriggerTask extends FrameworkTaskImpl {

	private Logger logger = Logger.getLogger(TriggerTask.class
			.getCanonicalName());

	private String name;

	private TriggerListener gw;
	
	private BroadcastTrigger trigger;

	/**
	 * Constructor for creating a TriggerTask
	 * @param ticksPerRun ticksPerRun of this {@link FrameworkTask}
	 * @param gw Trigger listener which is the {@link GlobalWorkspace}
	 * @param name Name of the trigger
	 * @param trigger {@link BroadcastTrigger} which creates this task
	 */
	public TriggerTask(int ticksPerRun, TriggerListener gw, String name, BroadcastTrigger trigger) {
		super(ticksPerRun);
		this.gw = gw;
		this.name = name;
		this.trigger=trigger;
	}

	@Override
	protected void runThisFrameworkTask() {
		logger.log(Level.FINE, name, TaskManager.getCurrentTick());
		gw.triggerBroadcast(trigger);
		setTaskStatus(TaskStatus.FINISHED);
	}

	@Override
	public String toString() {
		return name + "-" + super.toString();
	}

}
