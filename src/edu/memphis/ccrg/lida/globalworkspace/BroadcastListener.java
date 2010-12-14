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
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.ModuleListener;


/**
 * Modules that need to receive Broadcast must implement this interface. It will receive each 
 * Broadcast Content that will be sent by the GlobalWrokspace.
 * 
 * @author Javier Snaider
 * 
 */
public interface BroadcastListener extends ModuleListener{
	/**
	 * This method should return as possible in order to no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param bc the Content of the Broadcast
	 */
	public void receiveBroadcast(BroadcastContent bc);
	
	/**
	 * Theory says receivers of the broadcast should learn from it.
	 */
	public void learn();
	
}
