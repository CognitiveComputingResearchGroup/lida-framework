package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl.ConditionType;

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
	 * @param type the usage type of the condition
	 * @return true, if successful
	 * 
	 * @see ConditionType
	 */
	public boolean addCondition(Condition c, ConditionType type);
	
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
	 * Gets adding list.
	 * 
	 * @return the adding list
	 */
	public Collection<Condition> getAddingList();
		
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
    	
	/**
	 * Gets the average activation of this unit's context conditions.
	 * @return average activation of unit's context
	 */
	public double getAverageContextActivation();
	
	/**
	 * Gets the average net desirability of this unit's adding list
	 * @return average net desirability of this unit's adding list
	 */
	public double getAverageAddingListNetDesirability();		
}