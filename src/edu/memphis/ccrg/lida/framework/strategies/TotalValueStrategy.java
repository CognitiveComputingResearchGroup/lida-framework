/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

/**
 * A strategy that calculates a total value based on a learned component and a current component.
 * @author Ryan J. McCall
 */
public interface TotalValueStrategy extends Strategy{

	/**
	 * Calculates and returns a total value based on specified components.
	 * @param b the learned component
	 * @param c the current component
	 * @return calculated total value
	 */
	public double calculateTotalValue(double b, double c);
	
}
