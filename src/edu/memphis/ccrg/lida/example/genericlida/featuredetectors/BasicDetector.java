/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.example.genericlida.featuredetectors;

import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This is an example of FeatureDetector.
 * The method detect() is domain specific and can call domain 
 * specific methods in SensoryMemory to get specific content 
 * from there. The generic method getContent(String type,Object... parameters) 
 * of SensoryMemory can be used too.  
 * 
 * @author Ryan J. McCall - Javier Snaider
 *
 */
public class BasicDetector extends BasicDetectionAlgorithm {

	public BasicDetector(PamLinkable eid, SensoryMemory sm, 
								 PerceptualAssociativeMemory pam) {
		super(eid, sm, pam);
	}

	@Override
	public double detect() {
		return (Math.random())<.03?Math.random():0.0;
	}
	
	@Override
	public String toString(){
		return "BasicDetector " + getTaskId();
	}
 
}
