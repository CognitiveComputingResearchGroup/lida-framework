/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;


public class BasicCandidateThresholdReducer implements CandidateThresholdReducer {
	
    /**
	 * Percent to reduce the behavior activation threshold by if no behavior is selected
	 */
    private final double ACTIVATION_THRESHOLD_REDUCTION  = 0.10;  
    
    private final double ACTIVATION_LOWER_BOUND = 0.25;

	@Override
	public double reduceActivationThreshold(double behaviorActivationThreshold) {
		double result = behaviorActivationThreshold * (1.00 - ACTIVATION_THRESHOLD_REDUCTION);
		if(result < ACTIVATION_LOWER_BOUND)
			return ACTIVATION_LOWER_BOUND;
		else
			return result;
	}

}
