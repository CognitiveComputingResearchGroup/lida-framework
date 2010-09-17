package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * Implements a trigger that is activated when the sum of all coalitions in GW 
 * is greater than the threshold.
 * 
 * @author Javier Snaider
 *
 */
public class AggregateCoalitionActivationTrigger implements BroadcastTrigger {
	
	private Logger logger = Logger.getLogger("lida.globalworkspace.triggers");
	protected GlobalWorkspace gw;
	protected double threshold;
/**
 * This method is executed each time a new coalition enters the GW.
 * 
 * @param coalitions a Set with all the coallitions in the GW.
 */
	public void checkForTrigger(Collection<Coalition> coalitions) {
		double acc=0;
		for(Coalition c:coalitions){
			acc=acc+c.getActivation();
		}
		if(acc>threshold){
			logger.log(Level.FINE,"Aggregate Activation trigger ",LidaTaskManager.getActualTick());
			//System.out.println("aggregate ");
			gw.triggerBroadcast();
		}
	}//method

	public void reset() {
		// not applicable
	}

	public void setUp(Map<String, Object> parameters, GlobalWorkspace gw) {
		this.gw=gw;
		Object o = parameters.get("threshold");
		if ((o != null)&& (o instanceof Double)) {
			threshold= (Double)o;
		}
	}//method

	public void start() {
		// not applicable
	}

}//class