/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Collection;

import org.apache.commons.collections15.collection.UnmodifiableCollection;

import edu.memphis.ccrg.lida.framework.initialization.Initializable;

/**
 * TaskSpawners manage {@link LidaTask}s. Maintains a {@link Collection} of all added tasks.
 * Provides method to process the result of a {@link LidaTask}.
 * 
 * @author Ryan J McCall
 */
public interface TaskSpawner extends Initializable {
	
	/**
	 * Set the LidaTaskManager this TaskSpawner will use to actually run the
	 * tasks.
	 * 
	 * @param taskManager the {@link LidaTaskManager} of the system.
	 */
	public void setTaskManager(LidaTaskManager taskManager);
	
	/**
	 * Adds and runs supplied LidaTask.
	 * @param task the task to add.
	 */
	public void addTask(LidaTask task);

	/**
	 * Adds and runs supplied LidaTasks.
	 * 
	 * @param tasks
	 *            a collection of tasks to be run.
	 */
	public void addTasks(Collection<? extends LidaTask> tasks);
		
	/**
	 * This method receives a task  that has finished. TaskSpawners can choose what to do 
	 * with the LidaTask each time it finishes running. Generally the LidaTask's {@link LidaTaskStatus}
	 * determines this action.
	 * 
	 * @param task finished {@link LidaTask}
	 */
	public void receiveFinishedTask(LidaTask task);
	
	/**
	 * Cancels specified task if it exists in this {@link TaskSpawner}
	 * Task is removed from {@link TaskSpawner} and canceled in the {@link LidaTaskManager}. 
	 * This is only possible if the tick for which the task 
	 * is scheduled has not been reached.
	 * 
	 * @param task The task to cancel.
	 * @see LidaTaskManager#cancelTask(LidaTask)
	 * @return true if the task was canceled, false otherwise
	 */
	public boolean cancelTask(LidaTask task);
	
	/**
	 * Returns a {@link UnmodifiableCollection} that contains the LidaTasks in this
	 * TaskSpawner.
	 * 
	 * @return collection of running tasks.
	 */
	public Collection<LidaTask> getRunningTasks();

	/**
	 * Returns whether this TaskSpawner manages this task.
	 * @param t a LidaTask
	 * @return True if this taskspawner contains a task with t's id
	 */
	public boolean containsTask(LidaTask t);
	
}
