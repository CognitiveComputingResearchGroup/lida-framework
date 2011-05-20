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
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Abstract implementation of {@link FrameworkModule}
 * Implementations should add themselves to the agent.xml configuration file 
 * 
 * @author Javier Snaider
 */
public abstract class FrameworkModuleImpl implements FrameworkModule {

	private static final Logger logger = Logger.getLogger(FrameworkModuleImpl.class.getCanonicalName());
	private ModuleName moduleName;
	private Map<ModuleName, FrameworkModule> submodules = new ConcurrentHashMap<ModuleName, FrameworkModule>();
	protected TaskSpawner taskSpawner;
	protected Map<String, ?> parameters;

	public FrameworkModuleImpl() {
	}
	
	/*
	 * Creates a FrameworkModule with specified module name.  
	 * It is generally preferable to use the 
	 * default constructor instead of this one 
	 * since the ModuleName is typically specified by agent.xml and set by {@link AgentXmlFactory}
	 * 
	 * @see {@link AgentXmlFactory}
	 * @param moduleName {@link ModuleName} of this {@link FrameworkModule}
	 */
	FrameworkModuleImpl(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	
	/*
	 * @see FrameworkModule#getModuleContent(Object...)
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
	 * Furthermore they should not try to access modules added via {@link #setAssociatedModule(FrameworkModule, String)}
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
	public FrameworkModule getSubmodule(ModuleName type) {
		if(type == null){
			return null;
		}
		return submodules.get(type);
	}
	@Override
	public FrameworkModule getSubmodule(String name){
		if(name == null){
			return null;
		}
		return getSubmodule(ModuleName.getModuleName(name));
	}

	@Override
	public void addSubModule(FrameworkModule lm) {
		if(lm != null && lm.getModuleName() != null){
			submodules.put(lm.getModuleName(), lm);
		}else{
			logger.log(Level.WARNING, "Either module or module's name was null", TaskManager.getCurrentTick());
		}
	}

	protected Map<ModuleName, FrameworkModule> getSubmodules() {
		return submodules;
	}

	/**
	 * Framework users should not call this method. It will be called by the TaskManager.
	 * Decays this module and all its submodules. Subclasses overriding this method must
	 * call this method first in order to have all submodules decayed.
	 * 
	 * @param ticks number of ticks to decay.
	 */
	@Override
	public void taskManagerDecayModule(long ticks) {
		decayModule(ticks);
		for (FrameworkModule lm : submodules.values()) {
			lm.taskManagerDecayModule(ticks);
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
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
	}

}