package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.List;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public interface Behavior extends Scheme {

	void deactivate();

	void resetActivation();

	List<ExpectationCodelet> getExpectationCodelets();

	double getBeta();

	void reinforce(double reinforcement);

	void prepareToFire(NodeStructure currentState);

}
