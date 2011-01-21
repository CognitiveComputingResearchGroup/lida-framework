/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Calculates a new activation using an upscale parameter.
 * 
 * @author Ryan J. McCall
 */
public class UpscalePropagationBehavior implements PropagationBehavior{
	
	private Logger logger = Logger.getLogger(UpscalePropagationBehavior.class.getCanonicalName());

	/**
	 * Calculate and return an activation to propagate.
	 */
	@Override
	public double getActivationToPropagate(Map<String, Object> params) {
		Double totalActiv = (Double) params.get("totalActivation");
		Double upscale = (Double) params.get("upscale");
		
		if(totalActiv == null || upscale == null){
			logger.log(Level.WARNING,"Unable to obtain parameters",LidaTaskManager.getCurrentTick());
			return 0;
		}else
			return totalActiv * upscale;
	}
}
