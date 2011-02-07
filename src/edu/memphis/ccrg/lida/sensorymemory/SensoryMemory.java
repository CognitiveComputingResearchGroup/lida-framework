/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;

/**
 * This is the interface to be implemented by sensory memory modules. Implementing
 * modules sense the environment, store the sensed data ,and process it.
 * @author Ryan J. McCall
 */
//TODO have implementers implement SensoryMotorMemoryListener?
public interface SensoryMemory extends LidaModule, SensoryMotorMemoryListener, Saveable {

	/**
	 * Runs all the sensors associated with this memory. The sensors get the
         * information from the environment and store in this memory for later
         * processing and passing to the perceptual memory module. 
	 */
	public void runSensors();
	
	/**
	 * Adds a listener to this memory. This listener constantly
         * checks for information being sent from this memory to other modules
         * (Perceptual Associative Memory and Sensory Motor Memory).
	 * @param l the listener added to this memory
	 */
	public void addSensoryMemoryListener(SensoryMemoryListener l);

}
