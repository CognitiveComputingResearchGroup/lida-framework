/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collection;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.proceduralmemory.Stream;

public interface Behavior extends Activatible {
	
	//Ids
	public abstract long getId();
	public abstract long getActionId();
	public abstract String getLabel();

	//Context node type
	public abstract void setContextNodeType(String nodeType);
	public abstract String getContextNodeType();
	//
	public abstract boolean addContextCondition(Node condition);
	public abstract boolean containsContextCondition(Node commonNode);
	public abstract Collection<Node> getContextConditions();
	public abstract int getContextSize();
	
	//Context activation
	/**
	 * marks supplied condition as present 
	 */
	public abstract void updateContextCondition(Node condition);
	/**
	 * Returns true if supplied condition is satisfied
	 * @param condition
	 */
	public abstract boolean isContextConditionSatisfied(Node condition);
	/**
	 * Returns true if all context conditions are satisfied
	 */
	public abstract boolean isAllContextConditionsSatisfied();
	/**
	 * 
	 */
	public abstract void deactiveContextCondition(Node condition);
	
	/**
	 * deactivates all context conditions
	 */
	public abstract void deactivateAllContextConditions();
	
	//Add list
	public abstract Set<Node> getAddingList();
	public abstract boolean addToAddingList(Node condition);
	public abstract boolean containsAddingItem(Node commonNode);
	public abstract double getAddingListCount();
	
	//Delete list
	public abstract Set<Node> getDeletingList();	
    public abstract boolean addToDeletingList(Node deleteCondition);
    public abstract boolean containsDeletingItem(Node commonNode);
    public abstract double getDeletingListCount();
    
    //Containing streams
    public abstract void addContainingStream(Stream stream);
    public abstract Set<Stream> getContainingStreams();
    public abstract void removeContainingStream(Stream stream);
	
}//method