/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;
 
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateCoalitionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualCoaltionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoBroadcastOccurringTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoCoalitionArrivingTrigger;

public class GlobalWorkspaceInitalizer implements Initializer{

	private static final Logger logger = Logger.getLogger(GlobalWorkspaceInitalizer.class.getCanonicalName());
	
	private static final Integer DEFAULT_DELAY_NO_BROADCAST = 100;
	private static final Integer DEFAULT_DELAY_NO_NEW_COALITION = 50;
	private static final Double DEFAULT_AGGREGATE_ACT_THRESHOLD = 0.8;
	private static final Double DEFAULT_INDIVIDUAL_ACT_THRESHOLD = 0.5;

	public GlobalWorkspaceInitalizer() {
	}//method

	@Override
	public void initModule(Initializable module,Lida lida, Map<String, ?> params){		
		
		GlobalWorkspace globalWksp=(GlobalWorkspace)module;
		Integer delayNoBroadcast = (Integer) params.get("globalWorkspace.delayNoBroadcast");
		if (delayNoBroadcast==null){
			delayNoBroadcast=DEFAULT_DELAY_NO_BROADCAST;
			logger.log(Level.WARNING, "Delay no broadcast could not be read, using default", LidaTaskManager.getCurrentTick());
		}
		
		Integer delayNoNewCoalition = (Integer) params.get("globalWorkspace.delayNoNewCoalition");
		if (delayNoNewCoalition==null){
			delayNoNewCoalition=DEFAULT_DELAY_NO_NEW_COALITION;
			logger.log(Level.WARNING, "Delay no new coalition could not be read, using default", LidaTaskManager.getCurrentTick());
		}

		Double aggregateActivationThreshold = (Double) params.get("globalWorkspace.aggregateActivationThreshold");
		if (aggregateActivationThreshold==null){
			aggregateActivationThreshold=DEFAULT_AGGREGATE_ACT_THRESHOLD;
			logger.log(Level.WARNING, "aggregate activation threshold could not be read, using default", LidaTaskManager.getCurrentTick());
		}
		
		Double individualActivationThreshold = (Double) params.get("globalWorkspace.individualActivationThreshold");
		if (individualActivationThreshold==null){
			individualActivationThreshold=DEFAULT_INDIVIDUAL_ACT_THRESHOLD;
			logger.log(Level.WARNING, "individual activation threshold could not be read, using default", LidaTaskManager.getCurrentTick());
		}
		
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
