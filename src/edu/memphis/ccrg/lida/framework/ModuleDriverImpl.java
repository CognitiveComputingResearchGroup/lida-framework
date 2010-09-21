/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {
	protected static final int DEFAULT_TICKS_PER_CYCLE = 10;
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private ModuleName moduleName;
	protected Map<String,?> lidaProperties;

	public ModuleDriverImpl(int ticksPerCycle, LidaTaskManager tm,ModuleName moduleName){
		super(ticksPerCycle, tm);
		this.moduleName= moduleName;
	}
	public ModuleDriverImpl(int ticksPerCycle,ModuleName moduleName){
		super(ticksPerCycle);
		this.moduleName= moduleName;
	}
	public ModuleDriverImpl(){
		super();
	}

	protected void runThisLidaTask(){
		runThisDriver();
	}
	protected abstract void runThisDriver();
	/**
	 * Add a gui panel
	 */
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}
	
	/**
	 * Send a gui event
	 */
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveFrameworkGuiEvent(evt);
	}// method	
	
	@Override
	public ModuleName getModuleName(){
		return moduleName;
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.lidaProperties = params;
	}

	public void setAssociatedModule(LidaModule module) {
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (lidaProperties != null) {
			value = lidaProperties.get(name);
			if (value == null) {
				value = defaultValue;
			}
		}
		return value;
	}
	
}// class