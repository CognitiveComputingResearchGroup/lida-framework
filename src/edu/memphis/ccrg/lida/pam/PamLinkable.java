/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.Linkable;

public interface PamLinkable extends Linkable, Activatible{
	
	/**
	 * Determines if this linkable is relevant. A linkable is relevant if its total
	 * activation is greater or equal to the selection threshold.
	 * 
	 * @return <code>true</code> if this linkable is relevant
	 */
	public abstract boolean isOverThreshold();

	/**
	 * returns selection threshold
	 * 
	 * @return Selection threshold
	 */
	public abstract double getSelectionThreshold();
	
	/**
	 * Set the threshold
	 * @param threshold
	 */
	public abstract void setSelectionThreshold(double threshold);

	/**
	 * Returns final min activation variable
	 */
	public abstract double getMinActivation();

	/**
	 * Returns final max activation variable
	 */
	public abstract double getMaxActivation();

}
