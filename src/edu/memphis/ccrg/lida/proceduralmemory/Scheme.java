/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.shared.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 *
 * @author Ryan J. McCall
 */
public interface Scheme extends LearnableActivatible{
	
	/**
	 * unique identifier for this scheme
	 * @param id
	 */
	public void setId(long id);
	public long getId();
	
	/**
	 * id for the action in sensory-motor memory
	 * @return
	 */
	public long getSchemeActionId();
	public void setSchemeActionId(long actionId);	
	
	/**
	 * human-readable label
	 * @return
	 */
	public String getLabel();
	public void setLabel(String label);	
	
	//TODO Extend context to multiple node structures in a List
	public NodeStructure getContext();
	public void setContext(NodeStructure ns);
	
	public double getCuriosity();
	public void setCuriosity(double curiosity);

	public double getReliability();
	public boolean isReliable();
	
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
	
	/**
	 * Standard getter for numberOfExecutions.
	 * return the int value of numberOfExecutions
	 */
	public int getNumberOfExecutions();
	/**
	 * Standard setter for numberOfExecutions.
	 * @param numberOfExecutions the int value of numberOfExecutions
	 */
	public void setNumberOfExecutions(int numberOfExecutions);
	/**
	 * Increment numberOfExecutions.
	 */
	public void incrementNumberOfExecutions();

	/**
	 * Decay curiosity.
	 */
	public void decayCuriosity();
	
	//TODO Extend result to multiple node structures in a List
	public NodeStructure getAddingResult();
	public void setAddingResult(NodeStructure ns);
//	public List<NodeStructure> getResultConditions(long argumentId);
//	public void addResultConditions(long argumentId,NodeStructure ns);
	
	//TODO Extend result to multiple node structures in a List
	public NodeStructure getDeletingResult();
	public void setDeletingResult(NodeStructure ns);
	
	public void addArgument (Argument a);
	public Argument getArgument (long argumentId);
	public Collection<Argument> getArguments();
	
	/**
	 * 
	 * @return
	 */
	public Behavior getBehavior(NodeStructure broadcast);
	
}//interface
