package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;


/**
 * 
 * This trigger fires when x ms has passed w/o a new broadcast.
 * Check the parent class NoBroadcastTrigger for full understanding.
 * 
 * @author Javier Snaider
 *
 */
public class NoCoalitionArrivingTrigger extends NoBroadcastOccurringTrigger {
	
	/**
	 * Every time a new coalition is added 'checkForTrigger' is called for each trigger.
	 * In the case of this trigger the 'reset()' method inherited from NoBroadcastTrigger
	 * is called which resets the timer task object.  
	 * 
	 * So this trigger fires when x ms has passed w/o a new broadcast. 
	 * 
	 * @param coalitions
	 * @param maxActivation
	 */
	public void checkForTrigger(Set<Coalition> coalitions, double maxActivation) {
		reset();
	}

}
