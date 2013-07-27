/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * This is a JUnit class which can be used to test methods of the
 * AggregateCoalitionActivationTrigger class
 * 
 * @author Siminder Kaur
 */

public class AggregateCoalitionActivationTriggerTest {

	private Set<Coalition> coalitions;
	private Map<String, Object> parameters;
	private MockGlobalWorkspaceImpl gw;
	private Coalition coalition, coalition2, coalition3;
	private AggregateCoalitionActivationTrigger trigger;

	@Before
	public void setUp() throws Exception {
		trigger = new AggregateCoalitionActivationTrigger();
		coalitions = new HashSet<Coalition>();
		parameters = new HashMap<String, Object>();
		gw = new MockGlobalWorkspaceImpl();
		NodeStructure ns = new NodeStructureImpl();
		AttentionCodelet codelet = new AttentionCodeletImpl() {
			@Override
			public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
				return soughtContent;
			}

			@Override
			public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
				return false;
			}

			@Override
			public String toString() {
				return null;
			}
		};
		coalition = new CoalitionImpl(ns, codelet);
		coalition.setActivation(0.1);
		coalition2 = new CoalitionImpl(ns, codelet);
		coalition2.setActivation(0.2);
		coalition3 = new CoalitionImpl(ns, codelet);
		coalition3.setActivation(0.3);

		coalitions.add(coalition);
		coalitions.add(coalition2);
		coalitions.add(coalition3);
	}

	@Test
	public void testCheckForTriggerConditionSetOfCoalition() {
		parameters.put("threshold", 0.7);
		trigger.init(parameters, gw);

		trigger.checkForTriggerCondition(coalitions);

		assertNull(gw.trigger);
	}

	@Test
	public void testCheckForTriggerConditionSetOfCoalition1() {
		parameters.put("threshold", 0.5);
		trigger.init(parameters, gw);

		trigger.checkForTriggerCondition(coalitions);

		assertEquals(trigger, gw.trigger);
	}

	@Test
	public void testCheckForTriggerConditionSetOfCoalition2() {
		parameters.put("threshold", -0.5);
		trigger.init(parameters, gw);

		trigger.checkForTriggerCondition(coalitions);

		assertEquals(trigger, gw.trigger);
	}

}
