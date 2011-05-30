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
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * @author Ryan J. McCall, Usef Faghihi
 *
 */
public class AddToPerceptTaskTest{
	
	private PamNode nodeA, nodeB;
	private MockPAM pam;
	
	private static ElementFactory factory = ElementFactory.getInstance();
	
	@BeforeClass
	public static void setUpBeforeClass(){
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}

	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		nodeA = (PamNode) factory.getNode("PamNodeImpl");
		nodeB = (PamNode) factory.getNode("PamNodeImpl");
		pam = new MockPAM();
	}
	
	//test adding a single node
	@Test
	public void test1(){
		AddNodeToPerceptTask t = new AddNodeToPerceptTask(nodeA, pam);
		t.call();
		
		NodeStructure result = pam.getCurrentTestPercept();
		Node actual = result.getNode(nodeA.getId());
		assertEquals(nodeA, actual);
		assertEquals(nodeA.getId(), actual.getId());
		assertEquals(nodeA.getClass().getSimpleName(), actual.getClass().getSimpleName());
	}
	@Test
	public void test2(){
		//Setup
		NodeStructure expectedNS = new NodeStructureImpl("PamNodeImpl", "PamLinkImpl");
		expectedNS.addDefaultNode(nodeA);
		expectedNS.addDefaultNode(nodeB);
		Link expectedLink = expectedNS.addDefaultLink(nodeA.getId(), nodeB.getId(), PerceptualAssociativeMemoryImpl.NONE, 1.0, 0.0);
			
		//Code being tested
		AddLinkToPerceptTask t = new AddLinkToPerceptTask((PamLink) expectedLink, pam);
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
