/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.CoalitionInterface;


/**
 * @author Javier Snaider
 * 
 *
 */
public class TimeOutLapTrigger extends TimeOutTrigger {
	public void command(Set<CoalitionInterface> coallitions, double maxActivation) {
		reset();
	}


}
