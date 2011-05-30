/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

public class ExcitationTaskTest{
	
	private PamNode pamNode;
	
	/*
	 * Used to make another excitation call
	 */
	private  MockPAM pam;
	
	/*
	 * For threshold task creation
	 */
	private MockTaskSpawner taskSpawner;
	@Before
	public void setUp() throws Exception {
		pamNode = new PamNodeImpl();
		pam = new MockPAM();
		taskSpawner= new MockTaskSpawner();
	}
	
	@Test
	public void test(){
		pam.setPerceptThreshold(1.0);
		pamNode.setExciteStrategy(new LinearExciteStrategy());
		ExcitationTask excite= new ExcitationTask(pamNode, 0.5, 1, pam, taskSpawner);
		
		excite.call();
		assertTrue(pamNode.getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		assertTrue(pam.testGetSink().getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		assertTrue(TaskStatus.FINISHED == excite.getTaskStatus() );
	 
	}
	@Test
	public void testTaskSpawner(){
		pam.setPerceptThreshold(0.4);
		pamNode.setExciteStrategy(new LinearExciteStrategy());
		ExcitationTask excite= new ExcitationTask(pamNode, 0.5, 1, pam, taskSpawner);
		
		excite.call();
		assertTrue(pamNode.getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		assertTrue(pam.testGetSink().getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		
		Collection<FrameworkTask> tasks= taskSpawner.getRunningTasks(); 
		for(FrameworkTask tsk: tasks){
			assertTrue(tsk instanceof AddLinkToPerceptTask);
		}
		 
		assertTrue(TaskStatus.FINISHED == excite.getTaskStatus() );
	 
	}

}
