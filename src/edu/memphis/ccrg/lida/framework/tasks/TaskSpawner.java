/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.initialization.Initializable;

/**
 * TaskSpawners create, manage, and end new LidaTasks.  
 * 
 * @author Ryan J McCall
 *
 */
public interface TaskSpawner extends Initializable{

	/**
	 * Gets the number of Tasks in this Spawner
	 * @return number of tasks spawned
	 */
	public int getSpawnedTaskCount();
	/**
	 * Adds a new LidaTask to this Spawner.
	 * @param task the task to add.
	 */
	public void addTask(LidaTask task);
	/**
	 * returns a unmodifiable Collection that contains the LidaTasks in this Spawner
	 * @return collection
	 */
	public Collection<LidaTask> getSpawnedTasks();
	
	/**
	 * The supplied LidaTask will be start by the spawner right away.
	 */
	public void setInitialTasks(Collection<? extends LidaTask> initialTasks);
		
	/**
	 * This method receives the tasks that have finished. Each TaskSpawner can choose what to do 
	 * with each LidaTask each time it finished to run one step. Generally the LidaTask's status commands this 
	 * action.
	 * 
	 * @param task finished LidaTask
	 */
	public void receiveFinishedTask(LidaTask task);
	
	/**
	 * Cancels the task from the Task Queue. This is only possible if the tick for witch the task 
	 * is scheduled has not been reached.
	 * 
	 * @param task The task to cancel.
	 */
	public void cancelTask(LidaTask task);

	/**
	 * Returns the task manager for the taskspawner
	 * @return LidaTaskManager
	 */
	public LidaTaskManager getTaskManager();
	
	/**
	 * Set the LidaTaskManager this TaskSpawner will use to actually run the tasks
	 * @param taskManager task manager
	 */
	public void setTaskManager(LidaTaskManager taskManager);
}
