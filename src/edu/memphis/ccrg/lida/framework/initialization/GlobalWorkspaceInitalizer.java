package edu.memphis.ccrg.lida.framework.initialization;
 
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateCoalitionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualCoaltionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoBroadcastOccurringTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoCoalitionArrivingTrigger;

public class GlobalWorkspaceInitalizer implements Initializer{

	
	public GlobalWorkspaceInitalizer() {
	}//method

	public void initModule(Initializable module,Lida lida,Properties lidaProperties)  {
		//TODO: set default values and check for exceptions
		
		GlobalWorkspace globalWksp=(GlobalWorkspace)module;
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
		}

}//class
