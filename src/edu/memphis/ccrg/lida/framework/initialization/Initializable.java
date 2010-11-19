/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * An Initializable module will be initialized automatically by the LidaXmlFactory
 * 
 * @see LidaXmlFactory
 * @author ryanjmccall, Javier Snaider
 *
 */
public interface Initializable {
	/**
	 * This method initializes the module using properties from LIDA Properties File
	 * @param lidaProperties LIDA properties
	 */
	public void init (Map<String, ?> lidaProperties);
	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the LidaTask
	 */
	public void init();
	/**
	 * A convenience method to read parameters from the Map of properties set 
	 * with the init() method.
	 * @param name the parameter name
	 * @param defaultValue the default value to be returned if the parameter doesn't exist.
	 * @return the value of the parameter or the default value.
	 */
	public Object getParam(String name,Object defaultValue);
	/**
	 * Sets an associated LidaModule.
	 * @param module the module to be associated.
	 */
	public void setAssociatedModule(LidaModule module);
}
