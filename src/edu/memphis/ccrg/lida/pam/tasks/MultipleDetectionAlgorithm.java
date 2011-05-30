/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This class implements the FeatureDetector interface and provides default methods. 
 * Users should extend this class and overwrite the detect() and excitePam() methods.
 * A convenience init() method is added to initialize the class. This method can be 
 * overwritten as well.
 * This implementation is oriented to detect features from sensoryMemory, but the implementation 
 * can be used to detect and burstActivation from other modules, like Workspace, emotions or internal states.
 * 
 * @author Ryan J. McCall
 *
 * 
 */
public abstract class MultipleDetectionAlgorithm extends FrameworkTaskImpl implements DetectionAlgorithm {

	private static final Logger logger = Logger.getLogger(MultipleDetectionAlgorithm.class.getCanonicalName());
	/**
	 * Map of {@link PamLinkable}
	 */
	protected Map<String, PamLinkable> pamNodeMap;
	/**
	 * the {@link PerceptualAssociativeMemory}
	 */
	protected PerceptualAssociativeMemory pam;
	/**
	 * {@link PamLinkable} this algorithm detects
	 */
	protected SensoryMemory sensoryMemory;
	
	/**
	 * @param nodeMap Nodes indexed by their label
	 * @param sm {@link SensoryMemory}
	 * @param pam {@link PerceptualAssociativeMemory}
	 */
	public MultipleDetectionAlgorithm(Map<String, PamLinkable> nodeMap, SensoryMemory sm,
	   		   						   PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		this.sensoryMemory = sm;
		this.pamNodeMap = nodeMap;
	}
	
	@Override
	protected void runThisFrameworkTask(){
		detectMultipleLinkables();
		if(logger.isLoggable(Level.FINEST)){
			logger.log(Level.FINEST,"detection performed {1}"
					,new Object[]{TaskManager.getCurrentTick(),this});
		}
	}

	/**
	 * Override this method for domain-specific feature detection
	 */
	public abstract void detectMultipleLinkables();
	
	@Override
	public String toString(){
		return "Feature Detector ["+getTaskId()+"] ";
	}

}
