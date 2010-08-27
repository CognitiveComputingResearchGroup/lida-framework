/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * @author ryanjmccall
 *
 */
public class BehaviorNetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActionSelection behaviorNet = new BehaviorNetworkImpl();
		TaskSpawner taskSpawner = new ActionSelectionDriver();
		behaviorNet.setTaskSpawner(taskSpawner);
		
		Map<String, Double> params = new HashMap<String, Double>();
		//params.put("", value)
		
		
		
		
		((BehaviorNetworkImpl) behaviorNet).init(params);
		
	}

}
