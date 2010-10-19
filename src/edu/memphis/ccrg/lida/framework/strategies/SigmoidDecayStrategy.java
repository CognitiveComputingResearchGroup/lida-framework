/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

/**
 * 
 * @author Ryan J McCall
 *
 */
public class SigmoidDecayStrategy implements DecayStrategy{
	
	private final int DEFAULT_M = 10;
	private final int DEFAULT_B = 0;
	
	private int m = DEFAULT_M;
	private int b = DEFAULT_B;
	
	@Override
	public void init(Map<String, ? extends Object> parameters) {
		m = (Integer) parameters.get("m");
		b = (Integer) parameters.get("b");
	}
	
	@Override
	public double decay(double currentActivation, long ticks, Object... params) {
		double newActivation = currentActivation - 1 / (1 + Math.exp(-ticks*m + b));
		return newActivation;
	}

	@Override
	public double decay(double currentActivation, long ticks,
			Map<String, ? extends Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

}
