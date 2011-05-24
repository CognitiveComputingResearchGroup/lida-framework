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
package edu.memphis.ccrg.lida.actionselection.behaviornet;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public class MockTaskSpawner extends FrameworkTaskImpl implements TaskSpawner {

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl#runThisFrameworkTask()
	 */
	@Override
	protected void runThisFrameworkTask() {
		// not implemented
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#addTask(edu.memphis.ccrg.lida.framework.tasks.FrameworkTask)
	 */
	@Override
	public void addTask(FrameworkTask task) {
		try {
			task.setControllingTaskSpawner(this);
			task.call();
		} catch (Exception e) {
			// not implemented catch block
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#cancelTask(edu.memphis.ccrg.lida.framework.tasks.FrameworkTask)
	 */
	@Override
	public boolean cancelTask(FrameworkTask task) {
		// not implemented method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#getSpawnedTasks()
	 */
	@Override
	public Collection<FrameworkTask> getRunningTasks() {
		// not implemented method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#receiveFinishedTask(edu.memphis.ccrg.lida.framework.tasks.FrameworkTask)
	 */
	@Override
	public void receiveFinishedTask(FrameworkTask task) {
		// not implemented method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#setInitialTasks(java.util.Collection)
	 */
	@Override
	public void addTasks(Collection<? extends FrameworkTask> initialTasks) {
		// not implemented method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#setTaskManager(edu.memphis.ccrg.lida.framework.tasks.TaskManager)
	 */
	@Override
	public void setTaskManager(TaskManager taskManager) {
		// not implemented method stub

	}

	@Override
	public boolean containsTask(FrameworkTask t) {
		// not implemented method stub
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

}
