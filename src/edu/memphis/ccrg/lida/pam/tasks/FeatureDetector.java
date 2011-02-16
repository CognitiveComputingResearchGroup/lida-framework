/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * A process which detects a pattern (feature) in {@link SensoryMemory} content and excites {@link PamNode}s 
 * representing that pattern.
 * @author Javier Snaider
 * @see FeatureDetectorImpl
 */
public interface FeatureDetector extends LidaTask {
		
	/**
	 * Detects a feature.
	 * @return value from 0.0 to 1.0 representing the degree to which the feature occurs.
	 */
	public double detect(); 
	
	/**
	 * Returns PamNodes this feature detector can access.
	 * 
	 * @return the pam nodes
	 */
	public Collection<PamNode> getPamNodes();
	
	/**
	 * 
	 * Provides this feature detector with access to specified PamNode.
	 * @param node
	 *            the node
	 */
	public void addPamNode(PamNode node);
	
	/**
	 * Excites PAM some amount.
	 * @param detectionActivation Amount to excite.
	 */
	public void excitePam(double detectionActivation);
}
