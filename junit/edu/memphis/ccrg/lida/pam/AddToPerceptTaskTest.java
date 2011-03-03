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

import junit.framework.TestCase;

import org.junit.Before;

import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask;

/**
 * @author Ryan McCall, Usef Faghihi
 *
 */
public class AddToPerceptTaskTest extends TestCase{
	
	private PamNode nodeA, nodeB;
	private MockPAM pam;
	
	private LidaElementFactory factory = LidaElementFactory.getInstance();

	/**
	 * @throws java.lang.Exception e
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		nodeA = (PamNode) factory.getNode(PamNodeImpl.factoryName);
		nodeB = (PamNode) factory.getNode(PamNodeImpl.factoryName);
		pam = new MockPAM();
	}
	
	//test adding a single node
	public void test1(){
		AddToPerceptTask t = new AddToPerceptTask(nodeA, pam);
		t.call();
		
		NodeStructure result = pam.getCurrentTestPercept();
		Node actual = result.getNode(nodeA.getId());
		assertEquals(nodeA, actual);
		assertEquals(nodeA.getId(), actual.getId());
		assertEquals(nodeA.getClass().getSimpleName(), actual.getClass().getSimpleName());
	}
	
	public void test2(){
		//Setup
		NodeStructure expectedNS = factory.getPamNodeStructure();
		expectedNS.addDefaultNode(nodeA);
		expectedNS.addDefaultNode(nodeB);
		Link expectedLink = expectedNS.addLink(nodeA.getId(), nodeB.getId(), LinkCategoryNode.NONE, 1.0, 0.0);
			
		//Code being tested
		AddToPerceptTask t = new AddToPerceptTask(expectedNS, pam);
		t.call();
		
		//Check results
		NodeStructure actualNS = pam.getCurrentTestPercept();
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
//	
//	private void assertSimpleName(Object expected, Object result){
//		assertEquals(expected.getClass().getSimpleName(), result.getClass().getSimpleName());
//	}
//	private void assertCanonicalName(Object expected, Object result){
//		assertEquals(expected.getClass().getCanonicalName(), result.getClass().getCanonicalName());
//	}


}
