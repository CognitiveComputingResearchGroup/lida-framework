package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.mockclasses.ActionSelectionImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

public class NoBehaviorArrivingTriggerTest {
	
	Set<Scheme> behaviors;
	LidaTaskManager tm;
	NoBehaviorArrivingTrigger trigger;
	Map<String, Object> parameters;
	ActionSelection as;
	ActionSelectionDriver asd;
	Scheme schemeA,schemeB;
	

	@Before
	public void setUp() throws Exception {		
		
		schemeA = new SchemeImpl("Scheme1",1,1);
		schemeB = new SchemeImpl("Scheme2",2,2);
		behaviors = new HashSet<Scheme>();
		
		schemeA.setActivation(0.8);
		schemeB.setActivation(0.2);
				
		behaviors.add(schemeA);
		behaviors.add(schemeB);
		
		tm = new LidaTaskManager(200,50);
		
		trigger = new NoBehaviorArrivingTrigger();
		as = new ActionSelectionImpl();
		asd = new ActionSelectionDriver();
		parameters = new HashMap<String, Object>();
		
	}

	@Test
	public void testCheckForTriggerSetOfSchemeDouble() {
		//trigger.setLidaTaskManager(tm);
		asd.setTaskManager(tm);
		parameters.put("name", "abc");	
		parameters.put("delay", 15);	
		trigger.setUp(parameters, as,asd);
		
		System.out.println("Trigger started with delay of 15 ticks.");
		
		trigger.checkForTrigger(behaviors, 1.0);
		tm.resumeSpawnedTasks();
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}	
	}

}
