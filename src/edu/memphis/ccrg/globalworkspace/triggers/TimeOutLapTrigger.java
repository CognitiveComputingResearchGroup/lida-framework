/**
 * 
 */
package edu.memphis.ccrg.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.globalworkspace.Coalition;


/**
 * @author Javier Snaider
 * 
 *
 */
public class TimeOutLapTrigger extends TimeOutTrigger {
	public void command(Set<Coalition> coallitions, double maxActivation) {
		reset();
	}


}
