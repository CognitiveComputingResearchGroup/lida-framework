/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.example.genericlida.featuredetectors;

import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * @author Javier Snaider
 * 
 */
public class TopLeftDetector extends FeatureDetectorImpl {

	public TopLeftDetector(PamNode n, SensoryMemory sm,
			PerceptualAssociativeMemory pam) {
		super(n, sm, pam);
	}

	@Override
	public double detect() {
		double[][] data = (double[][]) sm.getSensoryContent("", null);

		if (data != null && data[0][0] > 0.0) {
			//this.excite(0.01);
			return 1.0;
		}
		return 0.0;
	}
 	
}
