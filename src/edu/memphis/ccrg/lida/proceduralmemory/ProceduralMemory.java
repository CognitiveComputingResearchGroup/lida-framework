/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * FrameworkModule containing {@link Scheme}s activated by each conscious broadcast.  
 * Activated schemes are instantiated, becoming {@link Behavior}s which are sent to 
 * {@link ActionSelection}
 * @author Ryan J. McCall
 *
 */
public interface ProceduralMemory extends FrameworkModule, Saveable{
	
	/**
	 * Add supplied scheme to this procedural memory.
	 * @param s Scheme
	 */
	public void addScheme(Scheme s);
		
	/**
	 * Add supplied schemes to this procedural memory.
	 * @param schemes set of Schemes
	 */
	public void addSchemes(Collection<Scheme> schemes);
	
	/**
	 * Returns whether this procedural memory contains specified scheme.
	 * @param s a {@link Scheme}
 	 * @return true if it contains an equal scheme
	 */
	public boolean containsScheme(Scheme s);
	
	/**
	 * Returns scheme Count
	 * @return number of schemes currently in the memory.
	 */
	public int getSchemeCount();
	
    /**
     * To change how scheme are activated based on the broadcast.
     * @param b behavior
     */
    public void setSchemeActivationBehavior(SchemeActivationBehavior b);
    
    /**
     * Returns SchemeActivationBehavior.
     * @return Behavior used to Activate schemes
     */
    public SchemeActivationBehavior getSchemeActivationBehavior();
    
	/**
	 * Using the Broadcast content, activate the relevant schemes of procedural memory 
	 * @param broadcastContent conscious contents
	 */
	public void activateSchemes(NodeStructure broadcastContent);
	
	/**
	 * Instantiate scheme and send it to listeners.
	 * @param s a scheme over threshold
	 */
	public void sendInstantiatedScheme(Scheme s);
		
}
