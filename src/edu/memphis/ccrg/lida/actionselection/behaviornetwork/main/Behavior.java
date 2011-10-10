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
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * An instantiated {@link Scheme} with a context, adding list, and deleting list.
 * @author Javier Snaider
 */

public interface Behavior extends Activatible {
	
	/**
	 * Gets this Behavior's id.
	 * 
	 * @return the id
	 */
	public long getId();
	
	/**
	 * Sets action
	 * @param a {@link Action} this behavior contains
	 */
	public void setAction(Action a);
	
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
	 * @param label the new label
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
	 * @param s the new generating scheme
	 */
	public void setGeneratingScheme(Scheme s);

	/**
	 * Adds the context condition.
	 * 
	 * @param c the condition
	 * @return true, if successful
	 */
	public boolean addContextCondition(Condition c);
	
    
	/**
	 * Adds the context condition.
	 * 
	 * @param c the condition
	 *            
	 * @param negated true for negated condition
	 * @return true, if successful
	 */
	public boolean addContextCondition(Condition c, boolean negated);
	
	
	/**
	 * Gets negated context conditions.
	 * 
	 * @return the context conditions that are negated
	 */
	public Collection<Condition> getNegatedContextConditions();
	
	/**
	 * Returns whether Behavior contains specified negated context condition.
	 * 
	 * @param c a {@link Condition}
	 *            
	 * @return true, if successful
	 */
	public boolean containsNegatedContextCondition(Condition c);
	
	/**
	 * Returns whether Behavior contains specified context condition.
	 * 
	 * @param c a {@link Condition}
	 *            
	 * @return true, if successful
	 */
	public boolean containsContextCondition(Condition c);
	
	/**
	 * Gets context condition with specified id
	 * @param id a {@link Condition} id
	 * @return Condition with id
	 */
	public Condition getContextCondition (Object id);
	
	/**
	 * Gets context conditions.
	 * 
	 * @return the context's conditions
	 */
	public Collection<Condition> getContextConditions();
	
	/**
	 * Gets context size.
	 * 
	 * @return the context size
	 */
	public int getContextSize();
	
	/**
	 * Returns true if supplied condition is satisfied
	 * @param c {@link Condition}
     * @return true, if is context condition satisfied
	 */
	public boolean isContextConditionSatisfied(Condition c);
	
	/**
	 * Returns true if all context conditions are satisfied
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
	 * Gets adding list.
	 * 
	 * @return the adding list
	 */
	public Collection<Condition> getAddingList();
	
	/**
	 * Adds the to adding list.
	 * 
	 * @param c the condition
	 * @return true, if successful
	 */
	public boolean addToAddingList(Condition c);
	
	/**
	 * Contains adding item.
	 * 
	 * @param c the {@link Condition}
	 * @return true, if successful
	 */
	public boolean containsAddingItem(Condition c);
	
	/**
	 * Gets adding list count.
	 * 
	 * @return the adding list count
	 */
	public double getAddingListCount();
	
	/**
	 * Gets deleting list.
	 * 
	 * @return the deleting list
	 */
	public Collection<Condition> getDeletingList();	
	
	 /**
	 * Adds the to deleting list.
	 * 
	 * @param c the delete condition
	 * @return true, if successful
	 */
    public boolean addToDeletingList(Condition c);
    
    /**
	 * Contains deleting item.
	 * 
	 * @param c
	 *            the {@link Condition}
	 * @return true, if successful
	 */
    public boolean containsDeletingItem(Condition c);
    
    /**
	 * Gets deleting list count.
	 * 
	 * @return the deleting list count
	 */
    public double getDeletingListCount();
    
    /**
	 * Gets result size.
	 * 
	 * @return the size of this behavior's result
	 */
    public double getResultSize(); 	
}