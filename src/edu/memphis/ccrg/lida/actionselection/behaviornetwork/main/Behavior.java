/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * An instantiated {@link Scheme} with a context, adding list, and deleting list.
 * @author Javier Snaider
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
	 * @param action {@link Action} this behavior contains
	 */
	public void setAction(Action action);
	
	/**
	 * Gets action.
	 * 
	 * @return the {@link Action} this behavior contains
	 */
	public Action getAction();
	
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

	//
	public boolean addContextCondition(Condition condition);
	public boolean containsContextCondition(Condition commonNode);
	public Condition getContextCondition (Object id);
	//public boolean isContextConditionNegated(Condition commonNode);
	public Collection<Condition> getContextConditions();
	public int getContextSize();
	
	//Context activation
//	/**
//	 * marks supplied condition as present 
//	 */
//	public void updateContextCondition(Condition condition);
	/**
	 * Returns true if supplied condition is satisfied
	 * @param n Node
	 */
	public boolean isContextConditionSatisfied(Condition n);
	/**
	 * Returns true if all context conditions are satisfied
	 */
	public boolean isAllContextConditionsSatisfied();
	
	public int getUnsatisfiedContextCount();
	
//	public void deactiveContextCondition(Condition condition);
	
//	public void deactivateAllContextConditions();
	
	//Add list
	public Collection<Condition> getAddingList();
	public boolean addToAddingList(Condition condition);
	public boolean containsAddingItem(Condition commonNode);
	public double getAddingListCount();
//	public void updateAddingCondition(Condition broadcastNode);
	
	//Delete list
	public Collection<Condition> getDeletingList();	
    public boolean addToDeletingList(Condition deleteCondition);
    public boolean containsDeletingItem(Condition commonNode);
    public double getDeletingListCount();
//    public void updateDeletingCondition(Condition broadcastNode);
    
    //result
    public double getResultSize();
	boolean addContextCondition(Condition condition, boolean negated);
	Collection<Condition> getNegatedContextConditions();
	boolean containsNegatedContextCondition(Condition contextCondition);
    	
}//method