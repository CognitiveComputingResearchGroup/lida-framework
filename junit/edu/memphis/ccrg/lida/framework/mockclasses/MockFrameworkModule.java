/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;

public class MockFrameworkModule extends FrameworkModuleImpl {

	public boolean wasDecayed;
	public long decayTicks;
	public List<ModuleListener> listeners = new ArrayList<ModuleListener>();

	@Override
	public Object getModuleContent(Object... params) {

		return null;
	}

	@Override
	public void init() {

	}

	@Override
	public void addListener(ModuleListener listener) {
		listeners.add(listener);

	}

	@Override
	public void decayModule(long ticks) {
		decayTicks = ticks;
		wasDecayed = true;
	}

	public FrameworkModule associatedModule;
	public String moduleUsage;

	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		associatedModule = module;
		this.moduleUsage = moduleUsage;
	}

}
