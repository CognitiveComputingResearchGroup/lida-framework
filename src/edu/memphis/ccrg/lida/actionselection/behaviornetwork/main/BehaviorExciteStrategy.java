package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.strategies.Strategy;

public interface BehaviorExciteStrategy extends Strategy{
	
	public void exciteBehavior(Behavior sourceBehavior, Behavior recipientBehavior);

}
