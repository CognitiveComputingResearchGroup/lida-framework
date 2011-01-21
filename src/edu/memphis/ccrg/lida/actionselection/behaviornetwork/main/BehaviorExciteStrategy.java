package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.strategies.Strategy;

/**
 * An excite strategy for behaviors in the behavior network.
 * @author Ryan J. McCall
 */
public interface BehaviorExciteStrategy extends Strategy{
	
	/**
	 * Excites recipientBehavior from sourceBehavior's activation
	 * @param sourceBehavior source of activation
	 * @param recipientBehavior recipient of activation 
	 */
	public void exciteBehavior(Behavior sourceBehavior, Behavior recipientBehavior);

}
