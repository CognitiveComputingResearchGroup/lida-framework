/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork;

import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;

/**
 * An excite strategy for behaviors in the behavior network.
 * @author Ryan J. McCall
 */
public interface BehaviorExciteStrategy extends Strategy{
	
	/**
	 * Excites recipientBehavior from sourceBehavior's activation
	 * @param sourceBehavior source of activation
	 * @param recipientBehavior recipient of activation 
	 */
	public void exciteBehavior(Behavior sourceBehavior, Behavior recipientBehavior);

}
