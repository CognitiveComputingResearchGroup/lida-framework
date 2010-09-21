/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.featuredetector;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * This interface makes more sense if you look at the implementation of it
 * @author Javier Snaider
 * @see FeatureDetectorImpl
 */
public interface FeatureDetector extends LidaTask {
	
	public abstract double detect(); 
	
	/**
	 * Get nodes that this detector is looking for.
	 */
	public abstract Collection<PamNode> getPamNodes();
	
	/**
	 * 
	 * @param node
	 */
	public abstract void addPamNode(PamNode node);
	
	public abstract void excitePam(double detectionActivation);
}
