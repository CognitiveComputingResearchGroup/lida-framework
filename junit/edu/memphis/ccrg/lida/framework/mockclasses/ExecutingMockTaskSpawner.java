/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;

public class ExecutingMockTaskSpawner extends TaskSpawnerImpl implements
		TaskSpawner {
	public List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();

	@Override
	public void addTask(FrameworkTask task) {
		tasks.add(task);
		task.setControllingTaskSpawner(this);
		try {
			task.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean cancelTask(FrameworkTask task) {
		return tasks.remove(task);
	}

	@Deprecated
	@Override
	public Collection<FrameworkTask> getRunningTasks() {
		return getTasks();
	}

	@Override
	public Collection<FrameworkTask> getTasks() {
		return Collections.unmodifiableCollection(tasks);
	}

	@Override
	public void receiveFinishedTask(FrameworkTask task) {

	}

	@Override
	public void addTasks(Collection<? extends FrameworkTask> initialTasks) {
		for (FrameworkTask t : initialTasks) {
			addTask(t);
		}
	}

	@Override
	public void init() {
		// not implemented

	}

	@Override
	public boolean containsTask(FrameworkTask t) {
		return tasks.contains(t);
	}
}
