/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;

/**
 * A {@link Strategy} that activates schemes whose context and/or result intersects with the conscious broadcast.
 * @author Ryan J. McCall
 */
public interface SchemeActivationStrategy extends Strategy{
	
	/**
	 * Activates schemes which intersect with the broadcast.
	 * @param broadcast {@link NodeStructure}
	 * @param params optional parameters which will be at least the {@link Scheme}s of {@link ProceduralMemory}
	 */
	public void activateSchemesWithBroadcast(NodeStructure broadcast, Object... params);
	
	/**
	 * Set amount of activation a {@link Scheme} must have for instantiation
	 * @param threshold threshold
	 */
	public void setSchemeSelectionThreshold(double threshold);

	/**
	 * Sets associated module {@link ProceduralMemory}
	 * @param pm the {@link ProceduralMemory} to associate
	 */
	public void setProceduralMemory(ProceduralMemory pm);

}
