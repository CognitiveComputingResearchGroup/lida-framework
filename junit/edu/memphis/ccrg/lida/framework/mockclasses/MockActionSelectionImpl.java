/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Collection;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;

public class MockActionSelectionImpl extends FrameworkModuleImpl implements
		ActionSelection {

	private static final Logger logger = Logger
			.getLogger(MockActionSelectionImpl.class.getCanonicalName());

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
	}

	@Override
	public void init() {
	}

	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {

	}

	@Override
	public void decayModule(long ticks) {
		// not implemented

	}

	@Override
	public Object getModuleContent(Object... params) {

		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {

	}

	@Override
	public Behavior selectBehavior(Collection<Behavior> behaviors,
			double candidateThreshold) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Behavior> getBehaviors() {
		// TODO Auto-generated method stub
		return null;
	}

}
