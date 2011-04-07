/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

/**
 * A strategy to reduce the threshold for action selection.
 * @author Ryan J. McCall
 *
 */
public interface CandidateThresholdReducer {

	/**
	 * Reduces specified threshold
	 * @param currentThreshold current threshold
	 * @return new, reduced threshold
	 */
	public double reduceActivationThreshold(double currentThreshold);

}
