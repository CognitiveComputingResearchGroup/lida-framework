/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.proceduralmemory.ns;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * FrameworkModule containing {@link Scheme}s activated by each conscious
 * broadcast. Activated schemes are instantiated, becoming {@link Behavior}s
 * which are sent to {@link ActionSelection}
 * 
 * @author Ryan J. McCall
 */
public interface ProceduralMemoryNS extends ProceduralMemory, FrameworkModule {

	/**
	 * Gets a new {@link Scheme} having specified {@link Action}.
	 * 
	 * @param a
	 *            an {@link Action}
	 * @return a new {@link Scheme} with {@link Action} a
	 */
	@Override
    public Scheme getNewScheme(Action a);

	/**
	 * Returns whether this procedural memory contains specified scheme.
	 * 
	 * @param s
	 *            a {@link Scheme}
	 * @return true if it contains an equal scheme
	 */
	@Override
    public boolean containsScheme(Scheme s);

	/**
	 * Removes specified {@link Scheme}.
	 * 
	 * @param s
	 *            scheme to be removed.
	 */
	@Override
    public void removeScheme(Scheme s);

	/**
	 * Returns a view of all {@link Scheme} objects currently in the
	 * {@link ProceduralMemoryNS}.
	 * 
	 * @return a {@link Collection} of schemes
	 */
	@Override
    public Collection<Scheme> getSchemes();

	/**
	 * Returns a count of the schemes
	 * 
	 * @return number of schemes currently in this procedural memory.
	 */
	@Override
    public int getSchemeCount();

	/**
	 * Using the broadcast, activate the relevant schemes of
	 * {@link ProceduralMemoryNS}
	 */
	@Override
    public void activateSchemes();

	/**
	 * A call-back method to determine if the {@link Scheme} s should be
	 * instantiated. This method can be overidden by subclasses to provide
	 * custom functionality.
	 * 
	 * @param s
	 *            the {@link Scheme} to be checked
	 * @param broadcastBuffer
	 *            the {@link NodeStructure} in {@link ProceduralMemoryNS}
	 *            containing recent broadcast
	 * @return true if the {@link Scheme} should be instantiated, false
	 *         otherwise.
	 */
	public boolean shouldInstantiate(Scheme s, NodeStructure broadcastBuffer);

	/**
	 * Instantiates specified {@link Scheme} .
	 * 
	 * @param s
	 *            a {@link Scheme} over instantiation threshold
	 * @return a {@link Behavior}, an instantiation of {@link Scheme} s
	 */
	@Override
    public Behavior createInstantiation(Scheme s);
}