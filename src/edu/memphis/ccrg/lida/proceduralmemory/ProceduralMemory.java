/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * FrameworkModule containing {@link Scheme}s activated by each conscious broadcast.  
 * Activated schemes are instantiated, becoming {@link Behavior}s which are sent to 
 * {@link ActionSelection}
 * 
 * @author Ryan J. McCall
 */
public interface ProceduralMemory extends FrameworkModule{
	
	/**
	 * Adds specified scheme to this {@link ProceduralMemory}.
	 * @param s Scheme
	 */
	public void addScheme(Scheme s);
		
	/**
	 * Adds specified schemes to this procedural memory.
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
	 * Removes specified {@link Scheme}.
	 * @param s scheme to be removed.
	 */
	public void removeScheme(Scheme s);
	
	/**
	 * Returns a view of all {@link Scheme} objects currently in the {@link ProceduralMemory}.
	 * @return a {@link Collection} of schemes
	 */
	public Collection<Scheme> getSchemes();
	
	/**
	 * Returns a count of the schemes
	 * @return number of schemes currently in this procedural memory.
	 */
	public int getSchemeCount();
    
	/**
	 * Using the Broadcast, activate the relevant schemes of procedural memory 
	 */
	public void activateSchemes();
	
	/**
	 * A call-back method to determine if the Scheme s should be instantiated.
	 * This method can be overidden by the user to provide custom functionality.
	 * @param s the {@link Scheme} to be checked
	 * @param broadcastBuffer the buffer in {@link ProceduralMemory} containing recent broadcast Nodes 
	 * @return true if the {@link Scheme} should be instantiated, false otherwise.
	 */
	public boolean shouldInstantiate(Scheme s, NodeStructure broadcastBuffer);
	
	/**
	 * Instantiates specified scheme.
	 * @param s a scheme over threshold
	 */
	public void createInstantiation(Scheme s);

	/**
	 * Adds condition to the condition pool if one with the same id is not already present.
	 *
	 * @param c {@link Condition} of a {@link Scheme} in this {@link ProceduralMemory}
	 * @return the existing condition if one already exists or the newly added condition.
	 */
	public Condition addCondition(Condition c);

	/**
	 * Gets condition
	 * @param conditionId {@link Condition}'s id 
	 * @return {@link Condition} of a {@link Scheme} in this {@link ProceduralMemory}
	 */
	public Condition getCondition(Object conditionId);
}