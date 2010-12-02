/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;


public class LinearDecayStrategy extends StrategyImpl implements DecayStrategy{

	    
	    /** The default slope (m = 1). */
	    public static final double DEFAULT_M = 0.01;
	    
	    /** The default intercept (b = 0). */
	    public static final double DEFAULT_B = 0.0;
	    
	    /** The slope of this linear curve. */
	    private double m;
	    
	    /** The y intercept of this linear curve. */
	    private double b;

	    
	    /** Creates a new instance of LinearCurve. Values for slope and intercept
	     * are set to the default ones.
	     */
	    public LinearDecayStrategy() {
	        m = DEFAULT_M;
	        b = DEFAULT_B;
	    }
	    
	    /**
	     * Creates a new instance of LinearCurve, with specific values for the slope
	     * and the intercept.
	     * @param m     The value of the slope.
	     * @param b     The value of the intercept.
	     */
	    public LinearDecayStrategy(double m, double b) {
	        this.m = m;
	        this.b = b;
	    }
	    
		@Override
		public void init() {
			m = (Double) getParam("m",DEFAULT_M);
			b = (Double) getParam("b",DEFAULT_B);
		}

		@Override
		public double decay(double currentActivation, long ticks,
				Object... params) {
			currentActivation= currentActivation - (m*ticks+b);
	    	return (currentActivation>0.0)?currentActivation:0.0;
		}

		@Override
		public double decay(double currentActivation, long ticks,
				Map<String, ? extends Object> params) {
			currentActivation= currentActivation - (m*ticks+b);
	    	return (currentActivation>0.0)?currentActivation:0.0;
		}
}