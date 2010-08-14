package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

//TODO figure out how to cull the number of methods
public interface Behavior extends Scheme {

	void deactivatePreconditions();

	void resetActivation();

	List<ExpectationCodelet> getExpectationCodelets();

	double getBaseLevelActivation();

	void reinforce(double reinforcement);

	void prepareToFire(NodeStructure currentState);

	double getAlpha();

	void decay(double n_activation);

	double getPreconditionCount();

	Set<Node> getPreconditions();

	Set<Node> getAddList();

	Set<Node> getDeleteList();

	void inhibit(double d);

	void merge(double baseLevelActivationAmplicationFactor);

	boolean isActive();

	void spreadInhibition(NodeStructure currentState,
			double goalExcitationAmount, double protectedGoalInhibitionAmount);

	void spreadExcitation(double broadcastExcitationAmount,
			double goalExcitationAmount);
	

	List<Behavior> getConflictors(Node precondition1);

	void addConflictors(Node precondition1, List<Behavior> behaviors);

	boolean containsPrecondition(Node addProposition);

	List<Behavior> getPredecessors(Node precondition);

	void addPredecessors(Node precondition, List<Behavior> behaviors);

	List<Behavior> getSuccessors(Node addProposition);

	void addSuccessors(Node addProposition, List<Behavior> behaviors);

	void satisfyPrecondition(Node proposition);

}
