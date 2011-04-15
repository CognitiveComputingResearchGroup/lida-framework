/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
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
import java.util.Map;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockTaskSpawner implements TaskSpawner {
	
	private List<LidaTask> tasks = new ArrayList<LidaTask>(); 

	@Override
	public void addTask(LidaTask task) {
//		System.out.println(task+" added! To be run at "+task.getNextTicksPerStep()+" ticks from now");
		tasks.add(task);
		task.setControllingTaskSpawner(this);
		try {
			task.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean cancelTask(LidaTask task) {
		System.out.println(task+" removed! ");
		return tasks.remove(task);
	}

	@Override
	public Collection<LidaTask> getRunningTasks() {
		return Collections.unmodifiableCollection(tasks);
	}

	@Override
	public void receiveFinishedTask(LidaTask task) {

	}

	@Override
	public void addTasks(Collection<? extends LidaTask> initialTasks) {
		

	}

	@Override
	public void setTaskManager(LidaTaskManager taskManager) {
		

	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		
		return null;
	}

	@Override
	public void init() {
		// not implemented
		
	}

	@Override
	public void init(Map<String, ?> lidaProperties) {
		// not implemented
		
	}

	@Override
	public boolean containsTask(LidaTask t) {
		return tasks.contains(t);
	}
	
}
