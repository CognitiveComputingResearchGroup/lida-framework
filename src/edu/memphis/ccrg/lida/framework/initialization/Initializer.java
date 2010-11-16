/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * An initializer performs configuration and set up on a particular module.  
 * @author Ryan McCall
 *
 */
public interface Initializer {
	
	/**
	 * Initializer is passed a particular module to configure.
	 * It can use the Lida object and a map of parameters to do so.  
	 * The variables in params are exactly those specified in Lida.xml under the module.
	 * 
	 * @param module module being initialized
	 * @param lida Lida object
	 * @param params parameters
	 */
	public void initModule(Initializable module, Lida lida, Map<String, ?> params); 

}
