/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

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
	 * Gets schemeActionId.
	 * @return id of the scheme's action in {@link SensoryMotorMemory}
	 */
	public long getSchemeActionId();
	
	/**
	 * Sets schemeActionId.
	 * @param actionId id of the scheme's action in {@link SensoryMotorMemory}
	 */
	public void setSchemeActionId(long actionId);	
	
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
		
	//TODO How serious are we about this attributes?
	public double getCuriosity();
	public void setCuriosity(double curiosity);
	public double getReliability();
	public boolean isReliable();
	public void decayCuriosity();
	/**
	 * Standard getter for intrinsicBehavior.
	 * @return the boolean value of intrinsicBehavior
	 */
	public boolean isIntrinsicBehavior();
	/**
	 * Standard setter for intrinsicBehavior.
	 * @param intrinsicBehavior the boolean value of intrinsicBehavior
	 */
	public void setIntrinsicBehavior(boolean intrinsicBehavior);
	//TODO maybe this should be number of instantiations? schemes can't be executed.
	//behaviors can be selected and that is like an execution.
	
	public void incrementNumberOfExecutions();
	/**
	 * Standard getter for numberOfExecutions.
	 * @return the int value of numberOfExecutions
	 */
	public int getNumberOfExecutions();
	
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
	
	//TODO remove 
	public void addArgument (Argument a);
	public Argument getArgument (long argumentId);
	public Collection<Argument> getArguments();
	
	/**
	 * Instantiates and returns a {@link Behavior} based on this Scheme
	 * @return an instantiation of this scheme
	 */
	public Behavior getBehavior();
	
}
