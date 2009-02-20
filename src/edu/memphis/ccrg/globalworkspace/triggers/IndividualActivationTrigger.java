package edu.memphis.ccrg.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.globalworkspace.Coalition;


public class IndividualActivationTrigger extends AggregateActivationTrigger {
	
	public void command(Set<Coalition> coallitions) {
		for(Coalition c:coallitions){
			if(c.getActivation()>threshold){
				gw.trigger();
				break;
			}
		}
	}

}
