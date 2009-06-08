package edu.memphis.ccrg.lida.serialization;

import java.util.HashMap;
import java.util.Map;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.Trigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

public class GlobalWorkspace_Input {

	public void read(GlobalWorkspace globalWksp, String inputPath) {
		Trigger tr;
		Map<String, Object> parameters;
		
		tr = new TimeOutTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOut");
		parameters.put("delay", 100L);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addTrigger(tr);
	
	//	tr = new AggregateActivationTrigger();
	//	parameters = new HashMap<String, Object>();
	//	parameters.put("threshold", 0.8);
	//	tr.setUp(parameters, (TriggerListener) gwksp);
	//	gwksp.addTrigger(tr);
	//
	//	tr = new TimeOutLapTrigger();
	//	parameters = new HashMap<String, Object>();
	//	parameters.put("name", "TimeOutLap");
	//	parameters.put("delay", 50L);
	//	tr.setUp(parameters, (TriggerListener) gwksp);
	//	gwksp.addTrigger(tr);
	//
	//	tr = new IndividualActivationTrigger();
	//	parameters = new HashMap<String, Object>();
	//	parameters.put("threshold", 0.5);
	//	tr.setUp(parameters, (TriggerListener) gwksp);
	//	gwksp.addTrigger(tr);
		
	}//method

}//class
