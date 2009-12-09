package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * If any coalition is above threshold, this trigger will fire.
 * 
 * @author Javier Snaider
 *
 */
public class IndividualCoaltionActivationTrigger extends AggregateCoalitionActivationTrigger {
	private Logger logger = Logger.getLogger("lida.globalworkspace.triggers");
	
	public void checkForTrigger(Set<Coalition> coallitions) {
		for(Coalition c:coallitions){
			if(c.getActivation()>threshold){
				logger.log(Level.FINE,"Individual Activation trigger ",LidaTaskManager.getActualTick());

				gw.triggerBroadcast();
				break;
			}
		}
	}//method

}//class
