/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;
 
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.actionselection.triggers.AggregateBehaviorActivationTrigger;
import edu.memphis.ccrg.lida.actionselection.triggers.IndividualBehaviorActivationTrigger;
import edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class ActionSelectionInitalizer implements Initializer{

	private static Logger logger = Logger.getLogger(ActionSelectionInitalizer.class.getCanonicalName());
	
	private static final Integer DEFAULT_DELAY_NO_ACTION_SELECTION = 100;
	private static final Integer DEFAULT_DELAY_NO_NEW_BEHAVIOR = 50;
	private static final Double DEFAULT_AGGREGATE_ACT_THRESHOLD = 0.8;
	private static final Double DEFAULT_INDIVIDUAL_ACT_THRESHOLD = 0.5;

	public ActionSelectionInitalizer() {
	}//method

	@Override
	public void initModule(Initializable module,Lida lida,Map<String,?> params){		
		
		ActionSelection actionSelection=(ActionSelection)module;
		Integer delayNoActionSelection = (Integer) params.get("actionSelection.delayNoBroadcast");
		if (delayNoActionSelection==null){
			delayNoActionSelection=DEFAULT_DELAY_NO_ACTION_SELECTION;
			logger.log(Level.WARNING, "Delay no Action Selection could not be read, using default", LidaTaskManager.getActualTick());
		}
		
		Integer delayNoNewBehavior = (Integer) params.get("actionSelection.delayNoNewBehavior");
		if (delayNoNewBehavior==null){
			delayNoNewBehavior=DEFAULT_DELAY_NO_NEW_BEHAVIOR;
			logger.log(Level.WARNING, "Delay no new Behavior could not be read, using default", LidaTaskManager.getActualTick());
		}

		Double aggregateActivationThreshold = (Double) params.get("actionSelection.aggregateActivationThreshold");
		if (aggregateActivationThreshold==null){
			aggregateActivationThreshold=DEFAULT_AGGREGATE_ACT_THRESHOLD;
			logger.log(Level.WARNING, "aggregate activation threshold could not be read, using default", LidaTaskManager.getActualTick());
		}
		
		Double individualActivationThreshold = (Double) params.get("actionSelection.individualActivationThreshold");
		if (individualActivationThreshold==null){
			individualActivationThreshold=DEFAULT_INDIVIDUAL_ACT_THRESHOLD;
			logger.log(Level.WARNING, "individual activation threshold could not be read, using default", LidaTaskManager.getActualTick());
		}
		
		ActionSelectionTrigger tr;
		Map<String, Object> parameters;
		
		tr = new NoActionSelectionOccurringTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "NoActionSelectionOccurringTrigger");
		parameters.put("delay", delayNoActionSelection);
		tr.setUp(parameters,  actionSelection);
		actionSelection.addActionSelectionTrigger(tr);

		tr = new AggregateBehaviorActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold",aggregateActivationThreshold);
		tr.setUp(parameters,  actionSelection);
		actionSelection.addActionSelectionTrigger(tr);

		tr = new NoActionSelectionOccurringTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "NoCoalitionArrivingTrigger");
		parameters.put("delay", delayNoNewBehavior);
		tr.setUp(parameters,  actionSelection);
		actionSelection.addActionSelectionTrigger(tr);

		tr = new IndividualBehaviorActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", individualActivationThreshold);
		tr.setUp(parameters,  actionSelection);
		actionSelection.addActionSelectionTrigger(tr);
		}

}//class
