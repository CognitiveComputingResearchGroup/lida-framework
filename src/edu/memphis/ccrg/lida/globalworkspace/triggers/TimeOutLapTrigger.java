/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;


/**
 * @author Javier Snaider
 * 
 *
 */
public class TimeOutLapTrigger extends TimeOutTrigger {
	public void checkForTrigger(Set<Coalition> coalitions, double maxActivation) {
		reset();
	}


}
