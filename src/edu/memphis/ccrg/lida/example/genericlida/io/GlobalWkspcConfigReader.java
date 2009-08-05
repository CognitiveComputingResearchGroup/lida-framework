package edu.memphis.ccrg.lida.example.genericlida.io;

import java.util.HashMap;
import java.util.Map;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutLapTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

public class GlobalWkspcConfigReader {

	public void read(GlobalWorkspace globalWksp, String inputPath) {
		BroadcastTrigger tr;
		Map<String, Object> parameters;
		
		tr = new TimeOutTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOut");
		parameters.put("delay", 5L); //Individual activation trigger will still dominate.
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	
		tr = new AggregateActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", 0.8);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	
		tr = new TimeOutLapTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOutLap");
		parameters.put("delay", 50L);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	
		tr = new IndividualActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", 0.5);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
		
	}//method

}//class
