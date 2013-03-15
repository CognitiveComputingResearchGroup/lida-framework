/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;

public class MockInitializer implements Initializer {

	public static FullyInitializable module;
	public static Agent agent;
	public static Map<String, ?> params;

	@Override
	public void initModule(FullyInitializable module, Agent agent,
			Map<String, ?> params) {
		MockInitializer.module = module;
		MockInitializer.agent = agent;
		MockInitializer.params = params;
	}

}
