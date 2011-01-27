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
 * A behavior that calculates the amount of activation to propagate.
 * 
 * @author Ryan J. McCall
 */
public interface PropagationBehavior {
	
	/**
	 * Depending on the behavior different parameters can be passed to this method for the 
	 * calculation of the amount to propagate.
	 * @param params parameters
	 */
	public double getActivationToPropagate(Map<String, Object> params);
}
