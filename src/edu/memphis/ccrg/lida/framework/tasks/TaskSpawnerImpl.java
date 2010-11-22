/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * Implements the TaskSpawner interface.
 * 
 * @author Javier Snaider
 *
 */
public class TaskSpawnerImpl implements TaskSpawner {

	private static final long serialVersionUID = 5214851238994032189L;

	private static Logger logger = Logger.getLogger(TaskSpawnerImpl.class.getCanonicalName());
	
	private LidaTaskManager taskManager;

	/**
	 * The task currently being run
	 */
	private ConcurrentLinkedQueue<LidaTask> runningTasks = new ConcurrentLinkedQueue<LidaTask>();

	private Map<String, ?> parameters;

	public TaskSpawnerImpl() {
		super();
	}
	public TaskSpawnerImpl(LidaTaskManager tm) {
		super();
		taskManager=tm;
	}

	public void setInitialTasks(Collection<? extends LidaTask> initialTasks) {
		for (LidaTask r : initialTasks)
			addTask(r);
	}

	/**
	 * Add a task to this taskspawner to be run
	 */
	public void addTask(LidaTask task) {
		task.setTaskStatus(LidaTaskStatus.WAITING_TO_RUN);
		task.setControllingTaskSpawner(this);
		runningTasks.add(task);
		runTask(task);
		logger.log(Level.FINEST, "Task {1} added", new Object[]{LidaTaskManager.getActualTick(),task});
	}

	/**
	 * Schedule the LidaTask to be excecuted.
	 * Sets the task status to RUNNING.
	 * @param task
	 */
	protected void runTask(LidaTask task) {
		logger.log(Level.FINEST, "Running task {1}", new Object[]{LidaTaskManager.getActualTick(),task});
		task.setTaskStatus(LidaTaskStatus.RUNNING);
		taskManager.scheduleTask(task, task.getNextExcecutionTickLap());
	}

	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is overridden then is should still be called first using super.
	 */
	public void receiveFinishedTask(LidaTask task) {
		switch (task.getStatus()) {
		case FINISHED_WITH_RESULTS:
			processResults(task);
			removeTask(task);
			logger.log(Level.FINEST, "FINISHED_WITH_RESULTS {1}", new Object[]{LidaTaskManager.getActualTick(),task});
			break;
		case FINISHED:
			removeTask(task);
			logger.log(Level.FINEST, "FINISHED {1}", new Object[]{LidaTaskManager.getActualTick(),task});
			break;
		case CANCELED:
			removeTask(task);
			logger.log(Level.FINEST, "CANCELLED {1}", new Object[]{LidaTaskManager.getActualTick(),task});
			break;
		case TO_RESET:
			logger.log(Level.FINEST, "TO_RESET {1}", new Object[]{LidaTaskManager.getActualTick(),task});
			task.reset();
		case WAITING_TO_RUN:
		case RUNNING:
			logger.log(Level.FINEST, "RUNNING", new Object[]{LidaTaskManager.getActualTick(),task});
			task.setTaskStatus(LidaTaskStatus.WAITING_TO_RUN);
			runTask(task);
			break;
		}
	}// method

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS or FINISHED or CANCELLED
	 * This method is called to remove the task from this TaskSpawner
	 * @param task the LidaTask to remove.
	 */
	protected void removeTask(LidaTask task) {
		logger.log(Level.FINEST, "Cancelling task {1}", new Object[]{LidaTaskManager.getActualTick(),task});
		runningTasks.remove(task);
	}

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS
	 * This method is called to handle the results.
	 * 
	 * @param task
	 */
	protected void processResults(LidaTask task){
	}

	/**
	 * Returns an unmodifiable collection of the running tasks spawned by this task spawner
	 */
	public Collection<LidaTask> getSpawnedTasks() {
		logger.log(Level.FINEST, "Getting all tasks", LidaTaskManager.getActualTick());
		return Collections.unmodifiableCollection(runningTasks);
	}

	/**
	 * 
	 */
	public int getSpawnedTaskCount() {
		logger.log(Level.FINEST, "Getting spawned count", LidaTaskManager.getActualTick());
		return runningTasks.size();
	}

	/**
	 * Removes task from running task queue and tells task manager to cancel the task 
	 */
	public void cancelTask(LidaTask task) {
		removeTask(task);
		taskManager.cancelTask(task);		
	}
	/**
	 * @param taskManager task manager
	 */
	public void setTaskManager(LidaTaskManager taskManager) {
		this.taskManager = taskManager;
	}

	/**
	 * @return the LidaTaskManager
	 */
	public LidaTaskManager getTaskManager(){
		return taskManager;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.parameters = params;
		init();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init()
	 */
	@Override
	public void init() {
	}
	
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
	}

}// class