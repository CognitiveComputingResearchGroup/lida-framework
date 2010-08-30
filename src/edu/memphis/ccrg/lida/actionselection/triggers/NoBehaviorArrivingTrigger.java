package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class NoBehaviorArrivingTrigger extends NoActionSelectionOccurringTrigger {

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
	public void checkForTrigger(Set<Scheme> behaviors, double maxActivation) {
		reset();
	}
}
