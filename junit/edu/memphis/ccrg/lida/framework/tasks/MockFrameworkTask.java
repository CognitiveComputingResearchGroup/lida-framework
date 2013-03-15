/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import edu.memphis.ccrg.lida.framework.FrameworkModule;

public class MockFrameworkTask extends FrameworkTaskImpl {
	public MockFrameworkTask(int i, TaskSpawner taskSpawner) {
		super(i, taskSpawner);
	}

	public MockFrameworkTask(int i) {
		super(i);
	}

	public MockFrameworkTask() {
		super(10);
	}

	// @Override
	// public String toString() {
	// return "testTask";
	// }
	public boolean wasRun;

	@Override
	protected void runThisFrameworkTask() {
		wasRun = true;
	}

	public FrameworkModule associatedModule;
	public String moduleUsage;

	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		associatedModule = module;
		this.moduleUsage = moduleUsage;
	}
}
