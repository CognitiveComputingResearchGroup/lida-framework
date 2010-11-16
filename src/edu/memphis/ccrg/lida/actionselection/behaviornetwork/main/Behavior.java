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
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.Stream;

public interface Behavior extends Activatible {
	
	//Ids
	public long getId();
	public long getActionId();
	public String getLabel();
	public void setLabel(String label);
	
	public Scheme getGeneratingScheme();
	public void setGeneratingScheme(Scheme s);

	//Context node type
	public void setContextNodeType(String nodeType);
	public String getContextNodeType();
	//
	public boolean addContextCondition(Node condition);
	public boolean containsContextCondition(Node commonNode);
	public Collection<Node> getContextConditions();
	public int getContextSize();
	
	//Context activation
	/**
	 * marks supplied condition as present 
	 */
	public void updateContextCondition(Node condition);
	/**
	 * Returns true if supplied condition is satisfied
	 * @param n Node
	 */
	public boolean isContextConditionSatisfied(Node n);
	/**
	 * Returns true if all context conditions are satisfied
	 */
	public boolean isAllContextConditionsSatisfied();
	
	public int getUnsatisfiedContextCount();
	
	/**
	 * 
	 */
	public void deactiveContextCondition(Node condition);
	
	/**
	 * deactivates all context conditions
	 */
	public void deactivateAllContextConditions();
	
	//Add list
	public Collection<Node> getAddingList();
	public boolean addToAddingList(Node condition);
	public boolean containsAddingItem(Node commonNode);
	public double getAddingListCount();
	public void updateAddingCondition(Node broadcastNode);
	
	//Delete list
	public Collection<Node> getDeletingList();	
    public boolean addToDeletingList(Node deleteCondition);
    public boolean containsDeletingItem(Node commonNode);
    public double getDeletingListCount();
    public void updateDeletingCondition(Node broadcastNode);
    
    //result
    public double getResultSize();
    
    //Containing streams
    public void addContainingStream(Stream stream);
    public Set<Stream> getContainingStreams();
    public void removeContainingStream(Stream stream);
	
}//method