/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * An instantiated {@link Scheme} with a context, adding list, and deleting list.
 */
public interface Behavior extends Activatible {
	
	/**
	 * Gets id.
	 * 
	 * @return the id
	 */
	public long getId();
	
	/**
	 * Sets action
	 * @param action {@link AgentAction} this behavior contains
	 */
	public void setAction(AgentAction action);
	
	/**
	 * Gets action.
	 * 
	 * @return the {@link AgentAction} this behavior contains
	 */
	public AgentAction getAction();
	
	/**
	 * Gets label.
	 * 
	 * @return the label
	 */
	public String getLabel();
	
	/**
	 * Sets label.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(String label);
	
	/**
	 * Gets generating scheme.
	 * 
	 * @return the generating scheme
	 */
	public Scheme getGeneratingScheme();
	
	/**
	 * Sets generating scheme.
	 * 
	 * @param s
	 *            the new generating scheme
	 */
	public void setGeneratingScheme(Scheme s);

	/**
	 * Sets context node type.
	 * 
	 * @param nodeType
	 *            the new context node type
	 */
	public void setContextNodeType(String nodeType);
	
	/**
	 * Gets context node type.
	 * 
	 * @return the context node type
	 */
	public String getContextNodeType();
	//
	/**
	 * Adds the context condition.
	 * 
	 * @param condition
	 *            the condition
	 * @return true, if successful
	 */
	public boolean addContextCondition(Node condition);
	
	/**
	 * Contains context condition.
	 * 
	 * @param commonNode
	 *            the common node
	 * @return true, if successful
	 */
	public boolean containsContextCondition(Node commonNode);
	
	/**
	 * Gets context conditions.
	 * 
	 * @return the context conditions
	 */
	public Collection<Node> getContextConditions();
	
	/**
	 * Gets context size.
	 * 
	 * @return the context size
	 */
	public int getContextSize();
	
	//Context activation
	/**
	 * marks supplied condition as present.
	 * 
	 * @param condition
	 *            the condition
	 */
	public void updateContextCondition(Node condition);
	
	/**
	 * Returns true if supplied condition is satisfied.
	 * 
	 * @param n
	 *            Node
	 * @return true, if is context condition satisfied
	 */
	public boolean isContextConditionSatisfied(Node n);
	
	/**
	 * Returns true if all context conditions are satisfied.
	 * 
	 * @return true, if is all context conditions satisfied
	 */
	public boolean isAllContextConditionsSatisfied();
	
	/**
	 * Gets unsatisfied context count.
	 * 
	 * @return the unsatisfied context count
	 */
	public int getUnsatisfiedContextCount();
	
	/**
	 * Deactive context condition.
	 * 
	 * @param condition
	 *            the condition
	 */
	public void deactiveContextCondition(Node condition);
	
	/**
	 * deactivates all context conditions.
	 */
	public void deactivateAllContextConditions();
	
	/**
	 * Gets adding list.
	 * 
	 * @return the adding list
	 */
	public NodeStructure getAddingList();
	
	/**
	 * Adds the to adding list.
	 * 
	 * @param condition
	 *            the condition
	 * @return true, if successful
	 */
	public boolean addToAddingList(Node condition);
	
	/**
	 * Contains adding item.
	 * 
	 * @param commonNode
	 *            the common node
	 * @return true, if successful
	 */
	public boolean containsAddingItem(Node commonNode);
	
	/**
	 * Gets adding list count.
	 * 
	 * @return the adding list count
	 */
	public double getAddingListCount();
	
	/**
	 * Update adding condition.
	 * 
	 * @param broadcastNode
	 *            the broadcast node
	 */
	public void updateAddingCondition(Node broadcastNode);
	
	/**
	 * Gets deleting list.
	 * 
	 * @return the deleting list
	 */
	public NodeStructure getDeletingList();	
    
    /**
	 * Adds the to deleting list.
	 * 
	 * @param deleteCondition
	 *            the delete condition
	 * @return true, if successful
	 */
    public boolean addToDeletingList(Node deleteCondition);
    
    /**
	 * Contains deleting item.
	 * 
	 * @param commonNode
	 *            the common node
	 * @return true, if successful
	 */
    public boolean containsDeletingItem(Node commonNode);
    
    /**
	 * Gets deleting list count.
	 * 
	 * @return the deleting list count
	 */
    public double getDeletingListCount();
    
    /**
	 * Update deleting condition.
	 * 
	 * @param broadcastNode
	 *            the broadcast node
	 */
    public void updateDeletingCondition(Node broadcastNode);
    
    /**
	 * Gets result size.
	 * 
	 * @return the result size
	 */
    public double getResultSize();   

}