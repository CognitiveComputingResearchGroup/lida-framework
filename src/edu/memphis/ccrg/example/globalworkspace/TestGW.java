/**
 * 
 */
package edu.memphis.ccrg.example.globalworkspace;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.globalworkspace.Trigger;
import edu.memphis.ccrg.globalworkspace.TriggerListener;
import edu.memphis.ccrg.globalworkspace.triggers.AggregateActivationTrigger;
import edu.memphis.ccrg.globalworkspace.triggers.IndividualActivationTrigger;
import edu.memphis.ccrg.globalworkspace.triggers.TimeOutLapTrigger;
import edu.memphis.ccrg.globalworkspace.triggers.TimeOutTrigger;


/**
 * @author Javier Snaider
 * 
 */
public class TestGW {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GlobalWorkspace gw = new GlobalWorkspaceImpl();
		Trigger tr;
		Map<String, Object> parameters;
		
		tr = new TimeOutTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOut");
		parameters.put("delay", 100L);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addTrigger(tr);

		tr = new AggregateActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", 0.8);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addTrigger(tr);

		tr = new TimeOutLapTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOutLap");
		parameters.put("delay", 50L);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addTrigger(tr);

		tr = new IndividualActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", 0.5);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addTrigger(tr);


		gw.addBroadcastListener(new BroadcastListener(){

			public void broadcast(BroadcastContent bc) {
				System.out.println(bc);
			}});
		
		Thread threadW=new Thread(new MockWorkspace(gw,55));
		gw.start();
		threadW.start();
		
	}

}
