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
 * Interface for the modules of LIDA.
 * 
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public interface FrameworkModule extends FullyInitializable{

	/**
	 * Gets moduleName.
	 * 
	 * @return {@link ModuleName} of this FrameworkModule
	 */
	public ModuleName getModuleName();
	
	/**
	 * Sets ModuleName.
	 * 
	 * @param moduleName {@link ModuleName} of this FrameworkModule
	 */
	public void setModuleName(ModuleName moduleName);
	
	/**
	 * Gets a subModule.
	 * 
	 * @param name
	 *            of the desired submodule.
	 * @return the submodule.
	 */
	public FrameworkModule getSubmodule(ModuleName name);
	
	/**
	 * Gets a subModule.
	 * 
	 * @param name
	 *            of the desired submodule.
	 * @return the submodule.
	 */
	public FrameworkModule getSubmodule(String name);
	
	/**
	 * Adds submodule as a component of this FrameworkModule.
	 * @param lm submodule to add
	 */
	public void addSubModule(FrameworkModule lm);
	
	/**
	 * Returns module content specified by params.  Should only be used by the GUI.
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
	 * @param listener - listener of this FrameworkModule
	 */
	public void addListener(ModuleListener listener);
	
	/**
	 * Specify the TaskSpawner which this FrameworkModule will use to spawn tasks.
	 * 
	 * @param ts
	 *            - the TaskSpawner
	 */
	public void setAssistingTaskSpawner(TaskSpawner ts);

	/**
	 * Returns the TaskSpawner which this FrameworkModule uses to spawn tasks.
	 * 
	 * @return the assisting task spawner
	 */
	public TaskSpawner getAssistingTaskSpawner();

}
