/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Implementations should add themselves to the lida.xml configuration file 
 * 
 * @author Javier Snaider
 */
public abstract class LidaModuleImpl implements LidaModule {

	private ModuleName moduleName;
	protected Map<String, ?> parameters;
	private Map<ModuleName, LidaModule> submodules = new ConcurrentHashMap<ModuleName, LidaModule>();
	protected TaskSpawner taskSpawner;

	public LidaModuleImpl() {
	}
	
	public LidaModuleImpl(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	
	/*
	 * @see LidaModule#getModuleContent(Object...)
	 */
	@Override
	public abstract Object getModuleContent(Object... params);
	
	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts){
		taskSpawner = ts;
	}

	@Override
	public TaskSpawner getAssistingTaskSpawner(){
		return taskSpawner;
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
	public abstract void init();
	
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

	@Override
	public LidaModule getSubmodule(ModuleName type) {
		return submodules.get(type);
	}

	@Override
	public void addSubModule(LidaModule lm) {
		submodules.put(lm.getModuleName(), lm);
	}

	protected Map<ModuleName, LidaModule> getSubmodules() {
		return submodules;
	}

	/**
	 * Framework users should not call this method. It will be called by the LidaTaskManager.
	 * Decays this module and all its submodules. Subclasses overriding this method must
	 * call this method first in order to have all submodules decayed.
	 * 
	 * @param ticks number of ticks to decay.
	 */
	@Override
	public void decayModule(long ticks) {
		for (LidaModule lm : submodules.values()) {
			lm.decayModule(ticks);
		}
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}

	@Override
	public ModuleName getModuleName() {
		return moduleName;
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
	}

}