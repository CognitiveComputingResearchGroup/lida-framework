/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

public interface DecayStrategy extends Strategy{

    /**
     * Decays the current activation according to some internal decay function.
     * @param currentActivation activation of the entity before decay.
     * @param ticks The number of ticks to decay.
     * 
     */
	public double decay(double currentActivation, long ticks, Object... params);
	
	/**
	 * 
	 * @param currentActivation activation of the entity before decay.
	 * @param ticks how much time has passed since last decay
	 * @param params parameters
	 * @return new activation amount
	 */
	public double decay(double currentActivation, long ticks, Map<String, ? extends Object>params);
        
}
