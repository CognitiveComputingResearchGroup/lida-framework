/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.strategies;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.StrategyImpl;


/**
 * @author Ryan J. McCall
 */
public class BasicCandidateThresholdReducer extends StrategyImpl implements DecayStrategy {
	
    /*
	 * Percent to reduce the behavior activation threshold by if no behavior is selected
	 */
    private static final double ACTIVATION_THRESHOLD_REDUCTION  = 0.10;  
    
    private static final double ACTIVATION_LOWER_BOUND = 0.0;
    
    //FIXME init for parameters

	@Override
	public double decay(double currentActivation, long ticks, Object... params) {
		//FIXME use params
		return reduceActivationThreshold(currentActivation);
	}

	@Override
	public double decay(double currentActivation, long ticks,
			Map<String, ? extends Object> params) {
		//FIXME use params
		return reduceActivationThreshold(currentActivation);
	}

	private double reduceActivationThreshold(double behaviorActivationThreshold) {
		double result = behaviorActivationThreshold  - ACTIVATION_THRESHOLD_REDUCTION;
		if(result < ACTIVATION_LOWER_BOUND)
			return ACTIVATION_LOWER_BOUND;
		else
			return result;
	}


}
