package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateCoalitionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualCoaltionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoBroadcastOccurringTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoCoalitionArrivingTrigger;

public class GlobalWorkspaceInitalizer implements Initializer{

	private GlobalWorkspace globalWksp;
	
	public GlobalWorkspaceInitalizer(GlobalWorkspace g) {
		globalWksp = g;		
	}//method

	public void initModule(Properties lidaProperties) {
		//TODO: set default values and check for exceptions
		int delayNoBroadcast = Integer.parseInt(lidaProperties.getProperty("globalWorkspace.delayNoBroadcast"));
		int delayNoNewCoalition = Integer.parseInt(lidaProperties.getProperty("globalWorkspace.delayNoNewCoalition"));
		double aggregateActivationThreshold = Double.parseDouble(lidaProperties.getProperty("globalWorkspace.aggregateActivationThreshold"));
		double individualActivationThreshold = Double.parseDouble(lidaProperties.getProperty("globalWorkspace.individualActivationThreshold"));
		BroadcastTrigger tr;
		Map<String, Object> parameters;
		
		tr = new NoBroadcastOccurringTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "NoBroadcastOccurringTrigger");
		parameters.put("delay", delayNoBroadcast);
		tr.setUp(parameters,  globalWksp);
		globalWksp.addBroadcastTrigger(tr);

		tr = new AggregateCoalitionActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold",aggregateActivationThreshold);
		tr.setUp(parameters,  globalWksp);
		globalWksp.addBroadcastTrigger(tr);

		tr = new NoCoalitionArrivingTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "NoCoalitionArrivingTrigger");
		parameters.put("delay", delayNoNewCoalition);
		tr.setUp(parameters,  globalWksp);
		globalWksp.addBroadcastTrigger(tr);

		tr = new IndividualCoaltionActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", individualActivationThreshold);
		tr.setUp(parameters,  globalWksp);
		globalWksp.addBroadcastTrigger(tr);
	

//		BroadcastTrigger tr;
//		Map<String, Object> parameters;
//		
//		tr = new NoBroadcastOccurringTrigger();
//		parameters = new HashMap<String, Object>();
//		parameters.put("name", "TimeOut");		
//		long broadcastTimeOut = Long.parseLong(p.getProperty("globalWorkspace.timeOut"));
//		parameters.put("delay", broadcastTimeOut); //Individual activation trigger will still dominate.
//		tr.setUp(parameters, (TriggerListener) globalWksp);
//		globalWksp.addBroadcastTrigger(tr);
//		
//		//If there hasn't been a broadcast for delayParameter milliseconds 
//		tr = new NoCoalitionArrivingTrigger();
//		parameters = new HashMap<String, Object>();
//		parameters.put("name", "TimeOutLap");
//		long timeOutLap = Long.parseLong(p.getProperty("globalWorkspace.timeOutLap"));
//		parameters.put("delay", timeOutLap);
//		tr.setUp(parameters, (TriggerListener) globalWksp);
//		globalWksp.addBroadcastTrigger(tr);
//	
//		tr = new AggregateCoalitionActivationTrigger();
//		parameters = new HashMap<String, Object>();
//		double aggActivThresh = Double.parseDouble(p.getProperty("globalWorkspace.aggregateActivationThreshold"));
//		parameters.put("threshold", aggActivThresh);
//		tr.setUp(parameters, (TriggerListener) globalWksp);
//		globalWksp.addBroadcastTrigger(tr);
//	
//		tr = new IndividualCoaltionActivationTrigger();
//		parameters = new HashMap<String, Object>();
//		double individActivThresh = Double.parseDouble(p.getProperty("globalWorkspace.individualActivationThreshold"));
//		parameters.put("threshold", individActivThresh);
//		tr.setUp(parameters, (TriggerListener) globalWksp);
//		globalWksp.addBroadcastTrigger(tr);
	}

}//class
