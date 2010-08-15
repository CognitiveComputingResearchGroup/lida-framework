package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.List;
import java.util.Set;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

//TODO figure out how to cull the number of methods
public interface Behavior extends Scheme {

	public abstract void deactivatePreconditions();

	public abstract void resetActivation();

	public abstract List<ExpectationCodelet> getExpectationCodelets();

	public abstract double getBaseLevelActivation();

	public abstract void reinforce(double reinforcement);

	public abstract void prepareToFire(NodeStructure currentState);

	public abstract double getAlpha();

	public abstract void decay(double n_activation);

	public abstract void inhibit(double d);

	public abstract void merge(double baseLevelActivationAmplicationFactor);

	public abstract boolean isActive();

	public abstract void spreadInhibition(NodeStructure currentState, double goalExcitationAmount, double protectedGoalInhibitionAmount);

	public abstract void spreadExcitation(double broadcastExcitationAmount, double goalExcitationAmount);
	
	//Preconditions
	public abstract int getPreconditionCount();
	public abstract Set<Node> getPreconditions();
	public abstract boolean addPrecondition(Node precondition);
	
	//Add list
	public abstract Set<Node> getAddList();
	public abstract boolean addAddCondition(Node addCondition);
	
	//Delete list
	public abstract Set<Node> getDeleteList();	
    public abstract boolean addDeleteCondition(Node deleteCondition);
	
    //Successors
	public abstract int getSuccessorCount();
	public abstract List<Behavior> getSuccessors(Node addProposition);
	public abstract void addSuccessors(Node addProposition, List<Behavior> behaviors);
	
	//Predecessors
	public abstract int getPredecessorCount();
	public abstract List<Behavior> getPredecessors(Node precondition);
	public abstract void addPredecessors(Node precondition, List<Behavior> behaviors);
	
	//Conflictors
	public abstract int getConflictorCount();
	public abstract List<Behavior> getConflictors(Node precondition1);
	public abstract void addConflictors(Node precondition1, List<Behavior> behaviors);
	
	public abstract boolean containsPrecondition(Node addProposition);
	public abstract void satisfyPrecondition(Node proposition);

}//method