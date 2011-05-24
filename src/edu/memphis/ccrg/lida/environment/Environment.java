/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.environment;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.FrameworkModule;

/**
 * Specification for domains that the framework can use as an environment.
 * 
 * @author Ryan J. McCall
 */
public interface Environment extends FrameworkModule{

	/**
	 * Environment resets its state.
	 */
	public void resetState();
	
	/**
	 * Environment takes the specified action updating its state accordingly.
	 * @param action an algorithm to be processed by the environment
	 */
	public void processAction(Object action);
	
	/**
	 * @param params Map of parameters. 
	 * @return A part of the environment's state, the part that returned depends on the parameters passed in the Map.  
	 */
	public Object getState(Map<String, ?> params);
}
