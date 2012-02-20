/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.initialization.AgentXmlFactory;
import edu.memphis.ccrg.lida.framework.initialization.InitializableImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Abstract implementation of {@link FrameworkModule}
 * Implementations should add themselves to the agent.xml configuration file 
 * 
 * @author Javier Snaider
 */
public abstract class FrameworkModuleImpl extends InitializableImpl implements FrameworkModule {

	private static final Logger logger = Logger.getLogger(FrameworkModuleImpl.class.getCanonicalName());
	private ModuleName moduleName;
	private Map<ModuleName, FrameworkModule> submodules = new ConcurrentHashMap<ModuleName, FrameworkModule>();
	
	/**
	 * {@link TaskSpawner} used by this module
	 */
	protected TaskSpawner taskSpawner;

	/**
	 * Default constructor
	 */
	public FrameworkModuleImpl() {
		moduleName = ModuleName.UnnamedModule;
	}
	
	/**
	 * Creates a FrameworkModule with specified module name.  
	 * It is generally preferable to use the 
	 * default constructor instead of this one 
	 * since the ModuleName is typically specified by agent.xml and set by {@link AgentXmlFactory}
	 * 
	 * @see AgentXmlFactory
	 * @param moduleName {@link ModuleName} of this {@link FrameworkModule}
	 */
	FrameworkModuleImpl(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	
	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts){
		taskSpawner = ts;
	}

	@Override
	public TaskSpawner getAssistingTaskSpawner(){
		return taskSpawner;
	}
	
	@Override
	public boolean containsSubmodule(ModuleName name){
		return submodules.containsKey(name);
	}
	
	@Override
	public boolean containsSubmodule(String name){
		return submodules.containsKey(ModuleName.getModuleName(name));
	}

	@Override
	public FrameworkModule getSubmodule(ModuleName name) {
		if(name == null){
			return null;
		}
		return submodules.get(name);
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

	@Override
	public Map<ModuleName, FrameworkModule> getSubmodules() {
		return Collections.unmodifiableMap(submodules);
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
	public String toString(){
		return moduleName.name;
	}
	
	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
	}
	
	/**
	 * Override this method to add a listener to the module
	 * @param listener - listener of this FrameworkModule
	 */
	@Override
	public void addListener(ModuleListener listener){
	}

	/** 
	 * Intended to be called from the GUI.
	 * Override this method to return particular module content based on params. 
	 */
	@Override
	public Object getModuleContent(Object... params){
		return null;
	}

}