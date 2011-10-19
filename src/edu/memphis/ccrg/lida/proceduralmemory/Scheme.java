/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;

/**
 * A Scheme consists of a Context, an Action and a Result.
 * A scheme is activated in the presence of its context.  If its action is taken 
 * then its result is more likely to be true. 
 * It can be instantiated in which case
 * the instantiation is called a {@link Behavior}.  
 * 
 * 
 * @author Ryan J. McCall
 */
public interface Scheme extends ProceduralUnit, Learnable{
	 	
	/**
	 * Gets scheme's id.
	 * @return unique identifier for this scheme
	 */
	public long getId();
	
	/**
	 * Returns reliability
	 * @return frequency that result is observed after scheme's {@link Action} is taken.
	 */
	public double getReliability();
	
	/**
	 * @return true if reliability is over threshold
	 */
	public boolean isReliable();
	
	/**
	 * Called when Scheme's action is executed
	 * Scheme should update the number of times its action has been executed in order to calculate
	 * reliability.
	 */
	public void actionExecuted();
	
	/**
	 * Called when Scheme's action produces expected result.
	 */
	public void actionSuccessful();
	
	/**
	 * Gets the number of executions
	 * @return how many times this scheme's action has been executed
	 */
	public int getExecutions();

	/**
	 * @return true if this scheme should not be decayed.
	 */
	public boolean isInnate();
	/**
	 * Sets innate
	 * @param innate whether this {@link Scheme} is hard-wired and cannot be decayed.
	 */
	public void setInnate(boolean innate);
	
	/**
	 * Instantiates and returns a {@link Behavior} based on this Scheme
	 * @return an instantiation of this scheme
	 */
	public Behavior getInstantiation();

	/**
	 * Sets reliabilityThreshold
	 * @param threshold threshold of reliability
	 */
	public void setReliabilityThreshold(double threshold);

	/**
	 * Gets reliabilityThreshold
	 * @return threshold of reliability
	 */
	public double getReliabilityThreshold();	
}