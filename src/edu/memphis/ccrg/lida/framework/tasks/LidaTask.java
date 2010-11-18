/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.concurrent.Callable;

import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.shared.Activatible;


/**
 * This is the base Interface for all the task process in LIDA.
 * All piece of process in the LIDA Framework have to implement this interface.
 * A LidaTask is intended as a small fraction of a process. For example a Codelet or a Feature 
 * detector are examples of LidaTask. However, if the process includes a loop, the LidaTask represents 
 * only one iteration of this process.
 * 
 * TaskSpawners send this LidaTask to the LidaTaskManager which executes them. TaskSpawners use
 * CompletionService, so when the task has finished, the TaskSpawner can decide if this particular task
 * must run again or not. This is based on the status of the LidaTask.
 * The LidaTask should set its status during it execution.
 * 
 * Most classes can extend from LidaTaskImpl instead of implementing this interface from scratch.
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */
 
public interface LidaTask extends Callable<LidaTask>, Activatible, Initializable{
	
	/**
	 * returns the LidaTask's status code. It must be one of the static members of this interface. 
	 * @return the status code.
	 */
	public LidaTaskStatus getStatus();
	/**
	 * Sets the LidaTask's status code. It must be one of the static members of this interface. 
	 * @param status - code for this tasks current status
	 */
	public void setTaskStatus(LidaTaskStatus status);

	/**
	 * The LidaTask must stop.
	 */
	public void stopRunning();
	
	/**
	 * Each LidaTask is meant to have a unique id that is set at the time of creation.
	 * 
	 * @param id unique task identifier
	 */
	public void setTaskID(long id);
	
	/**
	 * Each LidaTask is meant to have a unique id that is set at the time of creation.
	 * 
	 * @return  id
	 */
	public long getTaskId();
	
	/**
	 * Sets how many ticks need to pass before this task is run
	 * @param ticks number of ticks
	 */
	public void setNumberOfTicksPerRun(int ticks);

	/**
	 * Gets the number of ticks needed to complete a cycle or 'one run' of this task
	 * @return ticks 
	 */
	public  int getTicksPerStep();
	
	/**
	 * Resets the LidaTask.
	 */
	public  void reset();
	

	public String toString();
	
	/**
	 * Sets the TaskSpawner that controls this LidaTask.
	 * @param ts the TaskSpawner
	 */
	public void setControllingTaskSpawner(TaskSpawner ts);
	
	/**
	 * Gets the TaskSpawner that controls this LidaTask.
	 * @return the TaskSpawner.
	 */
	public TaskSpawner getControllingTaskSpawner();
	
	/**
	 * Sets the the lap of ticks to schedule the next execution of this
	 * LidaTask.
	 * It should be set by the LidaTask.
	 * 
	 * @param lapTick the number of ticks to wait until the next execution.
	 */
	public void setNextExcecutionTickLap(long lapTick);
	
	/**
	 * Gets the the lap of ticks to schedule the next execution of this
	 * LidaTask.
	 */
	public long getNextExcecutionTickLap();
	
	/**
	 * @return The last ScheduledTick for this task. Could be in the future if this task is 
	 * scheduled for execution. 
	 */
	public long getScheduledTick();
	
	/**
	 * Sets the next scheduledTick for this task. This method is used by LidaTaskManager when
	 * a new task is added.
	 * 
	 * @param scheduledTick tick to schedule this task
	 */
	public void setScheduledTick(long scheduledTick);

}
