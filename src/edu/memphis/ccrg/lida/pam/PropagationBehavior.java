/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.Map;

/**
 * A behavior that calculates a new activation to pass.
 * @author Ryan J. McCall
 *
 */
public interface PropagationBehavior {
	/**
	 * Depending on the behavior different parameters can be pass in params for the 
	 * calculation.
	 * @param params parameters
	 */
	public double getActivationToPropagate(Map<String, Object> params);
}
