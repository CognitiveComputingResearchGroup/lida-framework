/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.ns.tasks.AddNodeToPerceptTask;
import edu.memphis.ccrg.lida.pam.ns.tasks.ExcitationTask;

public class ExcitationTaskTest {

	private PamNode pamNode;

	private MockPAM pam;

	private MockTaskSpawner taskSpawner;

	@Before
	public void setUp() throws Exception {
		pamNode = new PamNodeImpl();
		pamNode.setActivation(0.0);
		pam = new MockPAM();
		taskSpawner = new MockTaskSpawner();
		pam.setAssistingTaskSpawner(taskSpawner);
	}

	private final double epsilon = 1e-10;

	@Test
	public void testExciteNotOverThreshold() {
		pam.setPerceptThreshold(1.0);
		pamNode.setExciteStrategy(new LinearExciteStrategy());
		ExcitationTask excite = new ExcitationTask(1, pamNode, 0.5, pam);

		excite.call();

		assertEquals(pamNode.getActivation(), 0.5, epsilon);
		assertEquals(pam.pmNode.getActivation(), 0.5, epsilon);
		assertEquals(0, taskSpawner.getTasks().size());
		assertEquals(TaskStatus.CANCELED, excite.getTaskStatus());

	}

	@Test
	public void testExciteOverThreshold() {
		pam.setPerceptThreshold(0.4);
		pamNode.setExciteStrategy(new LinearExciteStrategy());
		ExcitationTask excite = new ExcitationTask(1, pamNode, 0.5, pam);

		excite.call();

		assertEquals(pamNode.getActivation(), 0.5, epsilon);
		assertEquals(pam.pmNode.getActivation(), 0.5, epsilon);

		Collection<FrameworkTask> tasks = taskSpawner.getTasks();
		assertEquals(1, tasks.size());
		for (FrameworkTask tsk : tasks) {
			assertTrue(tsk instanceof AddNodeToPerceptTask);
		}

		assertEquals(TaskStatus.CANCELED, excite.getTaskStatus());
	}

}
