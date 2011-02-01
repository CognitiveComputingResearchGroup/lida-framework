/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.concurrent.Callable;

import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;


/**
 * This is the base interface for all task process in the LIDA framework. 
 * All parts of processes in the LIDA Framework have to implement this interface.
 * A LidaTask is intended as a small fraction of a process. For example a Codelet or a Feature 
 * detector are examples of LidaTask. However, if the process includes a loop, one run of 
 * the LidaTask represents only one iteration of the loop.
 * 
 * TaskSpawners send LidaTasks to the LidaTaskManager which executes them. TaskSpawners receive
 * the task each time it finishes running, so the TaskSpawner can decide if this particular task
 * must run again or not. This is based on the status of the LidaTask.
 * The LidaTask should set its status during it execution.  Implementations of this interface should 
 * call the task's TaskSpawner method, {@link TaskSpawner#receiveFinishedTask(LidaTask)}, to handle the finished task at the end of the 'call' method.
 * 
 * Most classes can extend from LidaTaskImpl instead of implementing this interface from scratch.
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */
 
public interface LidaTask extends Callable<LidaTask>, Activatible, FullyInitializable{
	
	/**
	 * Returns status
	 * @return current LidaTask status
	 */
	public LidaTaskStatus getStatus();
	
	/**
	 * Sets task status. Intended to be called by runThisLidaTask() 
	 * Cannot use this method to cancel the task, instead use {@link #stopRunning()} to 
	 * cancel the task.       
	 * @param status the new task status 
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
	 * Sets default number of ticks between consecutive executions of this task.
	 * To change ticksPerStep for only the next run, use {@link #setNextTicksPerStep(long)}
	 * 
	 * @param ticks number of ticks
	 */
	public void setTicksPerStep(int ticks);

	/**
	 * Gets default number of ticks needed to complete a cycle or 'one run' of this task
	 * @return ticks 
	 */
	public  int getTicksPerStep();
	
	/**
	 * It is useful to override toString for Tasks for comprehension in the GUI.
	 */
	@Override
	public String toString();
	
	/**
	 * Sets TaskSpawner that controls this LidaTask.
	 * @param ts the TaskSpawner
	 */
	public void setControllingTaskSpawner(TaskSpawner ts);
	
	/**
	 * Gets TaskSpawner that controls this LidaTask.
	 * @return the TaskSpawner.
	 */
	public TaskSpawner getControllingTaskSpawner();
	
	/**
	 * Sets number of ticks for the next, and only the next, execution of this
	 * LidaTask.  It should be set by the LidaTask.  To set the permanent (default) number
	 * of ticks per step use instead {@link #setTicksPerStep(int)} 
	 * 
	 * @param ticks the number of ticks to wait until the next execution.
	 */
	public void setNextTicksPerStep(long ticks);
	
	/**
	 * Gets number of ticks per step for the next scheduled execution of this
	 * LidaTask.
	 * @return 
	 * 
	 */
	public long getNextTicksPerStep();
	
	/**
	 * Sets tick when this task will be run next. This method is used by LidaTaskManager when
	 * a new task is added.
	 * 
	 * @see LidaTaskManager
	 * @param scheduledTick tick to schedule this task
	 */
	public void setScheduledTick(long scheduledTick);

	/**
	 * Returns the tick when this task is scheduled to run next.
	 * Could be in the future if this task is already scheduled for execution.
	 * @return scheduledTick tick when this task will run next
	 */
	public long getScheduledTick();
}

