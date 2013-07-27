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
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.ns.PamLink;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;
import edu.memphis.ccrg.lida.pam.ns.tasks.AddLinkToPerceptTask;
import edu.memphis.ccrg.lida.pam.ns.tasks.PropagationTask;

/**
 * A propagation task excites a node and a link. The link connects the source of
 * the activation to the node.
 * 
 * @author Ryan J. McCall
 * 
 */
public class PropagationTaskTest {

	private static ElementFactory factory = ElementFactory.getInstance();

	private PamNode source;
	private PamNode sink;
	private PamLink link;

	private double epsilon = 1e-10;

	private MockPAM pam;
	private MockTaskSpawner taskSpawner;

	@Before
	public void setUp() throws Exception {
		source = (PamNode) ElementFactory.getInstance().getNode("PamNodeImpl");
		sink = (PamNode) ElementFactory.getInstance().getNode("PamNodeImpl");
		sink.setActivation(0.0);
		sink.setExciteStrategy(new LinearExciteStrategy());
		link = (PamLink) factory.getLink("PamLinkImpl", source, sink,
				PerceptualAssociativeMemoryNSImpl.NONE);
		link.setActivation(0.0);
		link.setExciteStrategy(new LinearExciteStrategy());

		pam = new MockPAM();
		taskSpawner = new MockTaskSpawner();
		pam.setAssistingTaskSpawner(taskSpawner);
	}

	@Test
	public void testPropagateNotOverThreshold() {
		double perceptThreshold = 1.0;
		pam.setPerceptThreshold(perceptThreshold);

		double sourceActivation = 0.09;
		double linkBLA = 0.08;
		double linkActivation = 0.01;
		double sinkActivation = 0.02;
		link.setActivation(linkActivation);
		link.setBaseLevelActivation(linkBLA);
		sink.setActivation(sinkActivation);

		PropagationTask task = new PropagationTask(1, link, sourceActivation,
				pam);
		task.call();

		assertEquals(sourceActivation, link.getActivation(), epsilon);
		assertEquals(linkBLA * sourceActivation + sinkActivation, sink
				.getActivation(), epsilon);
		assertEquals(TaskStatus.CANCELED, task.getTaskStatus());

		assertEquals(sink, pam.pmNode);
		assertEquals(0, taskSpawner.getTasks().size());
	}

	@Test
	public void testPropagateOverThreshold() {
		double perceptThreshold = 0.5;
		pam.setPerceptThreshold(perceptThreshold);

		double sourceActivation = 0.5;
		double linkActivation = 0.2;
		double linkBLA = 0.1;
		double sinkActivation = 0.45;
		link.setActivation(linkActivation);
		link.setBaseLevelActivation(linkBLA);
		sink.setActivation(sinkActivation);

		PropagationTask excite = new PropagationTask(1, link, sourceActivation,
				pam);
		excite.call();

		assertEquals(sourceActivation, link.getActivation(), epsilon);
		// 0.5*0.1 + .45 = .5
		assertEquals(sourceActivation * linkBLA + sinkActivation, sink
				.getActivation(), epsilon);
		assertEquals(TaskStatus.CANCELED, excite.getTaskStatus());

		assertEquals(sink, pam.pmNode);
		Collection<FrameworkTask> tasks = taskSpawner.getTasks();
		assertEquals(1, tasks.size());
		for (FrameworkTask tsk : tasks) {
			assertTrue(tsk instanceof AddLinkToPerceptTask);
		}

	}

}