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

import edu.memphis.ccrg.lida.framework.initialization.AgentXmlFactory;

/**
 * Maintains a queue of running tasks and their task status. Methods to add and
 * cancel tasks. This implementation actually uses {@link TaskManager} to
 * execute the tasks.
 * 
 * @author Javier Snaider
 */
public class TaskSpawnerImpl implements TaskSpawner {

	private static final Logger logger = Logger.getLogger(TaskSpawnerImpl.class
			.getCanonicalName());

	private TaskManager taskManager;

	/*
	 * The tasks currently being run
	 */
	private ConcurrentLinkedQueue<FrameworkTask> runningTasks = new ConcurrentLinkedQueue<FrameworkTask>();

	private Map<String, ?> parameters;

	/**
	 * This default constructor is used by the {@link AgentXmlFactory}.
	 * {@link TaskManager} must be set using {@link TaskSpawner#setTaskManager(TaskManager)}
	 */
	public TaskSpawnerImpl() {
	}

	/**
	 * Convenience constructor that sets the {@link TaskManager}
	 * @param tm the {@link TaskManager} to set
	 */
	public TaskSpawnerImpl(TaskManager tm) {
		taskManager = tm;
	}

	@Override
	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public void addTasks(Collection<? extends FrameworkTask> initialTasks) {
		for (FrameworkTask r : initialTasks) {
			addTask(r);
		}
	}

	@Override
	public void addTask(FrameworkTask task) {
		task.setTaskStatus(TaskStatus.WAITING_TO_RUN);
		task.setControllingTaskSpawner(this);
		runningTasks.add(task);
		runTask(task);
		logger.log(Level.FINEST, "Task {1} added", new Object[] {
				TaskManager.getCurrentTick(), task });
	}

	/**
	 * Schedule the FrameworkTask to be executed. Sets task status to RUNNING.
	 * 
	 * @param task
	 */
	protected void runTask(FrameworkTask task) {
		logger.log(Level.FINEST, "Running task {1}", new Object[] {
				TaskManager.getCurrentTick(), task });
		task.setTaskStatus(TaskStatus.RUNNING);
		taskManager.scheduleTask(task, task.getNextTicksPerStep());
	}

	@Override
	public void receiveFinishedTask(FrameworkTask task) {
		switch (task.getTaskStatus()) {
		case FINISHED_WITH_RESULTS:
			processResults(task);
			removeTask(task);
			logger.log(Level.FINEST, "FINISHED_WITH_RESULTS {1}", new Object[] {
					TaskManager.getCurrentTick(), task });
			break;
		case FINISHED:
			removeTask(task);
			logger.log(Level.FINEST, "FINISHED {1}", new Object[] {
					TaskManager.getCurrentTick(), task });
			break;
		case CANCELED:
			removeTask(task);
			logger.log(Level.FINEST, "CANCELLED {1}", new Object[] {
					TaskManager.getCurrentTick(), task });
			break;
		case WAITING_TO_RUN:
		case RUNNING:
			logger.log(Level.FINEST, "RUNNING",
					new Object[] { TaskManager.getCurrentTick(), task });
			task.setTaskStatus(TaskStatus.WAITING_TO_RUN);
			runTask(task);
			break;
		}
	}

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS
	 * or FINISHED or CANCELLED This method is called to remove the task from
	 * this TaskSpawner
	 * 
	 * @param task
	 *            the FrameworkTask to remove.
	 */
	protected void removeTask(FrameworkTask task) {
		logger.log(Level.FINEST, "Cancelling task {1}", new Object[] {
				TaskManager.getCurrentTick(), task });
		runningTasks.remove(task);
	}

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS
	 * This method is called to handle the results.
	 * 
	 * @param task
	 */
	protected void processResults(FrameworkTask task) {
	}

	/**
	 * Returns an unmodifiable collection of the running tasks managed by this
	 * TaskSpawner
	 */
	@Override
	public Collection<FrameworkTask> getRunningTasks() {
		logger.log(Level.FINEST, "Getting all tasks",
				TaskManager.getCurrentTick());
		return Collections.unmodifiableCollection(runningTasks);
	}

	/*
	 * Removes FrameworkTask from task queue and tells TaskManager to cancel the
	 * task
	 */
	@Override
	public boolean cancelTask(FrameworkTask task) {
		if(containsTask(task)){
			removeTask(task);
			return taskManager.cancelTask(task);
		}
		return false;
	}

	@Override
	public boolean containsTask(FrameworkTask task) {
		return runningTasks.contains(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModule#init(java.util.Properties)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.parameters = params;
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModule#init()
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