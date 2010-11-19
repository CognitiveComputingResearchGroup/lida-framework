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

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockTaskSpawner implements TaskSpawner {

	@Override
	public void addTask(LidaTask task) {

	}

	@Override
	public void cancelTask(LidaTask task) {

	}

	@Override
	public int getSpawnedTaskCount() {
		return 0;
	}

	@Override
	public Collection<LidaTask> getSpawnedTasks() {
		return null;
	}

	@Override
	public LidaTaskManager getTaskManager() {
		return null;
	}

	@Override
	public void receiveFinishedTask(LidaTask task) {

	}

	@Override
	public void setInitialTasks(Collection<? extends LidaTask> initialTasks) {
		

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
	public void setAssociatedModule(LidaModule module) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Map<String, ?> lidaProperties) {
		// TODO Auto-generated method stub
		
	}
	
}
