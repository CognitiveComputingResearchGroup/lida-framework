/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockTaskSpawner implements TaskSpawner {

	@Override
	public void addTask(LidaTask task) {

	}

	@Override
	public void cancelTask(LidaTask task) {

	}

	@Override
	public Collection<LidaTask> getRunningTasks() {
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Map<String, ?> lidaProperties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsTask(LidaTask t) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
