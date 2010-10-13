/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

public class DefaultExciteStrategy implements ExciteStrategy {
	
	@SuppressWarnings("unused")
	private Map<String, ? extends Object> params;

	public void init(Map<String, ? extends Object> params) {
		this.params=params;
	}
	@Override
	public void setSlope(int m) {

	}

	@Override
	public void setIntercept(int b) {

	}

	@Override
	public double excite(double currentActivation, double excitation,
			Object... params) {
		double newActivation = currentActivation + excitation;
		if(newActivation > 1.0)
			return 1.0;
		if(newActivation < 0.0)
			return 0.0;
		return newActivation;
	}

	@Override
	public double excite(double currentActivation, double excitation, Map<String, Object> params) {
		return this.excite(currentActivation, excitation, params.values().toArray());
	} 

}
