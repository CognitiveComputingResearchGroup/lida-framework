/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.Learnable;
import edu.memphis.ccrg.lida.framework.shared.Linkable;

public interface PamLinkable extends Linkable, Learnable{
	
	/**
	 * Determines if this linkable is relevant. A linkable is relevant if its total
	 * activation is greater or equal to the selection threshold.
	 * 
	 * @return <code>true</code> if this linkable is relevant
	 */
	public boolean isOverThreshold();

	/**
	 * returns selection threshold
	 * 
	 * @return Selection threshold
	 */
	public double getPerceptThreshold();
	
	/**
	 * Set the threshold
	 * @param threshold threshold
	 */
	public void setPerceptThreshold(double threshold);

	/**
	 * Returns final min activation variable
	 */
	public double getMinActivation();

	/**
	 * Returns final max activation variable
	 */
	public double getMaxActivation();

}
