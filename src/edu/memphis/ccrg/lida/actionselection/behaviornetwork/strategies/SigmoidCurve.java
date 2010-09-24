/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * SigmoidCurve.java
 *
 * Sidney D'Mello
 * Created on June 9, 2004, 7:53 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;


public class SigmoidCurve implements ExciteStrategy, DecayStrategy{

	@Override
	public void init(Map<String, ? extends Object> parameters) {
	}

	@Override
	public double decay(double currentActivation, long ticks) {
		double newActivation = currentActivation - 1 / (1 + Math.exp(-ticks));
		return newActivation;
	}

	@Override
	public double excite(double currentActivation, double excitation) {
		double newActivation = currentActivation + 1 / (1 + Math.exp(-excitation));
		return newActivation;
	}    
}
