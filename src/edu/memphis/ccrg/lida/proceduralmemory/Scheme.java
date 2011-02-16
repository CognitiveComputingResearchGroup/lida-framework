/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;

/**
 * A Scheme consists of a Context, an Action and a Result.
 * A scheme is activated in the presence of its context.  If its action is taken 
 * then its result is more likely to be true. 
 * It can be instantiated in which case
 * the instantiation is called a behavior.  
 * 
 * 
 * @author Ryan J. McCall
 */
public interface Scheme extends Learnable{
	 
	/**
	 * Sets scheme's id.
	 * @param id unique identifier for this scheme
	 */
	public void setId(long id);
	
	/**
	 * Gets scheme's id.
	 * @return unique identifier for this scheme
	 */
	public long getId();
	
	/**
	 * Gets schemeAction.
	 * @return scheme's action in {@link SensoryMotorMemory}
	 */
	public LidaAction getSchemeAction();
	
	/**
	 * Sets schemeAction.
	 * @param scheme's action in {@link SensoryMotorMemory}
	 */
	public void setSchemeAction(LidaAction action);	
	
	/**
	 * Gets label.
	 * @return human-readable label
	 */
	public String getLabel();
	
	
	/**
	 * Sets label.
	 * @param label human-readable label
	 */
	public void setLabel(String label);	
	
	
	/**
	 * Gets context.
	 * @return context in which this scheme is activated.
	 */
	public NodeStructure getContext();
	
	/**
	 * Sets context.
	 * @param ns context in which this scheme is activated.
	 */
	public void setContext(NodeStructure ns);
	
	/**
	 * Returns reliability
	 * @return frequency that result is observed after scheme's {@link LidaAction} is taken.
	 */
	public double getReliability();
	
	/**
	 * @return true if reliability is over threshold
	 */
	public boolean isReliable();
	
	/**
	 * Scheme should update the number of times its action has been executed in order to calculate
	 * reliability.
	 */
	public void actionWasExecuted();

	/**
	 * @return true if this scheme should not be decayed.
	 */
	public boolean isInnate();
	/**
	 * Sets innate
	 * @param innate whether this behavior is hard-wired and cannot be decayed.
	 */
	public void setInnate(boolean innate);
	
	/**
	 * Returns addingResult.
	 * @return NodeStructure representing what this scheme's action should add if executed. 
	 */
	public NodeStructure getAddingResult();
	
	/**
	 * Sets addingResult.
	 * @param ns NodeStructure representing what this scheme's action should add if executed. 
	 */
	public void setAddingResult(NodeStructure ns);
	
	/**
	 * Returns deletingResult.
	 * @return NodeStructure representing what this scheme's action should delete if executed. 
	 */
	public NodeStructure getDeletingResult();
	
	/**
	 * Sets deletingResult.
	 * @param ns NodeStructure representing what this scheme's action should delete if executed. 
	 */
	public void setDeletingResult(NodeStructure ns);
	
	/**
	 * Instantiates and returns a {@link Behavior} based on this Scheme
	 * @return an instantiation of this scheme
	 */
	public Behavior getInstantiation();
	
}
