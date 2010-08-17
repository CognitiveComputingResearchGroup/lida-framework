package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class AggregateBehaviorActivationTrigger implements
		ActionSelectionTrigger {

	private Logger logger = Logger.getLogger("lida.actionselection.triggers");
	protected ActionSelection as;
	protected double threshold;
/**
 * This method is executed each time a new coalition enters the GW.
 * 
 * @param coalitions a Set with all the coallitions in the GW.
 */
	public void checkForTrigger(Queue<Scheme> behaviors) {
		double acc=0;
		for(Scheme c:behaviors){
			acc=acc+c.getActivation();
		}
		if(acc>threshold){
			logger.log(Level.FINE,"Aggregate Activation trigger ",LidaTaskManager.getActualTick());
			//System.out.println("aggregate ");
			as.triggerActionSelection();
		}
	}//method

	public void reset() {
		// not applicable
	}

	public void setUp(Map<String, Object> parameters, ActionSelection as) {
		this.as=as;
		Object o = parameters.get("threshold");
		if ((o != null)&& (o instanceof Double)) {
			threshold= (Double)o;
		}
	}//method

	public void start() {
		// not applicable
	}

}//class