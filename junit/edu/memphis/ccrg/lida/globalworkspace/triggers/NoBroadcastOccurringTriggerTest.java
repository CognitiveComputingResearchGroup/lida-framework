/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * This is a JUnit class which can be used to test methods of the
 * NoBroadcastOccurringTrigger class
 * 
 * @author Siminder Kaur
 */

public class NoBroadcastOccurringTriggerTest {

	private NoBroadcastOccurringTrigger trigger;
	private Map<String, Object> parameters;
	private GlobalWorkspace gw;
	private MockTaskSpawner ts;

	@Before
	public void setUp() throws Exception {
		ts = new MockTaskSpawner();

		trigger = new NoBroadcastOccurringTrigger();
		gw = new MockGlobalWorkspaceImpl();
		parameters = new HashMap<String, Object>();
	}

	@Test
	public void testInit() {
		gw.setAssistingTaskSpawner(ts);
		parameters.put("name", "whatever");
		parameters.put("delay", 567);
		trigger.init(parameters, gw);

		trigger.start();

		TriggerTask t = (TriggerTask) ts.tasks.get(0);
		assertEquals(1, ts.tasks.size());
		assertEquals(567, t.getTicksPerRun());
	}

	@Test
	public void testStart() {
		gw.setAssistingTaskSpawner(ts);
		parameters.put("name", "StartTask");
		parameters.put("delay", 20);
		trigger.init(parameters, gw);

		assertEquals(0, ts.tasks.size());
		trigger.start();

		TriggerTask t = (TriggerTask) ts.tasks.get(0);
		assertEquals(1, ts.tasks.size());
		assertEquals(20, t.getTicksPerRun());
	}

	@Test
	public void testReset() {
		gw.setAssistingTaskSpawner(ts);
		parameters.put("name", "StartTask");
		parameters.put("delay", 20);
		trigger.init(parameters, gw);

		assertEquals(0, ts.tasks.size());
		trigger.start();

		FrameworkTask t = ts.tasks.get(0);
		assertEquals(1, ts.tasks.size());
		trigger.reset();
		assertEquals(1, ts.tasks.size());
		assertNotSame(t.getTaskId(), ts.tasks.get(0));
	}
}
