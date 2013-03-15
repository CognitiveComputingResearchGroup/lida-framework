/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

public class MockBroadcastTrigger implements BroadcastTrigger {

	public Collection<Coalition> coalitions;

	@Override
	public void checkForTriggerCondition(Collection<Coalition> coalitions) {
		this.coalitions = coalitions;
	}

	@Override
	public void init(Map<String, Object> parameters, GlobalWorkspace gw) {

	}

	public boolean wasReset;

	@Override
	public void reset() {
		wasReset = true;

	}

	@Override
	public void start() {

	}

}
