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
 * Approx implementation of sigmoid excite.  Uses one parameters in activation calculation.
 * Can pass these parameters when the strategy is initialized. (see factoriesData.xml).
 * Alternatively, parameters can be passed in the excite method call.  
 * Formula used: Divides the curve into 3 parts: quadratic, linear, quadratic. 
 * 
 * @author Javier Snaider & Ryan & Daqi
 *
 */
public class ApproxSigmoidExciteStrategy extends StrategyImpl implements ExciteStrategy {

	private static final double DEFAULT_A = 1.0;
	private double a = DEFAULT_A;
	
	@Override
	public void init() {
		a = (Double) getParam("a", DEFAULT_A);
	}

	//TODO test boundary conditions 1.
	@Override
	public double excite(double curActiv, double excitation,
			Object... params) {
		double aa = a;

		if (params.length != 0) {
			aa = (Double) params[0];
		}
		return calcExcitation(curActiv, excitation, aa);
	}
	
	@Override
	public double excite(double currentActivation, double excitation,
			Map<String, ? extends Object> params) {
		double aa = a;

		if(params != null){
			aa = (Double) params.get("a");
		}
		return calcExcitation(currentActivation, excitation, aa);
	}

	/*
	 * @param curActiv
	 * @param excitation
	 * @param aa 
	 * @return
	 */
	
	/**
	 * This is a direct function to calculate new activation.
	 * You could choose it if you care about time very much.
	 * 
	 * @param curActiv current activation 
	 * @param excitation amount of new activation
	 * @param aa parameter
	 * @return new activation
	 */
	public double calcExcitation(double curActiv, double excitation, double aa) {

		double curExcitation = 0;
		double newExcitation = 0;
		double newActiv = 0;
		
		double a1 = 0.0144, b1 = 0.1458, c1 = 0.3759, k = 0.21429, 
		       a2 = -0.0144, b2 = 0.1458, c2 = 0.6241;
		
		//Step 1-3: curActiv -> curExcitation
		if (curActiv <= 0.007)
			curExcitation = -5;
		else if (curActiv < 0.2)
			curExcitation = ((0 - b1) + Math.sqrt(b1*b1 - 4*a1*(c1 - curActiv)))/(2*a1);
		else if (curActiv <= 0.8)
			curExcitation = (curActiv - 0.5)/k;
		else if (curActiv < 0.993)
			curExcitation = ((0 - b2) + Math.sqrt(b2*b2 - 4*a2*(c2 - curActiv)))/(2*a2);
		else 
			curExcitation = 5;
		
		//Step 2-3: curExcitation -> newExcitation
		newExcitation = curExcitation + aa*excitation;
		
		//Step 3-3: newExcitation -> newActiv
		if (newExcitation <= -5)
			newActiv = 0.007;
		else if (newExcitation < -1.4)
			newActiv = a1*newExcitation*newExcitation + b1*newExcitation + c1;
		else if (newExcitation <= 1.4)
			newActiv = k*newExcitation + 0.5;
		else if (newExcitation < 5)
			newActiv = a2*newExcitation*newExcitation + b2*newExcitation + c2;
		else
			newActiv = 0.993;
		
		return newActiv;	
	}

}
