/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public interface ProceduralMemory extends LidaModule, Saveable {

	/**
	 * For adding modules that listen to Procedural Memory
	 * @param listener
	 */
    public abstract void addProceduralMemoryListener(ProceduralMemoryListener listener); 
	
    /**
     * To change how scheme are activated based on the broadcast.
     * @param b
     */
    public void setSchemeActivationBehavior(SchemeActivationBehavior b);
    
	/**
	 * Using the Broadcast content, activate the relevant schemes of procedural memory 
	 */
	public abstract void activateSchemes();
	
	/**
	 * Send out the active schemes to the PM listeners.
	 */
	public abstract void sendInstantiatedScheme(Scheme s);
	
	/**
	 * Add supplied schemes to this procedural memory.
	 * @param schemes
	 */
	public abstract void addSchemes(Collection<Scheme> schemes);
	
	/**
	 * Add supplied scheme to this procedural memory.
	 * @param s
	 */
	public abstract void addScheme(Scheme s);
	
}
