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
 * WARNING:	Renaming this class requires renaming values in
 * 	  configs/factoriesData.xml
 */
public class DefaultExciteStrategy extends StrategyImpl implements ExciteStrategy {
	
	/* 
	 * The default slope
	 * 
	 */
	public static final double DEFAULT_M = 0.01;

	/*
	 * The slope of this linear curve.
	 */
	private double m;

	/**
	 * Creates a new instance of LinearCurve. Values for slope and intercept are
	 * set to the default ones.
	 */
	public DefaultExciteStrategy() {
		m = DEFAULT_M;
	}
	
	@Override
	public void init() {
		m = (Double) getParam("m", DEFAULT_M);
	}
	
	@Override
	public double excite(double currentActivation, double excitation, Object... params) {
		double mm = m;
		if (params.length != 0) {
			mm = (Double) params[0];
		}
		
		currentActivation += mm * excitation;
		if(currentActivation >= 1.0)
			return 1.0;
		if(currentActivation <= 0.0)
			return 0.0;
		return currentActivation;
	}

	@Override
	public double excite(double currentActivation, double excitation, Map<String, ?> params) {
		return excite(currentActivation, excitation, params.values().toArray());
	} 

}
