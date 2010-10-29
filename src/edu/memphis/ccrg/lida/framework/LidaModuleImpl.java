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

/**
 * @author Javier Snaider
 * 
 */
public abstract class LidaModuleImpl implements LidaModule {

	private ModuleName moduleName;
	protected Map<String, ?> lidaProperties;
	private Map<ModuleName, LidaModule> submodules = new ConcurrentHashMap<ModuleName, LidaModule>();

	public LidaModuleImpl(ModuleName moduleName) {
		this.moduleName = moduleName;
	}

	public LidaModuleImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getModuleType()
	 */
	public ModuleName getModuleName() {
		return moduleName;
	}
	
	public abstract Object getModuleContent(Object... params);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	public void init(Map<String, ?> params) {
		this.lidaProperties = params;
	}

	public LidaModule getSubmodule(ModuleName type) {
		return submodules.get(type);
	}

	public void addSubModule(LidaModule lm) {
		submodules.put(lm.getModuleName(), lm);
	}

	protected Map<ModuleName, LidaModule> getSubmodules() {
		return submodules;
	}

	/**
	 * Do not call this method. Decay the module and all the submodules. Subclasses must call this
	 * implementation in order to have all submodules decayed.
	 * 
	 * @param ticks
	 *            number of ticks to decay.
	 */
	public void decayModule(long ticks) {
		for (LidaModule lm : submodules.values()) {
			lm.decayModule(ticks);
		}
	}

	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}

	//TODO make abstract
	public void setAssociatedModule(LidaModule module) {
	}

	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (lidaProperties != null) {
			value = lidaProperties.get(name);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

}