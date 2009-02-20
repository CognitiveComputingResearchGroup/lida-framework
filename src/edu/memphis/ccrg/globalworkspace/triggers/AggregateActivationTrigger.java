package edu.memphis.ccrg.globalworkspace.triggers;

import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.globalworkspace.Coalition;
import edu.memphis.ccrg.globalworkspace.Trigger;
import edu.memphis.ccrg.globalworkspace.TriggerListener;


public class AggregateActivationTrigger implements Trigger {

	protected TriggerListener gw;
	protected double threshold;

	public void command(Set<Coalition> coallitions) {
		double acc=0;
		for(Coalition c:coallitions){
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
