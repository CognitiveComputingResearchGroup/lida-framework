/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

public class MockGlobalWorkspaceImpl extends GlobalWorkspaceImpl implements
		GlobalWorkspace {

	public List<BroadcastTrigger> triggers = new ArrayList<BroadcastTrigger>();

	@Override
	public void addBroadcastListener(BroadcastListener bl) {
	}

	@Override
	public void addBroadcastTrigger(BroadcastTrigger t) {
		triggers.add(t);
	}

	public Coalition coalition;

	@Override
	public boolean addCoalition(Coalition coalition) {
		this.coalition = coalition;
		return true;
	}

	public BroadcastTrigger trigger;

	@Override
	public void triggerBroadcast(BroadcastTrigger trigger) {
		this.trigger = trigger;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void decayModule(long ticks) {
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void init() {

	}

	@Override
	public void setCoalitionDecayStrategy(DecayStrategy coalitionDecayStrategy) {
	}

	@Override
	public DecayStrategy getCoalitionDecayStrategy() {
		return null;
	}

	@Override
	public void setCoalitionRemovalThreshold(double coalitionRemovalThreshold) {
	}

	@Override
	public double getCoalitionRemovalThreshold() {
		return 0;
	}
}