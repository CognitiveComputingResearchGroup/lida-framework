/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
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
	private PamNode source;
	private PamNode sink;
	private PamLink link;
	
	/*
	 * Used to make another excitation call
	 */
	private MockPAM pam;
	private static ElementFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}
	
	/*
	 * For threshold task creation
	 */
	private MockTaskSpawner taskSpawner;
	@Before
	public void setUp() throws Exception {
		source =  (PamNode) ElementFactory.getInstance().getNode("PamNodeImpl");
		sink   =  (PamNode) ElementFactory.getInstance().getNode("PamNodeImpl");
		link  = (PamLink) factory.getLink("PamLinkImpl", source, sink, PerceptualAssociativeMemoryImpl.NONE);
		 
		pam = new MockPAM();
		taskSpawner= new MockTaskSpawner();
	}
	@Test
	public void test(){
		double perceptThreshold = 1.0;
		double linkActivation = 0.1;
		double sourceActivation = 0.1;
		double sinkActivation = 0.1;
		pam.setPerceptThreshold(perceptThreshold);
		link.setActivation(linkActivation);
		source.setActivation(sourceActivation);
		sink.setActivation(sinkActivation);
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		PropagationTask excite= new PropagationTask(source, link, sink, 0.1, pam, taskSpawner );
		excite.call();
	 
		assertTrue(link.getActivation() > 0.1);
		assertTrue(sink.getActivation() > 0.1);
		assertTrue(source.getActivation() == 0.1);
		assertTrue(TaskStatus.FINISHED == excite.getTaskStatus() );
	 
		assertEquals(sink, pam.testGetSink());
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		assertFalse(pam.isOverPerceptThreshold(link));
		assertFalse(pam.isOverPerceptThreshold(sink));
		 
	 
	}
	@Test
	public void test2(){
		double perceptThreshold = 0.15;
		double linkActivation = 0.1;
		double sourceActivation = 0.1;
		double sinkActivation = 0.1;
		pam.setPerceptThreshold(perceptThreshold);
		link.setActivation(linkActivation);
		source.setActivation(sourceActivation);
		sink.setActivation(sinkActivation);
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		PropagationTask excite= new PropagationTask(source, link, sink, 0.5, pam, taskSpawner );
		excite.call();
	 
		assertTrue(link.getActivation() > 0.1);
		assertTrue(sink.getActivation() > 0.1);
		assertTrue(source.getActivation() == 0.1);
		assertTrue(TaskStatus.FINISHED == excite.getTaskStatus() );
	 
		assertEquals(sink, pam.testGetSink());
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		assertTrue(pam.isOverPerceptThreshold(link));
		assertTrue(pam.isOverPerceptThreshold(sink));
		assertTrue(taskSpawner.getRunningTasks().size() != 0);
		 
	 
	}
 
}