package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;


public class IndividualActivationTrigger extends AggregateActivationTrigger {
	
	public void checkForTrigger(Set<Coalition> coallitions) {
		for(Coalition c:coallitions){
			if(c.getActivation()>threshold){
				//System.out.println("Individual activation");
				gw.triggerBroadcast();
				break;
			}//if
		}//for
	}//method

}//class
