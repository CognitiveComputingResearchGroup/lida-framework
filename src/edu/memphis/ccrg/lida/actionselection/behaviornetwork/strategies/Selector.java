/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * A strategy for choosing which behavior to execute
 * @author ryanjmccall
 *
 */
public interface Selector {

	/**
	 * Select a single behavior as the current winner
	 * @return
	 */
	public abstract Behavior selectBehavior(Collection<Behavior> behaviors, double candidateThreshold);

}
