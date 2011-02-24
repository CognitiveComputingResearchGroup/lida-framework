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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.BasicDetector;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;

/**
 * Tests {@link PerceptualAssociativeMemoryImpl}
 * @author Siminder Kaur, Ryan J. McCall
 */
public class PerceptualAssociativeMemoryImplTest extends TestCase{
	
	PerceptualAssociativeMemoryImpl pam;
	NodeStructure nodeStructure;
	PamNodeImpl node1,node2,node3;
	LinearDecayStrategy defaultDecayStrategy ;
	PamLinkImpl link1,link2;
	
	private LidaElementFactory factory = LidaElementFactory.getInstance();
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		pam = new PerceptualAssociativeMemoryImpl();
		nodeStructure = factory.getPamNodeStructure();
		
		node1 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);
		node2 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);
		node3 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);
		defaultDecayStrategy = new LinearDecayStrategy();
		
		link1 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node1, node2, LinkCategoryNode.CHILD);
		link2 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node2, node3, LinkCategoryNode.CHILD);

	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#getModuleName()}.
	 */
	@Test
	public void testGetModuleName() {
		ModuleName name = pam.getModuleName();
		assertEquals("Problem with GetModuleName", ModuleName.PerceptualAssociativeMemory, name);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#decayModule(long)}.
	 * @see LinearDecayStrategy
	 */
	@Test
	public void testDecayModule() {		
		node1.setActivation(1.0);
		node1.setDecayStrategy(defaultDecayStrategy);
		pam.addDefaultNode(node1);	
		pam.decayModule(100);
		assertTrue("Problem with DecayModule",pam.getPamNode(node1.getId()).getTotalActivation() == 0.0);		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#setAssistingTaskSpawner(edu.memphis.ccrg.lida.framework.tasks.TaskSpawner)}.
	 *
	 */
	@Test
	public void testSetTaskSpawner() {
		TaskSpawner taskSpawner = new TaskSpawnerImpl();
		pam.setAssistingTaskSpawner(taskSpawner);
		assertEquals("Problem with SetTaskSpawner", taskSpawner, pam.getAssistingTaskSpawner());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#setPropagationBehavior(edu.memphis.ccrg.lida.pam.PropagationBehavior)}.
	 */
	@Test
	public void testSetPropagationBehavior() {
		PropagationBehavior pb = new UpscalePropagationBehavior();
		pam.setPropagationBehavior(pb);		
		assertEquals("Problem with SetPropagationBehavior", pb, pam.getPropagationBehavior());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addDefaultNodes(java.util.Set)}.
	 */
	@Test
	public void testAddDefaultNodes() {
		Set<PamNode> nodes = new HashSet<PamNode>();
		nodes.add(node2);
		nodes.add(node3);
		pam.addDefaultNode(node1);	
		pam.addDefaultNodes(nodes);
		assertTrue("Problem with AddNodes", pam.containsNode(node1) && pam.containsNode(node2) && pam.containsNode(node3));
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addDefaultLinks(Set)}.
	 */
	@Test
	public void testAddDefaultLinks() {
		Set<PamLink> links = new HashSet<PamLink>();
		
		pam.addDefaultNode(node1);	
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);
		links.add(link1);
		links.add(link2);
		pam.addDefaultLinks(links);
		
		assertTrue("Problem with AddLinks",pam.containsLink(link1) && pam.containsLink(link2));
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addFeatureDetector(edu.memphis.ccrg.lida.pam.tasks.FeatureDetector)}.
	 */
	@Test
	public void testAddFeatureDetector() {	
		FeatureDetector detector = new BasicDetector(node1, null, pam);
		pam.setAssistingTaskSpawner(new MockTaskSpawner());
		pam.addFeatureDetector(detector);
		assertTrue("Problem with AddFeatureDetector", pam.getAssistingTaskSpawner().containsTask(detector));
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addNewNode(java.lang.String)}.
	 */
	@Test
	public void testAddNewNode() {
		PamNodeImpl pn = (PamNodeImpl) pam.addNewNode("BOB");
		assertTrue(pn != null);
		assertTrue(pn.getLabel().equals("BOB"));
		assertTrue(pam.containsNode(pn));
		Node other = pam.getPamNode(pn.getId());
		assertTrue(other.equals(pn));
	}
	
	public void testAddNewNode2(){
		PamNode pn = pam.addNewNode(PamNodeImpl.factoryName, "BOB");
		assertTrue(pn != null);
		assertTrue(pn instanceof PamNodeImpl);
		assertTrue(pn.getLabel().equals("BOB"));	
		
		Node other = pam.getPamNode(pn.getId());
		assertTrue(other.equals(pn));
	}
	
	@Test
	public void testIsOverPerceptThreshold() {
		pam.setPerceptThreshold(0.5);
		node1.setBaseLevelActivation(0.4);
		node1.setActivation(0.2);
		assertTrue(pam.isOverPerceptThreshold(node1));
		
		pam.setPerceptThreshold(0.61);
		assertTrue(!pam.isOverPerceptThreshold(node1));

		pam.setPerceptThreshold(24839.61);
		assertTrue(!pam.isOverPerceptThreshold(node1));
	}
	
	public void testContainsLink(){
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		node1.setLearnableRemovalThreshold(0.0);
		node1.setActivation(0.0);
		node1.setBaseLevelActivation(0.0);
		pam.decayModule(1);
		assertTrue(pam.containsNode(node1)+"", !pam.containsNode(node1));
	}

}
