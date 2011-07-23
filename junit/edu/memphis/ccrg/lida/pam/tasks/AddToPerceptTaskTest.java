/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.pam.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * @author Ryan J. McCall, Usef Faghihi
 *
 */
public class AddToPerceptTaskTest{
	
	private PamNode nodeA, nodeB;
	private PamLink pl;
	private MockPAM pam;
	
	private static ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		nodeA = (PamNode) factory.getNode("PamNodeImpl");
		nodeB = (PamNode) factory.getNode("PamNodeImpl");
		pl = (PamLink) factory.getLink("PamLinkImpl", nodeA, nodeB, nodeA);
		pam = new MockPAM();
	}
	
	@Test
	public void testAddNodeToPercept(){
		AddNodeToPerceptTask t = new AddNodeToPerceptTask(nodeA, pam);
		assertNull(pam.nodePercept);
		
		t.call();
		
		assertEquals(nodeA, pam.nodePercept);
		assertEquals(TaskStatus.FINISHED, t.getTaskStatus());
	}
	
	@Test
	public void testAddLinkToPercept(){
		AddLinkToPerceptTask t = new AddLinkToPerceptTask(pl, pam);
		assertNull(pam.linkPercept);
		
		t.call();
		
		assertEquals(pl, pam.linkPercept);
		assertEquals(TaskStatus.FINISHED, t.getTaskStatus());
	}
	
	@Test
	public void test2(){
		//Setup
		NodeStructure expectedNS = new NodeStructureImpl("PamNodeImpl", "PamLinkImpl");
		expectedNS.addDefaultNode(nodeA);
		expectedNS.addDefaultNode(nodeB);
		Link expectedLink = expectedNS.addDefaultLink(pl);
		assertNull(pam.nsPercept);
			
		//Code being tested
		AddNodeStructureToPerceptTask t = new AddNodeStructureToPerceptTask(expectedNS, pam);
		t.call();
		
		//Check results
		NodeStructure actualNS = pam.nsPercept;
		assertEquals(2, actualNS.getNodeCount());
		assertEquals(1, actualNS.getLinkCount());
		assertEquals(3, actualNS.getLinkableCount());
				
		Node resultNodeA = actualNS.getNode(nodeA.getId());
		assertEquals(nodeA, resultNodeA);
		assertEquals(nodeA.getId(), resultNodeA.getId());
		assertTrue(resultNodeA instanceof PamNodeImpl);
		
		Node resultNodeB = actualNS.getNode(nodeB.getId());
		assertEquals("" + resultNodeB , nodeB, resultNodeB);
		assertEquals(nodeB.getId(), resultNodeB.getId());
		assertTrue(resultNodeB instanceof PamNodeImpl);

		Link resultLink = (Link) actualNS.getLink(expectedLink.getExtendedId());
		assertEquals(expectedLink, resultLink);
		assertEquals(expectedLink.getExtendedId(), resultLink.getExtendedId());
		assertTrue(resultLink instanceof PamLinkImpl);
	}

}
