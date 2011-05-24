/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Agent;

/**
 * An initializer configures the parameters and objects of a particular module.  
 * @author Ryan J. McCall
 *
 */
public interface Initializer {
	
	/**
	 * Receives a particular module to configure. The {@link Agent} object and a map of parameters can be used.  
	 * The variables in params are those specified in agent.xml for the module being initialized.
	 * 
	 * @param module module being initialized
	 * @param agent {@link Agent} object
	 * @param params module parameters
	 */
	public void initModule(FullyInitializable module, Agent agent, Map<String, ?> params); 

}
