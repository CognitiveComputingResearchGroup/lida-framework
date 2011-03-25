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

/**
 * Maintains a queue of running tasks and their task status.  Methods to add and cancel tasks.
 * This implementation actually uses {@link LidaTaskManager} to execute the tasks. 
 *
 * @author Javier Snaider
 */
public class TaskSpawnerImpl implements TaskSpawner {

	private static final Logger logger = Logger.getLogger(TaskSpawnerImpl.class.getCanonicalName());
	
	private LidaTaskManager taskManager;

	/*
	 * The tasks currently being run
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
	
	@Override
	public void setTaskManager(LidaTaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public void addTasks(Collection<? extends LidaTask> initialTasks) {
		for (LidaTask r : initialTasks){
			addTask(r);
		}
	}

	@Override
	public void addTask(LidaTask task) {
		task.setTaskStatus(LidaTaskStatus.WAITING_TO_RUN);
		task.setControllingTaskSpawner(this);
		runningTasks.add(task);
		runTask(task);
		logger.log(Level.FINEST, "Task {1} added", new Object[]{LidaTaskManager.getCurrentTick(),task});
	}

	/**
	 * Schedule the LidaTask to be executed.
	 * Sets task status to RUNNING.
	 * @param task
	 */
	protected void runTask(LidaTask task) {
		logger.log(Level.FINEST, "Running task {1}", new Object[]{LidaTaskManager.getCurrentTick(),task});
		task.setTaskStatus(LidaTaskStatus.RUNNING);
		taskManager.scheduleTask(task, task.getNextTicksPerStep());
	}

	@Override
	public void receiveFinishedTask(LidaTask task) {
		switch (task.getStatus()) {
		case FINISHED_WITH_RESULTS:
			processResults(task);
			removeTask(task);
			logger.log(Level.FINEST, "FINISHED_WITH_RESULTS {1}", new Object[]{LidaTaskManager.getCurrentTick(),task});
			break;
		case FINISHED:
			removeTask(task);
			logger.log(Level.FINEST, "FINISHED {1}", new Object[]{LidaTaskManager.getCurrentTick(),task});
			break;
		case CANCELED:
			removeTask(task);
			logger.log(Level.FINEST, "CANCELLED {1}", new Object[]{LidaTaskManager.getCurrentTick(),task});
			break;
		case WAITING_TO_RUN:
		case RUNNING:
			logger.log(Level.FINEST, "RUNNING", new Object[]{LidaTaskManager.getCurrentTick(),task});
			task.setTaskStatus(LidaTaskStatus.WAITING_TO_RUN);
			runTask(task);
			break;
		}
	}

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS or FINISHED or CANCELLED
	 * This method is called to remove the task from this TaskSpawner
	 * @param task the LidaTask to remove.
	 */
	protected void removeTask(LidaTask task) {
		logger.log(Level.FINEST, "Cancelling task {1}", new Object[]{LidaTaskManager.getCurrentTick(),task});
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
	 * Returns an unmodifiable collection of the running tasks managed by this TaskSpawner
	 */
	@Override
	public Collection<LidaTask> getRunningTasks() {
		logger.log(Level.FINEST, "Getting all tasks", LidaTaskManager.getCurrentTick());
		return Collections.unmodifiableCollection(runningTasks);
	}

	/**
	 * Removes LidaTask from task queue and tells LidaTaskManager to cancel the task 
	 */
	@Override
	public void cancelTask(LidaTask task) {
		removeTask(task);
		taskManager.cancelTask(task);		
	}

	@Override
	public boolean containsTask(LidaTask t) {
		return runningTasks.contains(t);
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

}