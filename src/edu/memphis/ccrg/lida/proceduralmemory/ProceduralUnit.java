package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;

/**
 * An abstraction of the commonality between {@link Scheme} and {@link Behavior}
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public interface ProceduralUnit extends Activatible{
		
	/**
	 * Sets action
	 * @param a {@link Action} this unit contains
	 */
	public void setAction(Action a);
	
	/**
	 * Gets action.
	 * 
	 * @return the {@link Action} this unit contains
	 */
	public Action getAction();
	
	/**
	 * Gets label.
	 * 
	 * @return the label
	 */
	public String getLabel();

	/**
	 * Adds the context condition.
	 * 
	 * @param c the condition
	 * @return true, if successful
	 */
	public boolean addContextCondition(Condition c);
	
//    
//	/**
//	 * Adds the context condition.
//	 * 
//	 * @param c the condition
//	 *            
//	 * @param negated true for negated condition
//	 * @return true, if successful
//	 */
//	public boolean addContextCondition(Condition c, boolean negated);
	
//	
//	/**
//	 * Gets negated context conditions.
//	 * 
//	 * @return the context conditions that are negated
//	 */
//	public Collection<Condition> getNegatedContextConditions();
	
//	/**
//	 * Returns whether {@link ProceduralUnit} contains specified negated context condition.
//	 * 
//	 * @param c a {@link Condition}
//	 *            
//	 * @return true, if successful
//	 */
//	public boolean containsNegatedContextCondition(Condition c);
	
	/**
	 * Returns whether {@link ProceduralUnit} contains specified context condition.
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
	 * @return the size of this unit's result
	 */
    public double getResultSize(); 	
}
