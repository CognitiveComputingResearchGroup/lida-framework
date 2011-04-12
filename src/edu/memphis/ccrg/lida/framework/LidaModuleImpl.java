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
 * Abstract implementation of {@link LidaModule}
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
	
	/*
	 * Creates a LidaModule with specified module name.  
	 * It is generally preferable to use the 
	 * default constructor instead of this one 
	 * since the ModuleName is typically specified by lida.xml and set by {@link LidaXmlFactory}
	 * 
	 * @see {@link LidaXmlFactory}
	 * @param moduleName {@link ModuleName} of this {@link LidaModule}
	 */
	LidaModuleImpl(ModuleName moduleName) {
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
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializable#init(java.util.Map)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.parameters = params;
		init();
	}
	

	/** 
	 * Implementations should only perform initialization of variables or
	 * objects that depend on parameters in the parameter map specified in {@link #init(Map)}.
	 * Furthermore they should not try to access modules added via {@link #setAssociatedModule(LidaModule, String)}
	 * as this method will not have run yet at the time this method is called.
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
	public LidaModule getSubmodule(String name){
		return getSubmodule(ModuleName.getModuleName(name));
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
	public void setAssociatedModule(LidaModule module, String moduleUsage) {
	}

}