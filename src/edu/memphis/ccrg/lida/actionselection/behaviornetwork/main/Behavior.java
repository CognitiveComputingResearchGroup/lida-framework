package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.Node;

public interface Behavior extends LearnableActivatible {
	
	//Ids
	public abstract long getId();
	public abstract long getSchemeActionId();
	public abstract String getLabel();

	//Preconditions
	public abstract Set<Node> getContextConditions();
	public abstract boolean addContextCondition(Node condition);
	public abstract int getContextSize();
	
	//Precondition satisfiability
	/**
	 * marks supplied condition as present 
	 */
	public abstract void satisfyContextCondition(Node condition);
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
	 * deactivates all context conditions
	 */
	public abstract void deactivateContext();
	
	//Add list
	public abstract Set<Node> getAddList();
	public abstract boolean addAddCondition(Node condition);
	public abstract double getAddListCount();
	
	//Delete list
	public abstract Set<Node> getDeleteList();	
    public abstract boolean addDeleteCondition(Node deleteCondition);
    public abstract double getDeleteListCount();
	
    //Successors
	public abstract int getSuccessorCount();
	public abstract Set<Behavior> getSuccessors(Node addProposition);
	public abstract void addSuccessor(Node contextNode, Behavior behavior);
	public abstract void removeSuccessor(Node precondition, Behavior behavior);
	
	//Predecessors
	public abstract int getPredecessorCount();
	public abstract Set<Behavior> getPredecessors(Node precondition);
	public abstract void addPredecessor(Node addItem, Behavior behavior);
	public abstract void removePredecessor(Node addItem, Behavior behavior);
	
	//Conflictors
	public abstract int getConflictorCount();
	public abstract Set<Behavior> getConflictors(Node precondition1);
	public abstract Collection<Set<Behavior>> getConflictors();
	public abstract void addConflictor(Node deleteItem, Behavior newBehavior);
	public abstract void addConflictors(Node precondition, Set<Behavior> deletors);
	public abstract void removeConflictor(Node deleteItem, Behavior behavior);
	
	//Codelets
	public abstract List<ExpectationCodelet> getExpectationCodelets();
	
}//method