package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.CoalitionInterface;
import edu.memphis.ccrg.lida.globalworkspace.Trigger;
import edu.memphis.ccrg.lida.globalworkspace.TriggerListener;

/**
 * Implements a trigger that is activated when the sum of all coalitions in GW 
 * is greater than the threshold.
 * 
 * @author Javier Snaider
 *
 */
public class AggregateActivationTrigger implements Trigger {

	protected TriggerListener gw;
	protected double threshold;
/**
 * This method is executed each time a new coalition enters the GW.
 * 
 * @param coalitions a Set with all the coallitions in the GW.
 */
	public void command(Set<CoalitionInterface> coallitions) {
		double acc=0;
		for(CoalitionInterface c:coallitions){
			acc=acc+c.getActivation();
		}
		if(acc>threshold){
			gw.trigger();
		}
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	public void setUp(Map<String, Object> parameters, TriggerListener gw) {
		this.gw=gw;
		Object o = parameters.get("threshold");
		if ((o != null)&& (o instanceof Double)) {
			threshold= (Double)o;
		}

	}

	public void start() {
		// TODO Auto-generated method stub

	}

}
