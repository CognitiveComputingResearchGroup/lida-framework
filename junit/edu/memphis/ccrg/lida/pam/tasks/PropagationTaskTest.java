/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
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
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * A propagation task excites a node and a link.  
 * The link connects the source of the activation to the node.
 * @author Ryan J McCall, Usef
 *
 */

public class PropagationTaskTest{
	
	private static ElementFactory factory = ElementFactory.getInstance();
	
	private PamNode source;
	private PamNode sink;
	private PamLink link;
	
	private double epsilon = 1e-10;
	
	private MockPAM pam;
	private MockTaskSpawner taskSpawner;
	
	@Before
	public void setUp() throws Exception {
		source =  (PamNode) ElementFactory.getInstance().getNode("PamNodeImpl");
		sink   =  (PamNode) ElementFactory.getInstance().getNode("PamNodeImpl");
		sink.setActivation(0.0);
		sink.setExciteStrategy(new LinearExciteStrategy());
		link  = (PamLink) factory.getLink("PamLinkImpl", source, sink, PerceptualAssociativeMemoryImpl.NONE);
		link.setActivation(0.0);
		link.setExciteStrategy(new LinearExciteStrategy());
		 
		pam = new MockPAM();
		taskSpawner= new MockTaskSpawner();
		pam.setAssistingTaskSpawner(taskSpawner);
	}
	
	@Test
	public void testPropagateNotOverThreshold(){
		double perceptThreshold = 1.0;
		pam.setPerceptThreshold(perceptThreshold);
		double upscaleFactor = 0.5;
		pam.setUpscaleFactor(upscaleFactor);
		
		double linkActivation = 0.1;
		double sourceActivation = 0.1;
		double sinkActivation = 0.1;
		link.setActivation(linkActivation);
		sink.setActivation(sinkActivation);
		assertEquals(0.1, link.getActivation(), epsilon); 
		
		PropagationTask task= new PropagationTask(1, link, sourceActivation, pam);
		task.call();
	 
		assertEquals(0.2, link.getActivation(), epsilon);
		assertEquals(0.2*upscaleFactor + 0.1, sink.getActivation(), epsilon);
		assertEquals(TaskStatus.FINISHED, task.getTaskStatus());
	 
		assertEquals(sink, pam.pmNode);
		assertEquals(0, taskSpawner.getRunningTasks().size());
	}
	@Test
	public void testPropagateOverThreshold(){
		double perceptThreshold = 0.5;
		pam.setPerceptThreshold(perceptThreshold);
		double upscaleFactor = 0.5;
		pam.setUpscaleFactor(upscaleFactor);
		
		double linkActivation = 0.2;
		double sourceActivation = 0.6;
		double sinkActivation = 0.1;
		link.setActivation(linkActivation);
		sink.setActivation(sinkActivation);
		
		PropagationTask excite= new PropagationTask(1, link, sourceActivation, pam);
		excite.call();
	 
		assertEquals(sourceActivation + linkActivation, link.getActivation(), epsilon);
		assertEquals((sourceActivation + linkActivation)*upscaleFactor + sinkActivation, sink.getActivation(), epsilon);
		assertEquals(TaskStatus.FINISHED, excite.getTaskStatus());
	 
		assertEquals(sink, pam.pmNode);
		Collection<FrameworkTask> tasks = taskSpawner.getRunningTasks();
		assertEquals(1, tasks.size());
		for(FrameworkTask tsk: tasks){
			assertTrue(tsk instanceof AddLinkToPerceptTask);
		}
	 
	}
 
}