/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

/**
 * An object which can be configured in particular ways.
 * @author Ryan J. McCall
 *
 */
public interface Initializable {

	/**
	 * This method initializes the module with parameters specified in agent.xml
	 * 
	 * @param parameters Map of parameters indexed by String names
	 */
	public void init(Map<String, ?> parameters);
//	TODO add another map for default parameters. first will be runtime-specified parameters.

	/**
	 * This is a convenience method to initialize this Object. It is called from init(Map<String, Object> parameters).
	 * Subclasses can overwrite this method in order to initialize this Object. But make sure to call super.init();
         * at the beginning of the implementation of this method.
         *
	 */
	public void init();

	/**
	 * A convenience method to read parameters from the Map of properties set 
	 * with the init() method.
	 * @param name the parameter name
	 * @param defaultValue the default value to be returned if the parameter doesn't exist.
	 * @return the value of the parameter or the default value.
	 */
	public Object getParam(String name, Object defaultValue);

}