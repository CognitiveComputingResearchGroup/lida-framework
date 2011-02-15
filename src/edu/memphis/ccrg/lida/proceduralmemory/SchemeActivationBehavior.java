/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * A behavior to activation schemes whose context and/or result intersects with the conscious broaadcast.
 * @author Ryan J. McCall
 */
public interface SchemeActivationBehavior {
	
	/**
	 * Activate schemes which intersect with the broadcast.
	 * @param broadcast NodeStructure
	 * @param schemeMap schemes indexed in some way
	 */
	public void activateSchemesWithBroadcast(NodeStructure broadcast, Map<?, Set<Scheme>> schemeMap);
	
	/**
	 * Set amount of activation a {@link Scheme} must have for instantiation
	 * @param threshold threshold
	 */
	public void setSchemeSelectionThreshold(double threshold);

}
