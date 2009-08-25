package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutLapTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

public class GlobalWorkspaceInitalizer implements Initializer{

	private GlobalWorkspace globalWksp;
	
	public GlobalWorkspaceInitalizer(GlobalWorkspace g) {
		globalWksp = g;		
	}//method

	public void initModule(Properties p) {
		BroadcastTrigger tr;
		Map<String, Object> parameters;
		
		tr = new TimeOutTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOut");		
		long broadcastTimeOut = Long.parseLong(p.getProperty("globalWorkspace.timeOut"));
		parameters.put("delay", broadcastTimeOut); //Individual activation trigger will still dominate.
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
		
		//If there hasn't been a broadcast for delayParameter milliseconds 
		tr = new TimeOutLapTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOutLap");
		long timeOutLap = Long.parseLong(p.getProperty("globalWorkspace.timeOutLap"));
		parameters.put("delay", timeOutLap);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	
		tr = new AggregateActivationTrigger();
		parameters = new HashMap<String, Object>();
		double aggActivThresh = Double.parseDouble(p.getProperty("globalWorkspace.aggregateActivationThreshold"));
		parameters.put("threshold", aggActivThresh);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	
		tr = new IndividualActivationTrigger();
		parameters = new HashMap<String, Object>();
		double individActivThresh = Double.parseDouble(p.getProperty("globalWorkspace.individualActivationThreshold"));
		parameters.put("threshold", individActivThresh);
		tr.setUp(parameters, (TriggerListener) globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	}

}//class
