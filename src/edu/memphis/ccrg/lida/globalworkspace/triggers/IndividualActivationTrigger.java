package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.CoalitionInterface;


public class IndividualActivationTrigger extends AggregateActivationTrigger {
	
	public void command(Set<CoalitionInterface> coallitions) {
		for(CoalitionInterface c:coallitions){
			if(c.getActivation()>threshold){
				gw.trigger();
				break;
			}
		}
	}

}
