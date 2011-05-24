/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

/**
 * Basic {@link DecayStrategy} governed by a linear curve.
 * WARNING: Renaming this class requires renaming values in
 * configs/factoriesData.xml
 */
public class LinearDecayStrategy extends StrategyImpl implements DecayStrategy {

	/* 
	 * The default slope
	 * 
	 */
	public static final double DEFAULT_M = 0.1;

	/*
	 * The slope of this linear curve.
	 */
	private double m;

	/**
	 * Creates a new instance of LinearCurve. Values for slope and intercept are
	 * set to the default ones.
	 */
	public LinearDecayStrategy() {
		m = DEFAULT_M;
	}

	/**
	 * Creates a new instance of LinearCurve, with specific values for the slope
	 * and the intercept.
	 * 
	 * @param m
	 *            The value of the slope.
	 */
	public LinearDecayStrategy(double m) {
		this.m = m;
	}

	@Override
	public void init() {
		m = (Double) getParam("m", DEFAULT_M);
	}

	@Override
	public double decay(double currentActivation, long ticks, Object... params) {
		double mm = m;
		if (params.length != 0) {
			mm = (Double) params[0];
		}
		currentActivation -= (mm * ticks);
		return (currentActivation > 0.0) ? currentActivation : 0.0;
	}

	@Override
	public double decay(double currentActivation, long ticks,
			Map<String, ? extends Object> params) {
		double mm = m;
		if (params != null) {
			mm = (Double) params.get("m");
		}
		currentActivation -= (mm * ticks);
		return (currentActivation > 0.0) ? currentActivation : 0.0;
	}
}