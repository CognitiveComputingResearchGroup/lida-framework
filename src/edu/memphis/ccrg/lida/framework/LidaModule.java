/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * General Module Interface in LIDA.
 * 
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public interface LidaModule extends FullyInitializable{
	
	/**
	 * Gets module name.
	 * 
	 * @return ModuleName
	 */
	public ModuleName getModuleName();
	
	/**
	 * param ModuleName.
	 * 
	 * @param moduleName
	 *            the new module name
	 */
	public void setModuleName(ModuleName moduleName);
	
	/**
	 * Gets submodule.
	 * 
	 * @param name
	 *            of the desired submodule.
	 * @return the submodule.
	 */
	public LidaModule getSubmodule(ModuleName name);
	
	/**
	 * Adds submodule as a component of this LidaModule.
	 * @param lm submodule to add
	 */
	public void addSubModule(LidaModule lm);
	
	/**
	 * Returns module content specified by params.  Should only be used by the GUI.
	 * TODO being used in ways other than for the gui!
	 * 
	 * @param params
	 *            - specifies what content will be returned.
	 * @return various kinds of content of this module.
	 */
	public Object getModuleContent(Object... params);

	/**
	 * Decay this module and all its submodules. 
	 * @param ticks number of ticks to decay.
	 */
	public void decayModule(long ticks);

	/**
	 * Generic way to add various kinds of listeners.  
	 * @param listener - listener of this LidaModule
	 */
	public void addListener(ModuleListener listener);
	
	/**
	 * Specify the TaskSpawner which this LidaModule will use to spawn tasks.
	 * 
	 * @param ts
	 *            - the TaskSpawner
	 */
	public void setAssistingTaskSpawner(TaskSpawner ts);

	/**
	 * Returns the TaskSpawner which this LidaModule uses to spawn tasks.
	 * 
	 * @return the assisting task spawner
	 */
	public TaskSpawner getAssistingTaskSpawner();

}
