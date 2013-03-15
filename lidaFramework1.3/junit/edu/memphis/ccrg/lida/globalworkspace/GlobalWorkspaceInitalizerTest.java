/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateCoalitionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualCoaltionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoBroadcastOccurringTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoCoalitionArrivingTrigger;

/**
 * This is a JUnit class which can be used to test methods of the
 * GlobalWorkspaceInitalizer class
 * 
 * @author Siminder Kaur
 */
public class GlobalWorkspaceInitalizerTest {

	private Map<String, Object> params;
	private MockGlobalWorkspaceImpl globalWksp;
	private Agent lida;
	private GlobalWorkspaceInitializer initializer = new GlobalWorkspaceInitializer();

	@Before
	public void setUp() throws Exception {
		params = new HashMap<String, Object>();
		globalWksp = new MockGlobalWorkspaceImpl();
	}

	@Test
	public void testInitModule() {
		params.put("globalWorkspace.delayNoBroadcast", 1111);
		params.put("globalWorkspace.aggregateActivationThreshold", 0.9999);
		params.put("globalWorkspace.delayNoNewCoalition", 555);
		params.put("globalWorkspace.individualActivationThreshold", 0.3333);

		initializer.initModule(globalWksp, lida, params);

		BroadcastTrigger trigger = globalWksp.triggers.get(0);
		assertTrue(trigger instanceof NoBroadcastOccurringTrigger);
		assertEquals(1111, ((NoBroadcastOccurringTrigger) trigger).getDelay());

		trigger = globalWksp.triggers.get(1);
		assertTrue(trigger instanceof AggregateCoalitionActivationTrigger);
		assertEquals(0.9999, ((AggregateCoalitionActivationTrigger) trigger)
				.getThreshold(), 0.00001);

		trigger = globalWksp.triggers.get(2);
		assertTrue(trigger instanceof NoCoalitionArrivingTrigger);
		assertEquals(555, ((NoCoalitionArrivingTrigger) trigger).getDelay());

		trigger = globalWksp.triggers.get(3);
		assertTrue(trigger instanceof IndividualCoaltionActivationTrigger);
		assertEquals(0.3333, ((IndividualCoaltionActivationTrigger) trigger)
				.getThreshold(), 0.00001);
	}

}
