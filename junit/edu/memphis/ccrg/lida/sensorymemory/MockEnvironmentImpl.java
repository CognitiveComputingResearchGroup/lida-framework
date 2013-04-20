/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymemory;

import java.util.Map;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;

public class MockEnvironmentImpl extends EnvironmentImpl {

	@Override
	public void resetState() {

	}

	@Override
	public void processAction(Object action) {

	}

	@Override
	public Object getState(Map<String, ?> params) {

		return null;
	}

	@Override
	public Object getModuleContent(Object... params) {

		return null;
	}

	@Override
	public void init() {

	}

}
