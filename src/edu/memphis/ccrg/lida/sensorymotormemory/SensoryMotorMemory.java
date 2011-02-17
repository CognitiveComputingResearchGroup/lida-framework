/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;

/**
 * Sensory Motor Memory is a module which receives selected actions from ActionSelection and
 * content from SensoryMemory.  It contains the algorithm for a selected action.  When it executes an algorithm it
 * directly calls a method in the environment (doesn't use a listener).  
 * @author Ryan J McCall, Javier Snaider
 *
 */
public interface SensoryMotorMemory extends LidaModule, Saveable{

	/**
	 * Any non-environment communication should use listeners.
	 * @param l SensoryMotorMemoryListener
	 */
	public void addSensoryMotorMemoryListener(SensoryMotorMemoryListener l);
	
	/**
	 * Adds action with associated actionId
	 * @param lidaActionId id by which action is referenced
	 * @param actionAlgorithm an algorithm 
	 */
	public void addActionAlgorithm(long lidaActionId, Object actionAlgorithm);
	
	/**
	 * Specify the algorithms in SensoryMotorMemory 
	 * @param actionMap Algorithms indexed by LidaAction id
	 */
	public void setLidaActionAlgorithmMap(Map<Long, Object> actionMap);
	
	/**
	 * Executes specified action algorithm 
	 * @param algorithm algorithm to execute in the Environment
	 */
	public void executeAction(Object algorithm);

}
