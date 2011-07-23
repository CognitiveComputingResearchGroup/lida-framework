/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulation of the name of a {@link FrameworkModule}.  Provides several public-static instances by default. 
 * @author Javier Snaider, Ryan J. McCall
 *
 */
@SuppressWarnings(value = {"all"})
public class ModuleName {

	/**
	 * String representation of {@link ModuleName}
	 */
	public final String name;
	private static Map<String, ModuleName> moduleNames = new HashMap<String, ModuleName>();

	/**
	 * Returns ModuleName of specified name. 
	 * @param name String
	 * @return ModuleName
	 */
	public static ModuleName getModuleName(String name) {
		return moduleNames.get(name);
	}

	/**
	 * Creates and adds a new module name if name is not already defined.
	 * Returns new ModuleName or existing {@link ModuleName} associated with the name.
	 * @param name String
	 * @return ModuleName
	 */
	public static ModuleName addModuleName(String name) {
		if (!moduleNames.containsKey(name)) {
			new ModuleName(name);
		}
		return moduleNames.get(name);
	}

	private ModuleName(String name) {
		this.name = name;
		moduleNames.put(name, this);
	}
	
	@Override
	public String toString(){
		return name;
	}

	/**
	 * Returns a {@link Collection} of all {@link ModuleName}s
	 * @return all module names
	 */
	public static Collection<ModuleName> values() {
		return Collections.unmodifiableCollection(moduleNames.values());
	}

	public final static ModuleName Environment = new ModuleName("Environment");
	public final static ModuleName SensoryMemory = new ModuleName(
			"SensoryMemory");
	public final static ModuleName PerceptualAssociativeMemory = new ModuleName(
			"PerceptualAssociativeMemory");

	public final static ModuleName TransientEpisodicMemory = new ModuleName(
			"TransientEpisodicMemory");
	public final static ModuleName DeclarativeMemory = new ModuleName(
			"DeclarativeMemory");

	public final static ModuleName Workspace = new ModuleName("Workspace");
	public final static ModuleName PerceptualBuffer = new ModuleName(
			"PerceptualBuffer");
	public final static ModuleName EpisodicBuffer = new ModuleName(
			"EpisodicBuffer");
	public final static ModuleName BroadcastQueue = new ModuleName(
			"BroadcastQueue");

	public final static ModuleName CurrentSituationalModel = new ModuleName(
			"CurrentSituationalModel");

	public final static ModuleName AttentionModule = new ModuleName(
			"AttentionModule");
	public final static ModuleName StructureBuildingCodeletModule = new ModuleName(
			"StructureBuildingCodeletModule");
	public final static ModuleName GlobalWorkspace = new ModuleName(
			"GlobalWorkspace");

	public final static ModuleName ProceduralMemory = new ModuleName(
			"ProceduralMemory");
	public final static ModuleName ActionSelection = new ModuleName(
			"ActionSelection");
	public final static ModuleName SensoryMotorMemory = new ModuleName(
			"SensoryMotorMemory");

	public final static ModuleName NoModule = new ModuleName("NoModule");
	public final static ModuleName Agent = new ModuleName("Agent");
}
