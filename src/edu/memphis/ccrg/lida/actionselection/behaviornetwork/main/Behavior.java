package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.Node;

public interface Behavior extends Activatible {
	
	//Ids
	public abstract long getId();
	public abstract long getActionId();
	public abstract String getLabel();

	//Preconditions
	public abstract Set<Node> getContextConditions();
	public abstract boolean addContextCondition(Node condition);
	public abstract int getContextSize();
	
	//Precondition satisfiability
	/**
	 * marks supplied condition as present 
	 */
	public abstract void updateContextCondition(Node condition);
	/**
	 * Returns true if supplied condition is satisfied
	 * @param addProposition
	 * @return
	 */
	public abstract boolean isContextConditionSatisfied(Node condition);
	/**
	 * Returns true if all context conditions are satisfied
	 * @return
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
	public abstract Set<Node> getAddList();
	public abstract boolean addAddCondition(Node condition);
	public abstract double getAddListCount();
	
	//Delete list
	public abstract Set<Node> getDeleteList();	
    public abstract boolean addDeleteCondition(Node deleteCondition);
    public abstract double getDeleteListCount();
    
    //Containing streams
    void addContainingStream(Stream stream);
	List<Stream> getContainingStreams();
	
}//method