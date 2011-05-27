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
 * Default implementation of sigmoid excite.  Uses two parameters in activation calculation.
 * Can pass these parameters when the strategy is initialized. (see factoriesData.xml).
 * Alternatively, parameters can be passed in the excite method call.  
 * Formula used: 1 / (1 + exp(-a* x + c))
 * 
 * @author Javier Snaider & Ryan
 *
 */
public class SigmoidExciteStrategy extends StrategyImpl implements ExciteStrategy {

	private static final double DEFAULT_A = 1.0;
	private double a = DEFAULT_A;
	
	private static final double DEFAULT_C = 0.0;
	private double c = DEFAULT_C;
	
	private static final double epsilon = 1e-10;
	
	@Override
	public void init() {
		a = (Double) getParam("a", DEFAULT_A);
		c = (Double) getParam("c", DEFAULT_C);
	}

	@Override
	public double excite(double curActiv, double excitation,
			Object... params) {
		double aa = a;
		double cc = c;
		if(params.length == 2){
			aa = (Double) params[0];
			cc = (Double) params[1];
		}
		return calcExcitation(curActiv, excitation, aa, cc);
	}
	
	@Override
	public double excite(double currentActivation, double excitation,
			Map<String, ? extends Object> params) {
		double aa = a;
		double cc = c;
		if(params != null && params.containsKey("a") && params.containsKey("c")){
			aa = (Double) params.get("a");
			cc = (Double) params.get("c");
		}
		return calcExcitation(currentActivation, excitation, aa, cc);
	}

	/*
	 * @param curActiv
	 * @param excitation
	 * @param aa 
	 * @param cc 
	 * @return
	 */
	private double calcExcitation(double curActiv, double excitation, double aa, double cc) {
		double curExcitation = -(Math.log((1.0 + epsilon - curActiv)/(curActiv + epsilon)) + cc) / aa + excitation;
		return 1/(1 + Math.exp(-(aa * curExcitation + cc)));
	}

}
