package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class IndividualBehaviorActivationTrigger extends AggregateBehaviorActivationTrigger {
private Logger logger = Logger.getLogger("lida.actionselection.triggers");
	
	public void checkForTrigger(Set<Scheme> behaviors) {
		for(Scheme c:behaviors){
			if(c.getActivation()>threshold){
				logger.log(Level.FINE,"Individual Activation trigger ",LidaTaskManager.getActualTick());

				as.triggerActionSelection();
				break;
			}
		}
	}//method


}
