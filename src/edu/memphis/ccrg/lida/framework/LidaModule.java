/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.framework.initialization.Initializable;

/**
 * Generic Module Interface in LIDA.
 * @author Javier Snaider
 *
 */
public interface LidaModule extends Initializable{

	
	/**
	 * @return ModuleName
	 */
	public abstract ModuleName getModuleName();
	
	/**
	 * param ModuleName
	 */
	public abstract void setModuleName(ModuleName moduleName);
	
	/**
	 * @param name of the desired submodule.
	 * @return the submodule.
	 */
	public abstract LidaModule getSubmodule(ModuleName name);
	
	/**
	 * @return the generic content of this module.
	 */
	//TODO:public abstract Object getModuleContent(Object... params);
	public abstract Object getModuleContent();

	/**
	 * Decay the module and all the submodules. 
	 * @param ticks number of ticks to decay.
	 */
	public abstract void decayModule(long ticks);

	public abstract void addSubModule(LidaModule lm);

	public abstract void addListener(ModuleListener listener);

}
