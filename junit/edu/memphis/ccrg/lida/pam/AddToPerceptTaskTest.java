/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.pam;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask;

/**
 * @author Siminder Kaur
 *
 */
public class AddToPerceptTaskTest {
	
	PamNode pamNode;
	PerceptualAssociativeMemory pam;
	AddToPerceptTask addToTask;

	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		pamNode = new PamNodeImpl();
		pam = new PerceptualAssociativeMemoryImpl();
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask#runThisLidaTask()}.
	 */
	@Test
	public void testRunThisLidaTask() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask#AddToPerceptTask(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory)}.
	 */
	@Test
	public void testAddToPerceptTaskPamNodePerceptualAssociativeMemory() {
		addToTask = new AddToPerceptTask(pamNode, pam);
		fail("Not yet implemented"); // TODO
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask#AddToPerceptTask(edu.memphis.ccrg.lida.framework.shared.NodeStructure, edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory)}.
	 */
	@Test
	public void testAddToPerceptTaskNodeStructurePerceptualAssociativeMemory() {
		fail("Not yet implemented"); // TODO
	}

}
