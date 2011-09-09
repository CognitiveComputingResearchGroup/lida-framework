/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.ModuleListener;


/**
 * Modules that need to receive Broadcast must implement this interface. It will receive each 
 * BroadcastContent that will be sent by the GlobalWrokspace.
 * 
 * @author Javier Snaider, Ryan McCall
 * 
 */
public interface BroadcastListener extends ModuleListener{
	
	/**
	 * This method should return as possible in order to not delay the rest of the broadcasting.
	 * A good implementation should copy the broadcast content and create a task to process it.
	 * @param coalition coalition that won the competition for consciousness
	 */
	public void receiveBroadcast(Coalition coalition);
	
	/**
	 * This is a place-holder method to remind implementing classes that 
	 * they should implement learning. LIDA theory says receivers of the broadcast 
	 * should learn from it.
	 * This method will not be called directly by {@link GlobalWorkspace} 
	 * but should be managed by the receiving module.
	 * @param coalition coalition that won the competition for consciousness
	 */
	public void learn(Coalition coalition);
	
}
