/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.strategies;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Javier Snaider
 *
 */
public interface Strategy extends Serializable {
	/**
	 * Receives a Map of init parameters for initalization.
	 * 
	 * 
	 * @param parameters the Map with the parameters.
	 */
	public void init(Map<String, ? extends Object> parameters);

}
