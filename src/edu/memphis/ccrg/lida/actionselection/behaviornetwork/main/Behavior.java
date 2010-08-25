package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

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
	public abstract Set<Node> getPreconditions();
	public abstract boolean addPrecondition(Node precondition);
	public abstract int getPreconditionCount();
	
	//Precondition satisfiability
	public abstract void satisfyPrecondition(Node proposition);
	public abstract boolean isPreconditionSatisfied(Node addProposition);
	public abstract boolean isAllPreconditionsSatisfied();
	public abstract void deactivateAllPreconditions();
	
	//Add list
	public abstract Set<Node> getAddList();
	public abstract boolean addAddCondition(Node addCondition);
	public abstract double getAddListCount();
	
	//Delete list
	public abstract Set<Node> getDeleteList();	
    public abstract boolean addDeleteCondition(Node deleteCondition);
    public abstract double getDeleteListCount();
	
    //Successors
	public abstract int getSuccessorCount();
	public abstract List<Behavior> getSuccessors(Node addProposition);
	public abstract void addSuccessor(Node contextNode, Behavior behavior);
	
	//Predecessors
	public abstract int getPredecessorCount();
	public abstract List<Behavior> getPredecessors(Node precondition);
	public abstract void addPredecessor(Node addItem, Behavior behavior);
	
	//Conflictors
	public abstract int getConflictorCount();
	public abstract List<Behavior> getConflictors(Node precondition1);
	public abstract void addConflictor(Node deleteItem, Behavior newBehavior);
	public abstract void addConflictors(Node precondition, List<Behavior> deletors);
	
	//Codelets
	public abstract List<ExpectationCodelet> getExpectationCodelets();

}//method