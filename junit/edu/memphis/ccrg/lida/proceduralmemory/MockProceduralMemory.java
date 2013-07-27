/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemory;

public class MockProceduralMemory extends FrameworkModuleImpl implements
		ProceduralMemory {

	@Override
	public boolean containsScheme(Scheme s) {
		// not implemented
		return false;
	}

	@Override
	public int getSchemeCount() {
		// not implemented
		return 0;
	}

	public Collection<Scheme> getTestInstantiated() {
		return instantiated;
	}

	public void clearInstantiated() {
		instantiated.clear();
	}

	private Collection<Scheme> instantiated = new ArrayList<Scheme>();

	@Override
	public Behavior createInstantiation(Scheme s) {
		instantiated.add(s);
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		// not implemented

	}

	@Override
	public void decayModule(long ticks) {
		// not implemented

	}

	@Override
	public Object getModuleContent(Object... params) {
		// not implemented
		return null;
	}

	@Override
	public void init() {
		// not implemented

	}

	@Override
	public void removeScheme(Scheme s) {
	}

	@Override
	public void activateSchemes() {
	}

	@Override
	public boolean shouldInstantiate(Scheme s, NodeStructure broadcastBuffer) {
		return false;
	}

	@Override
	public Collection<Scheme> getSchemes() {
		return null;
	}

	@Override
	public Scheme getNewScheme(Action a) {
		return null;
	}
}
