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
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;


/**
 * This is the base interface for all task process in the LIDA framework. 
 * All parts of processes in the LIDA Framework have to implement this interface.
 * A FrameworkTask is intended as a small fraction of a process. For example a Codelet or a Feature 
 * detector are examples of FrameworkTask. However, if the process includes a loop, one run of 
 * the FrameworkTask represents only one iteration of the loop.
 * 
 * TaskSpawners send FrameworkTasks to the TaskManager which executes them. TaskSpawners receive
 * the task each time it finishes running, so the TaskSpawner can decide if this particular task
 * must run again or not. This is based on the status of the FrameworkTask.
 * The FrameworkTask should set its status during it execution.  Implementations of this interface should 
 * call the task's TaskSpawner method, {@link TaskSpawner#receiveFinishedTask(FrameworkTask)}, to handle the finished task at the end of the 'call' method.
 * 
 * Most classes can extend from FrameworkTaskImpl instead of implementing this interface from scratch.
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */ 
public interface FrameworkTask extends Callable<FrameworkTask>, Learnable, FullyInitializable{
	
	/**
	 * Returns status
	 * @return current FrameworkTask status
	 */
	public TaskStatus getStatus();
	
	/**
	 * Sets task status. Intended to be called by runThisFrameworkTask() 
	 * Cannot use this method to cancel the task, instead use {@link #stopRunning()} to 
	 * cancel the task.       
	 * @param status the new task status 
	 */
	public void setTaskStatus(TaskStatus status);

	/**
	 * Tells this FrameworkTask to shutdown.
	 */
	public void stopRunning();
	
	/**
	 * Each FrameworkTask is meant to have a unique id that is set at the time of creation.
	 * 
	 * @return  id unique task identifier
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
	 * Sets TaskSpawner that controls this FrameworkTask.
	 * @param ts the TaskSpawner
	 */
	public void setControllingTaskSpawner(TaskSpawner ts);
	
	/**
	 * Gets TaskSpawner that controls this FrameworkTask.
	 * @return the TaskSpawner.
	 */
	public TaskSpawner getControllingTaskSpawner();
	
	/**
	 * Sets number of ticks for the next, and only the next, execution of this
	 * FrameworkTask.  It should be set by the FrameworkTask.  To set the permanent (default) number
	 * of ticks per step use instead {@link #setTicksPerStep(int)} 
	 * 
	 * @param ticks the number of ticks to wait until the next execution.
	 */
	public void setNextTicksPerStep(long ticks);
	
	/**
	 * Gets nextTicksPerStep
	 * @return number of ticks per step for the next scheduled execution of this
	 * FrameworkTask.
	 */
	public long getNextTicksPerStep();
	
	/**
	 * Sets tick when this task will be run next. This method is used by TaskManager when
	 * a new task is added.
	 * 
	 * @see TaskManager
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

