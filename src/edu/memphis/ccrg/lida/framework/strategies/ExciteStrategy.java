/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

public interface ExciteStrategy  extends Strategy{

	/**
	 * 
	 * @param currentActivation
	 * @param excitation
	 * @param params
	 * @return
	 */
	public abstract double excite(double currentActivation, double excitation, Object... params);
	
	/**
	 * 
	 * @param currentActivation
	 * @param excitation
	 * @param params
	 * @return
	 */
	public abstract double excite(double currentActivation, double excitation, Map<String, ? extends Object>params);

}

