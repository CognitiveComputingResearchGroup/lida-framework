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

/**
 * @author Javier
 *
 */
/**
 * TriggerTask is executed when the Timer object goes off
 * In this case the global workspace is told to trigger 
 * the broadcast
 *
 */
public class TriggerTask extends LidaTaskImpl{
	
	private Logger logger = Logger.getLogger(TriggerTask.class.getCanonicalName());

	private String name;
	
	private TriggerListener gw;
	
	public TriggerTask(int ticksForCycle,TriggerListener gw,String name) {
		super(ticksForCycle);
		this.gw=gw;
		this.name=name;
	}

	@Override
	protected void runThisLidaTask() {
		logger.log(Level.FINE,name,LidaTaskManager.getCurrentTick());
		gw.triggerBroadcast();
		setTaskStatus(LidaTaskStatus.FINISHED);
	}		
	@Override
	public String toString(){
		return "TriggerTask "+name +" " + getTaskId();
	}


}
