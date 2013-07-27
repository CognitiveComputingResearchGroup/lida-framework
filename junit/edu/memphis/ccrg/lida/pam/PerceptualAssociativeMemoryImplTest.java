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
package edu.memphis.ccrg.lida.pam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.mockclasses.ExecutingMockTaskSpawner;
import edu.memphis.ccrg.lida.framework.mockclasses.MockDetectionAlgorithm;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.ns.PamLink;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;
import edu.memphis.ccrg.lida.pam.ns.PropagationStrategy;
import edu.memphis.ccrg.lida.pam.ns.UpscalePropagationStrategy;
import edu.memphis.ccrg.lida.pam.ns.tasks.DetectionAlgorithm;

/**
 * Tests {@link PerceptualAssociativeMemoryNSImpl}
 * 
 * @author Ryan J. McCall
 * @author Siminder Kaur
 */
public class PerceptualAssociativeMemoryImplTest {

	private static ElementFactory factory;
	private PerceptualAssociativeMemoryNSImpl pam;
	private NodeStructure nodeStructure;
	private PamNode node1, node2, node3;
	private DecayStrategy decayStrategy;
	private ExciteStrategy exciteStrat;
	private TotalActivationStrategy tas;
	private PamLink link1, link2;

	@BeforeClass
	public static void setUpBeforeClass() {
		factory = ElementFactory.getInstance();
		Properties prop = ConfigUtils
				.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		FactoriesDataXmlLoader.loadFactoriesData(prop);
	}

	@Before
	public void setUp() throws Exception {
		pam = new PerceptualAssociativeMemoryNSImpl();
		nodeStructure = new NodeStructureImpl("PamNodeImpl", "PamLinkImpl");

		node1 = (PamNodeImpl) factory.getNode("PamNodeImpl", "Node 1");
		node2 = (PamNodeImpl) factory.getNode("PamNodeImpl", "Node 2");
		node3 = (PamNodeImpl) factory.getNode("PamNodeImpl", "Node 3");

		decayStrategy = new SigmoidDecayStrategy();
		exciteStrat = new SigmoidExciteStrategy();
		tas = (TotalActivationStrategy) factory
				.getStrategy("DefaultTotalActivation");
		link1 = (PamLinkImpl) factory.getLink("PamLinkImpl", node1, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);
		link2 = (PamLinkImpl) factory.getLink("PamLinkImpl", node2, node3,
				PerceptualAssociativeMemoryNSImpl.NONE);
	}

	@Test
	public void testAddDefaultNode_Label() {
		PamNode n = pam.addDefaultNode("hi");
		assertTrue(n instanceof PamNodeImpl);
		assertEquals("hi", n.getLabel());
		assertEquals("PamNodeImpl", n.getFactoryType());
		assertEquals(0.0, n.getActivation(), epsilon);
		assertEquals(0.0, n.getActivatibleRemovalThreshold(), epsilon);
		assertTrue(pam.containsNode(n));
		assertEquals(n, pam.getNode("hi"));

		PamNode n2 = pam.addDefaultNode("hi");
		assertSame(n, n2);
	}

	@Test
	public void testAddNode_Type_Label() {
		factory.addNodeType("mockPamNode",
				"edu.memphis.ccrg.lida.pam.PamNodeImplSubclass");

		PamNode n = pam.addNode("mockPamNode", "hi");
		assertTrue(n instanceof PamNodeImplSubclass);
		assertEquals("hi", n.getLabel());
		assertEquals("mockPamNode", n.getFactoryType());
		assertEquals(0.0, n.getActivation(), epsilon);
		assertEquals(0.0, n.getActivatibleRemovalThreshold(), epsilon);
		assertTrue(pam.containsNode(n));
		assertEquals(n, pam.getNode("hi"));

		int nodeCount = pam.getNodes().size();
		// try to add another node of different type
		PamNode n2 = pam.addNode("PamNodeImpl", "hi");
		assertSame(n, n2);
		assertEquals(nodeCount, pam.getNodes().size());
	}

	@Test
	public void testAddNode_Type_Label2() {
		PamNode n = pam.addNode(null, "hi");
		assertNull(n);

		n = pam.addNode("PamNodeImpl", null);
		assertNull(n);

		n = pam.addNode("Bad type", "hi");
		assertNull(n);
	}

	@Test
	public void addDefaultLink_Node_Linkable_LinkCategory() {
		pam.addLinkCategory(node3);

		PamNode newNode1 = pam.addDefaultNode("1");
		PamNode newNode2 = pam.addDefaultNode("2");
		PamLink pl = pam.addDefaultLink(newNode1, newNode2, node3);

		assertTrue(pl instanceof PamLinkImpl);
		assertEquals("PamLinkImpl", pl.getFactoryType());
		assertEquals(0.0, pl.getActivation(), epsilon);
		assertEquals(0.0, pl.getActivatibleRemovalThreshold(), epsilon);
		assertTrue(pam.containsLink(pl));
		assertEquals(newNode1, pl.getSource());
		assertEquals(newNode2, pl.getSink());
		assertEquals(node3, pl.getCategory());
	}

	@Test
	public void addLink_Type_Node_Linkable_LinkCategory1() {
		PamLink l = pam.addLink("PamLinkImpl", node1, node2, node3);
		assertNull(l);

		PamNode newNode1 = pam.addDefaultNode("1");
		PamNode newNode2 = pam.addDefaultNode("2");
		l = pam.addLink("PamLinkImpl", newNode1, node2, node3);
		assertNull(l);

		l = pam.addLink("PamLinkImpl", newNode1, newNode2, node3);
		assertNull(l);

		pam.addLinkCategory(node3);
		l = pam.addLink("PamLinkImpl", newNode1, newNode2, node3);
		assertNotNull(l);
	}

	@Test
	public void addLink_Type_Node_Linkable_LinkCategory2() {
		PamLink l = pam.addLink("PamLinkImpl", null, null, null);
		assertNull(l);

		PamNode newNode1 = pam.addDefaultNode("1");
		PamNode newNode2 = pam.addDefaultNode("2");
		l = pam.addLink("PamLinkImpl", newNode1, null, null);
		assertNull(l);

		l = pam.addLink("PamLinkImpl", newNode1, newNode2, null);
		assertNull(l);

		pam.addLinkCategory(node3); // link1 not present
		l = pam.addLink("PamLinkImpl", newNode1, link1, node3);
		assertNull(l);

		pam.addLinkCategory(node3);
		l = pam.addLink("LinkImpl", newNode1, newNode2, node3);
		assertNull(l);

		l = pam.addLink("BADTYPE", newNode1, newNode2, node3);
		assertNull(l);
	}

	@Test
	public void addLink_Type_Node_Linkable_LinkCategory3() {
		factory.addLinkType("testPamLink",
				"edu.memphis.ccrg.lida.pam.PamLinkImplSubclass");

		PamNode newNode1 = pam.addDefaultNode("1");
		PamNode newNode2 = pam.addDefaultNode("2");
		pam.addLinkCategory(node3);
		PamLink pl = pam.addLink("testPamLink", newNode1, newNode2, node3);

		assertTrue(pl instanceof PamLinkImplSubclass);
		assertEquals("testPamLink", pl.getFactoryType());
		assertEquals(0.0, pl.getActivation(), epsilon);
		assertEquals(0.0, pl.getActivatibleRemovalThreshold(), epsilon);
		assertTrue(pam.containsLink(pl));
		assertEquals(newNode1, pl.getSource());
		assertEquals(newNode2, pl.getSink());
		assertEquals(node3, pl.getCategory());
	}

	@Test
	public void addLink_Type_Node_Linkable_LinkCategory4() {
		PamNode newNode1 = pam.addDefaultNode("1");
		PamNode newNode2 = pam.addDefaultNode("2");
		pam.addLinkCategory(node3);
		PamLink pl = pam.addLink("PamLinkImpl", newNode1, newNode2, node3);

		assertNotNull(pl);
		assertTrue(pam.containsLink(pl));

		int linkCount = pam.getLinks().size();
		PamLink pl2 = pam.addLink("testPamLink", newNode1, newNode2, node3);
		assertNull(pl2);
		assertEquals(linkCount, pam.getLinks().size());
	}

	@Test
	public void testAddDefaultNode() {
		node2.setActivation(0.987);
		node2.setBaseLevelActivation(0.01);

		PamNode res = pam.addDefaultNode(node2);

		assertEquals(node2.getActivation(), res.getActivation(), epsilon);
		assertEquals(node2.getBaseLevelActivation(), res
				.getBaseLevelActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl#addDefaultNodes(java.util.Set)}
	 * .
	 */
	@Test
	public void testAddDefaultNodes() {
		Set<PamNode> nodes = new HashSet<PamNode>();
		node2.setActivation(0.123);
		node2.setBaseLevelActivation(0.01);

		node3.setActivation(0.456);
		node3.setBaseLevelActivation(0.02);

		nodes.add(node2);
		nodes.add(node3);
		Set<PamNode> results = pam.addDefaultNodes(nodes);
		for (PamNode res : results) {
			assertTrue(nodes.contains(res));
			if (res.equals(node2)) {
				assertEquals(res.getActivation(), node2.getActivation(),
						epsilon);
				assertEquals(res.getBaseLevelActivation(), node2
						.getBaseLevelActivation(), epsilon);
			} else if (res.equals(node3)) {
				assertEquals(res.getActivation(), node3.getActivation(),
						epsilon);
				assertEquals(res.getBaseLevelActivation(), node3
						.getBaseLevelActivation(), epsilon);
			} else {
				assertFalse(true);
			}
		}
		assertTrue(pam.containsNode(node2));
		assertTrue(pam.containsNode(node3));

		assertEquals(2, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void testAddNullNodes() {
		Set<PamNode> results = pam.addDefaultNodes(null);
		assertTrue(results == null);
		assertTrue(pam.getNodes().size() - pam.getLinkCategories().size() == 0);
	}

	@Test
	public void testAddNodeNodeType() {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(factory.getNode());
		nodes.add(factory.getNode());
		pam.addDefaultNodes(nodes);
		assertEquals(2, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void testAddDefNode() {
		Node n = null;
		PamNode res = pam.addDefaultNode(n);
		assertNull(res);
		assertEquals(0, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void testAddDefNode2() {
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node1);
		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void testAddDefNode1() {
		pam.addDefaultNode(node1);
		assertTrue(pam.containsNode(node1));
		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void testAddDefaultLinks() {
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);

		Set<PamLink> links = new HashSet<PamLink>();

		link1.setActivation(0.123);
		link1.setBaseLevelActivation(0.01);

		links.add(link1);
		links.add(link2);

		Set<PamLink> storedLinks = pam.addDefaultLinks(links);
		for (PamLink pLink : storedLinks) {
			assertTrue(links.contains(pLink));
			if (pLink.equals(link1)) {
				assertEquals(link1.getActivation(), pLink.getActivation(),
						epsilon);
				assertEquals(link1.getBaseLevelActivation(), pLink
						.getBaseLevelActivation(), epsilon);
			}
		}
		assertTrue(pam.containsLink(link1));
		assertTrue(pam.containsLink(link2));
		assertEquals(2, pam.getLinks().size());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl#addDetectionAlgorithm(edu.memphis.ccrg.lida.pam.ns.tasks.DetectionAlgorithm)}
	 * .
	 */
	@Test
	public void testAddFeatureDetector() {
		DetectionAlgorithm detector = new MockDetectionAlgorithm();
		detector.setPamLinkable(node1);
		pam.setAssistingTaskSpawner(new MockTaskSpawner());
		pam.addDetectionAlgorithm(detector);
		assertTrue(pam.getAssistingTaskSpawner().containsTask(detector));
		assertEquals(1, pam.getAssistingTaskSpawner().getTasks().size());
	}

	@Test
	public void testDetectionAlgorithm1() {
		DetectionAlgorithm detector = new MockDetectionAlgorithm();
		detector.setPamLinkable(node1);
		pam.setAssistingTaskSpawner(new MockTaskSpawner());
		pam.addDetectionAlgorithm(detector);
		pam.addDetectionAlgorithm(detector);
		assertTrue(pam.getAssistingTaskSpawner().containsTask(detector));
		assertEquals(2, pam.getAssistingTaskSpawner().getTasks().size());
	}

	@Test
	public void testAddPamListener() {
		MockPamListener pl = new MockPamListener();
		pam.addPamListener(pl);
		pam.addToPercept(new NodeStructureImpl());

		NodeStructure ns = pl.ns;
		assertEquals(0, ns.getNodeCount());
		assertEquals(0, ns.getLinkCount());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl#setPropagationStrategy(edu.memphis.ccrg.lida.pam.ns.PropagationStrategy)}
	 * .
	 */
	@Test
	public void testSetPropagationStrategy() {
		PropagationStrategy ps = new UpscalePropagationStrategy();
		pam.setPropagationStrategy(ps);
		assertEquals(ps, pam.getPropagationStrategy());
	}

	private final double epsilon = 0.000001;

	@Test
	public void testPropagateActivationToParents() {

		PamNode testNod1 = pam.addDefaultNode("number1");
		testNod1.setActivation(0.2);

		PamNode testNod2 = pam.addDefaultNode("number2");
		double node2Activation = 0.2;
		testNod2.setActivation(node2Activation);

		PamNode testNod3 = pam.addDefaultNode("number3");
		double node3Activation = 0.3;
		testNod3.setActivation(node3Activation);

		PamNode testNod4 = pam.addDefaultNode("number4");
		testNod4.setActivation(0.99);

		PamLinkImpl l12 = (PamLinkImpl) pam.addDefaultLink(testNod1, testNod2,
				PerceptualAssociativeMemoryNSImpl.NONE);
		double link12Activation = 1.0;
		l12.setBaseLevelActivation(link12Activation);

		PamLinkImpl l13 = (PamLinkImpl) pam.addDefaultLink(testNod1, testNod3,
				PerceptualAssociativeMemoryNSImpl.NONE);
		double link13Activation = 0.5;
		l13.setBaseLevelActivation(link13Activation);

		PamLinkImpl l41 = (PamLinkImpl) pam.addDefaultLink(testNod4, testNod1,
				PerceptualAssociativeMemoryNSImpl.NONE);
		l41.setBaseLevelActivation(0.99);

		TaskSpawner ts = new ExecutingMockTaskSpawner();
		pam.setAssistingTaskSpawner(ts);

		double upscaleFactor = 0.5;
		pam.setUpscaleFactor(upscaleFactor);
		pam.propagateActivationToParents(testNod1);

		// Should be 2 tasks to pass the activation to Nodes 2, 3.
		Collection<FrameworkTask> tasks = ts.getTasks();
		assertEquals(2, tasks.size());

		double propagatedActivation = testNod1.getTotalActivation()
				* upscaleFactor;
		assertEquals(propagatedActivation, pam.getLink(l12.getExtendedId())
				.getActivation(), epsilon);
		assertEquals(propagatedActivation, pam.getLink(l13.getExtendedId())
				.getActivation(), epsilon);

		assertEquals(node2Activation + propagatedActivation * link12Activation,
				pam.getNode(testNod2.getId()).getActivation(), epsilon);
		assertEquals(node3Activation + propagatedActivation * link13Activation,
				pam.getNode(testNod3.getId()).getActivation(), epsilon);

		assertEquals(0.99, testNod4.getActivation(), epsilon);
		assertEquals(0.0, l41.getActivation(), epsilon);
	}

	@Test
	public void testAddNodeStructureToPercept() {
		MockPamListener pl = new MockPamListener();
		pam.addPamListener(pl);
		nodeStructure.addDefaultNode(node1);
		nodeStructure.addDefaultNode(node2);
		nodeStructure.addDefaultNode(node3);

		pam.addToPercept(nodeStructure);

		NodeStructure ns = pl.ns;
		assertEquals(3, ns.getNodeCount());
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
		assertTrue(ns.containsNode(node3));
		assertEquals(0, ns.getLinkCount());
	}

	@Test
	public void testAddNodeToPercept() {
		MockPamListener pl = new MockPamListener();
		pam.addPamListener(pl);
		assertNull(pl.n);

		pam.addToPercept(node1);

		assertEquals(node1.getId(), pl.n.getId());
		assertEquals(node1.getActivation(), pl.n.getActivation(), epsilon);
	}

	@Test
	public void testAddLinkToPercept() {
		MockPamListener pl = new MockPamListener();
		pam.addPamListener(pl);
		assertNull(pl.l);

		pam.addToPercept(link1);

		assertEquals(link1.getExtendedId(), pl.l.getExtendedId());
		assertEquals(link1.getActivation(), pl.l.getActivation(), epsilon);
	}

	@Test
	public void testContainsNode() {
		Node n = pam.addDefaultNode("foo");
		assertTrue(pam.containsNode(n));
	}

	@Test
	public void testContainsNode0() {
		node2.setBaseLevelRemovalThreshold(0.1111);
		node2.setBaseLevelActivation(1.0);
		node2 = (PamNodeImpl) pam.addDefaultNode(node2);
		assertTrue(pam.containsNode(node2));
		// TODO test decay
		// pam.decayModule(1000);
	}

	@Test
	public void testContainsNodeEid() {
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		ExtendedId eid = new ExtendedId(node1.getId());
		assertTrue(pam.containsNode(eid));

		eid = new ExtendedId(node1.getId() + 1);
		assertFalse(pam.containsNode(eid));
	}

	@Test
	public void testContainsLink() {
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		node1.setBaseLevelRemovalThreshold(0.0);
		node1.setActivation(0.0);
		node1.setBaseLevelActivation(0.0);
		assertTrue(pam.containsNode(node1));

		// TODO test decay
		// pam.decayModule(1);
	}

	@Test
	public void testContainsLink1() {
		PamNode a = pam.addDefaultNode(node1);
		PamNode b = pam.addDefaultNode(node2);
		Link l = factory.getLink("PamLinkImpl", a, b,
				PerceptualAssociativeMemoryNSImpl.NONE);
		pam.addDefaultLink(l);
		assertTrue(pam.containsLink(l));
		assertFalse(pam.containsLink(this.link2));
		assertTrue(pam.containsLink(l.getExtendedId()));
	}

	@Test
	public void testSetPerceptThreshold() {
		pam.setPerceptThreshold(0.7);

		pam.setPerceptThreshold(-1);
		assertTrue(PerceptualAssociativeMemoryNSImpl.getPerceptThreshold() == 0.7);

		pam.setPerceptThreshold(231);
		assertTrue(PerceptualAssociativeMemoryNSImpl.getPerceptThreshold() == 0.7);

		pam.setPerceptThreshold(0.3487);
		assertTrue(PerceptualAssociativeMemoryNSImpl.getPerceptThreshold() == 0.3487);

		pam.setPerceptThreshold(0.0);
		assertTrue(PerceptualAssociativeMemoryNSImpl.getPerceptThreshold() == 0.0);

		pam.setPerceptThreshold(1.0);
		assertTrue(PerceptualAssociativeMemoryNSImpl.getPerceptThreshold() == 1.0);
	}

	@Test
	public void testUpscaleFactor() {
		pam.setUpscaleFactor(-1.0);
		assertTrue(0.0 == pam.getUpscaleFactor());

		pam.setUpscaleFactor(123);
		assertTrue(1.0 == pam.getUpscaleFactor());

		pam.setUpscaleFactor(0.5);
		assertTrue(0.5 == pam.getUpscaleFactor());

		pam.setUpscaleFactor(0.0);
		assertTrue(0.0 == pam.getUpscaleFactor());

		pam.setUpscaleFactor(1.0);
		assertTrue(1.0 == pam.getUpscaleFactor());
	}

	@Test
	public void testDownscaleFactor() {
		pam.setDownscaleFactor(-1.0);
		assertTrue(0.0 == pam.getDownscaleFactor());

		pam.setDownscaleFactor(123);
		assertTrue(1.0 == pam.getDownscaleFactor());

		pam.setDownscaleFactor(0.5);
		assertTrue(0.5 == pam.getDownscaleFactor());

		pam.setDownscaleFactor(0.0);
		assertTrue(0.0 == pam.getDownscaleFactor());

		pam.setDownscaleFactor(1.0);
		assertTrue(1.0 == pam.getDownscaleFactor());
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

		pam.setPerceptThreshold(0.0);
		node1.setBaseLevelActivation(0.0);
		node1.setActivation(0.0);
		assertFalse(pam.isOverPerceptThreshold(node1));
	}

	@Test
	public void testGetPamNode() {
		Node n = pam.getNode(223042328);
		assertEquals(null, n);

		pam.addDefaultNode(node3);
		n = pam.getNode(node3.getId());
		assertEquals(node3, n);

		node2.setId(69);
		pam.addDefaultNode(node2);
		n = (PamNode) pam.getNode(69);
		assertEquals(node2, n);

		// extended id
		n = pam.getNode(node3.getExtendedId());
		assertEquals(node3, n);

		n = pam.getNode(new ExtendedId(70));
		assertEquals(null, n);
	}

	@Test
	public void testGetPamNodeByLabel() {
		Node n = pam.getNode("asdfg");
		assertEquals(null, n);

		node3.setLabel("node3");
		pam.addDefaultNode(node3);
		n = pam.getNode("node3");
		assertEquals(node3, n);
	}

	@Test
	public void testGetPamLink() {
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		Link l = factory.getLink("PamLinkImpl", node1, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);
		pam.addDefaultLink(l);

		assertEquals(l, pam.getLink(l.getExtendedId()));
		assertEquals(null, pam.getLink(new ExtendedId(99, new ExtendedId(11),
				99)));

		ExtendedId id2 = new ExtendedId(node1.getId(), node2.getExtendedId(),
				PerceptualAssociativeMemoryNSImpl.NONE.getId());
		assertEquals(l, pam.getLink(id2));
	}

	/*
	 * Test method for {@link
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl
	 * #taskManagerDecayModule(long)}.
	 * 
	 * @see LinearDecayStrategy
	 */
	@Test
	public void testDecayModule() {
		// TODO test decay
		// node1.setBaseLevelActivation(1.0);
		// node1.setLearnableRemovalThreshold(0.0001);
		// node1.setDecayStrategy(decayStrategy);
		// node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		// assertTrue(pam.containsNode(node1));
		// assertFalse(node1.isRemovable());
		//		
		// pam.decayModule(100);
		// assertTrue(node1.getBaseLevelActivation()+"",node1.getBaseLevelActivation()
		// <= 0.001);
		// assertFalse(pam.containsNode(node1));
	}

	@Test
	public void testGetPamNodes() {
		Collection<Node> nodes = pam.getNodes();
		assertEquals(0, nodes.size() - pam.getLinkCategories().size());
		try {
			nodes.clear();
			assertTrue(false);
		} catch (UnsupportedOperationException e) {
		}

		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);

		nodes = pam.getNodes();
		assertTrue(nodes.size() - pam.getLinkCategories().size() == 2);
	}

	@Test
	public void testGetPamLinks() {
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);

		Collection<Link> links = pam.getLinks();
		assertTrue(links.size() == 0);
		try {
			links.remove(link1);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {
		}
		Link l = factory.getLink("PamLinkImpl", node1, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);
		pam.addDefaultLink(l);

		l = factory.getLink("PamLinkImpl", node2, node3,
				PerceptualAssociativeMemoryNSImpl.NONE);
		pam.addDefaultLink(l);

		l = factory.getLink("PamLinkImpl", node3, node1,
				PerceptualAssociativeMemoryNSImpl.NONE);
		pam.addDefaultLink(l);

		links = pam.getLinks();
		assertTrue(links.size() == 3);
	}

	@Test
	public void testGetLinkCategories() {
		Collection<LinkCategory> cats = pam.getLinkCategories();
		// This assumes PamNode will be LinkCategory which is reasonable
		assertTrue(cats.size() == pam.getNodes().size());

		for (LinkCategory cat : cats) {
			assertTrue(cat instanceof PamNode);
		}
	}

	@Test
	public void testGetLinkCategory() {
		int id = PerceptualAssociativeMemoryNSImpl.NONE.getId();
		assertEquals(PerceptualAssociativeMemoryNSImpl.NONE, pam
				.getLinkCategory(id));

		Node newCat = factory.getNode("PamNodeImpl");
		newCat.setId(666);
		pam.addLinkCategory((LinkCategory) newCat);
		assertTrue(pam.getLinkCategory(666) != null);
		assertEquals(pam.getLinkCategory(666), newCat);
	}

	@Test
	public void testAddLinkCategory() {
		int categoryCount = pam.getLinkCategories().size();

		LinkCategory lc = pam.addLinkCategory(null);

		assertEquals(null, lc);
		assertEquals(categoryCount, pam.getLinkCategories().size());

		lc = pam.addLinkCategory(node1);

		assertEquals(node1, lc);
		assertEquals(categoryCount + 1, pam.getLinkCategories().size());
	}

}
