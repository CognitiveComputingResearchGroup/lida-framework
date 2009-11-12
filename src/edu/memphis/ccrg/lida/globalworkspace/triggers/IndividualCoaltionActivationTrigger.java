package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * If any coalition is above threshold, this trigger will fire.
 * 
 * @author Javier Snaider
 *
 */
public class IndividualCoaltionActivationTrigger extends AggregateCoalitionActivationTrigger {
	
	public void checkForTrigger(Set<Coalition> coallitions) {
		for(Coalition c:coallitions){
			if(c.getActivation()>threshold){
				gw.triggerBroadcast();
				break;
			}
		}
	}//method

}//class
