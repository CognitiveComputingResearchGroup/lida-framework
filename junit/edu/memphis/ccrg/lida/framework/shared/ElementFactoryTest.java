/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.attentioncodelets.NeighborhoodAttentionCodelet;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.initialization.FrameworkTaskDef;
import edu.memphis.ccrg.lida.framework.initialization.LinkableDef;
import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.BasicStructureBuildingCodelet;

public class ElementFactoryTest {

	private static ElementFactory factory;
	private static final double epsilon = 10e-9;
	private Node node1, node2;
	private PamNode pamNode1;
	private Link link1;
	private LinkCategory category1;

	@BeforeClass
	public static void setUpBeforeClass() {
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils
				.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}

	@Before
	public void setUp() {
		node1 = factory.getNode();
		node2 = factory.getNode();
		pamNode1 = (PamNode) factory.getNode("PamNodeImpl");
		category1 = (LinkCategory) factory.getNode("PamNodeImpl");
		link1 = factory.getLink(node1, node2, category1);

		node1.setActivation(0.11);
		node1.setActivatibleRemovalThreshold(0.22);
		node1.setLabel("blah");
		node1.setGroundingPamNode(pamNode1);
	}

	@Test
	public void testGetInstance() {
		ElementFactory factory1 = ElementFactory.getInstance();
		assertTrue(factory1 != null);

		ElementFactory factory2 = ElementFactory.getInstance();
		assertTrue(factory1 == factory2);
		assertEquals(factory1, factory2);
	}

	@Test
	public void testSetDefaultLinkType() {
		factory.setDefaultLinkType("FOO");
		assertEquals("LinkImpl", factory.getDefaultLinkType());

		factory.addLinkType("FOO", PamLinkImpl.class.getCanonicalName());
		factory.setDefaultLinkType("FOO");
		assertEquals("FOO", factory.getDefaultLinkType());

		// return to normal
		factory.setDefaultLinkType("LinkImpl");
	}

	@Test
	public void testSetDefaultNodeType() {
		factory.setDefaultNodeType("BAR");
		assertEquals("NodeImpl", factory.getDefaultNodeType());

		factory.addNodeType("FOO", PamNodeImpl.class.getCanonicalName());
		factory.setDefaultNodeType("FOO");
		assertEquals("FOO", factory.getDefaultNodeType());

		// return to normal
		factory.setDefaultNodeType("NodeImpl");
	}

	@Test
	public void testSetDefaultDecayType() {
		factory.setDefaultDecayType("BANANA");
		assertFalse("BANANA".equals(factory.getDefaultDecayType()));

		StrategyDef def = new StrategyDef();
		factory.addDecayStrategy("BANANA", def);
		factory.setDefaultDecayType("BANANA");
		assertEquals("BANANA", factory.getDefaultDecayType());

		// return to normal
		factory.setDefaultDecayType("defaultDecay");
	}

	@Test
	public void testSetDefaultExciteType() {
		factory.setDefaultExciteType("VVV");
		assertFalse("VVV".equals(factory.getDefaultExciteType()));

		StrategyDef def = new StrategyDef();
		factory.addExciteStrategy("VVV", def);
		factory.setDefaultExciteType("VVV");
		assertEquals("VVV", factory.getDefaultExciteType());

		// return to normal
		factory.setDefaultExciteType("defaultExcite");
	}

	@Test
	public void testAddDecayStrategy() {
		StrategyDef decay = new StrategyDef(SigmoidDecayStrategy.class
				.getCanonicalName(), "sigmoid", null, "decay", false);
		factory.addDecayStrategy("sigmoid", decay);
		DecayStrategy strategy = factory.getDecayStrategy("sigmoid");

		assertTrue(strategy instanceof SigmoidDecayStrategy);
	}

	@Test
	public void testAddExciteStrategy() {
		StrategyDef excite = new StrategyDef(SigmoidExciteStrategy.class
				.getCanonicalName(), "sigmoid", null, "excite", false);
		factory.addExciteStrategy("sigmoid", excite);
		ExciteStrategy strategy = factory.getExciteStrategy("sigmoid");

		assertTrue(strategy instanceof SigmoidExciteStrategy);
	}

	@Test
	public void testAddStrategy() {
		factory.addStrategy("generic", new StrategyDef(
				SigmoidExciteStrategy.class.getCanonicalName(), "sigmoid",
				null, "excite", false));

		Strategy strategy = factory.getStrategy("generic");
		assertTrue(strategy instanceof SigmoidExciteStrategy);
	}

	@Test
	public void testAddLinkType1() {
		LinkableDef linkDef = new LinkableDef(PamLinkImpl.class
				.getCanonicalName(), new HashMap<String, String>(), "abc", null);
		factory.addLinkType(linkDef);
		Link l = factory.getLink("abc", factory.getNode(), factory.getNode(),
				new PamNodeImpl());
		assertTrue(l instanceof PamLinkImpl);
		assertTrue(factory.containsLinkType("abc"));
	}

	@Test
	public void testAddLinkType2() {
		factory.addLinkType("samson", PamLinkImpl.class.getCanonicalName());
		Link l = factory.getLink("samson", factory.getNode(),
				factory.getNode(), new PamNodeImpl());
		assertTrue(l instanceof PamLinkImpl);
		assertTrue(factory.containsLinkType("samson"));
	}

	@Test
	public void testAddNodeType1() {
		LinkableDef nodeDef = new LinkableDef(PamNodeImpl.class
				.getCanonicalName(), new HashMap<String, String>(), "abc", null);
		factory.addNodeType(nodeDef);
		Node node = factory.getNode("abc");
		assertTrue(node instanceof PamNodeImpl);
		assertTrue(factory.containsNodeType("abc"));
	}

	@Test
	public void testAddNodeType2() {
		factory.addNodeType("samson", PamNodeImpl.class.getCanonicalName());
		Node node = factory.getNode("samson");
		assertTrue(node instanceof PamNodeImpl);
		assertTrue(factory.containsNodeType("samson"));
	}

	@Test
	public void testAddCodeletType1() {
		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				NeighborhoodAttentionCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "winwin",
				new HashMap<String, Object>(),
				new HashMap<ModuleName, String>());
		factory.addFrameworkTaskType(taskDef);
		Codelet foo = (Codelet) factory.getFrameworkTask("winwin", null);
		assertTrue(foo instanceof NeighborhoodAttentionCodelet);
		assertTrue(factory.containsTaskType("winwin"));
	}

	@Test
	public void testAddCodeletType2() {
		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				BasicStructureBuildingCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "apple",
				new HashMap<String, Object>(),
				new HashMap<ModuleName, String>());
		factory.addFrameworkTaskType(taskDef);
		Codelet foo = (Codelet) factory.getFrameworkTask("apple", null);
		assertTrue(foo instanceof BasicStructureBuildingCodelet);
		assertTrue(factory.containsTaskType("apple"));
	}

	@Test
	public void testContainsNodeType() {
		// already tested standard usage in previous tests
		assertFalse(factory.containsNodeType("RYANJMCCALL"));
	}

	@Test
	public void testContainsLinkType() {
		// already tested standard usage in previous tests
		assertFalse(factory.containsLinkType("RYANJMCCALL"));
	}

	@Test
	public void testContainsCodeletType() {
		// already tested standard usage in previous tests
		assertFalse(factory.containsTaskType("RYANJMCCALL"));
	}

	@Test
	public void testGetDecayStrategy() {
		// already tested standard usage in previous tests
		DecayStrategy foo = factory.getDecayStrategy("1492");
		DecayStrategy defaultStrategy = factory.getDefaultDecayStrategy();
		assertEquals(foo.getClass(), defaultStrategy.getClass());
	}

	@Test
	public void testGetExciteStrategy() {
		// already tested standard usage in previous tests
		ExciteStrategy foo = factory.getExciteStrategy("1912");
		ExciteStrategy defaultStrategy = factory.getDefaultExciteStrategy();
		assertEquals(foo.getClass(), defaultStrategy.getClass());
	}

	@Test
	public void testGetStrategy() {
		// already tested standard usage in previous tests
		Strategy s = factory.getStrategy("1984");
		assertNull(s);
	}

	@Test
	public void testGetLink0() {
		Link l = factory.getLink(node1, node2, category1, 0.7, 0.09);
		assertTrue(l instanceof LinkImpl);
		assertEquals(l.getSource(), node1);
		assertEquals(l.getSink(), node2);
		assertEquals(l.getCategory(), category1);
		assertTrue(0.7 == l.getActivation());
		assertTrue(0.09 == l.getActivatibleRemovalThreshold());
		assertTrue(l.isSimpleLink());
		assertTrue(l.getGroundingPamLink() == null);

		l = factory.getLink(node1, link1, category1, 0.7, 0.09);
		assertTrue(l instanceof LinkImpl);
		assertEquals(l.getSource(), node1);
		assertEquals(l.getSink(), link1);
		assertEquals(l.getCategory(), category1);
		assertTrue(0.7 == l.getActivation());
		assertTrue(0.09 == l.getActivatibleRemovalThreshold());
		assertTrue(!l.isSimpleLink());
		assertTrue(l.getGroundingPamLink() == null);

		l = factory.getLink(null, node2, category1, 0.7, 0.09);
		assertTrue(l == null);

		l = factory.getLink(node1, null, category1, 0.7, 0.09);
		assertTrue(l == null);

		l = factory.getLink(node1, node2, null, 0.7, 0.09);
		assertTrue(l == null);
	}

	@Test
	public void testGetLink1() {
		Link l = factory.getLink(node1, node2, category1);

		assertTrue(l instanceof LinkImpl);
		assertEquals(l.getSource(), node1);
		assertEquals(l.getSink(), node2);
		assertEquals(l.getCategory(), category1);
		assertTrue(Activatible.DEFAULT_ACTIVATION == l.getActivation());
		assertTrue(Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD == l
				.getActivatibleRemovalThreshold());
		assertTrue(l.isSimpleLink());
		assertTrue(l.getGroundingPamLink() == null);
	}

	@Test
	public void testGetLink2() {
		String requiredType = "LinkImpl";
		String desiredType = "PamLinkImpl";
		Link l = factory.getLink(requiredType, desiredType, node1, node2,
				category1);
		assertTrue(l instanceof PamLinkImpl);
		assertTrue(Activatible.DEFAULT_ACTIVATION == l.getActivation());
		assertTrue(Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD == l
				.getActivatibleRemovalThreshold());

		requiredType = "PamLinkImpl";
		desiredType = "LinkImpl";
		l = factory.getLink(requiredType, desiredType, node1, node2, category1);
		assertTrue(l == null);
	}

	@Test
	public void testGetLink3() {
		Link l = factory.getLink("LinkImpl", node1, node2, category1);
		assertTrue(l instanceof LinkImpl);

		l = factory.getLink("PamLinkImpl", node1, node2, category1);
		assertTrue(l instanceof PamLinkImpl);

		l = factory.getLink("NodeImpl", node1, node2, category1);
		assertTrue(l == null);
	}

	@Test
	public void testGetLink4() {
		String linkT = "PamLinkImpl";

		Link l = factory.getLink(linkT, node2, node1, category1,
				"defaultDecay", "defaultExcite", 0.99, 0.11);

		assertTrue(l instanceof PamLinkImpl);
		assertEquals(l.getSource(), node2);
		assertEquals(l.getSink(), node1);
		assertEquals(l.getCategory(), category1);
		assertTrue(0.99 == l.getActivation());
		assertTrue(0.11 == l.getActivatibleRemovalThreshold());
		assertTrue(l.isSimpleLink());
		assertTrue(l.getGroundingPamLink() == l);
		assertEquals(LinearDecayStrategy.class, l.getDecayStrategy().getClass());
		assertEquals(LinearExciteStrategy.class, l.getExciteStrategy()
				.getClass());
	}

	@Test
	public void testGetNode0() {
		Node n = factory.getNode();

		assertTrue(n.getGroundingPamNode() == null);
		assertTrue(n.getActivation() == Activatible.DEFAULT_ACTIVATION);
		assertTrue(n.getActivatibleRemovalThreshold() == Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
	}

	@Test
	public void testGetNode7() {
		Node n = factory.getNode("NodeImpl");

		assertTrue(n.getGroundingPamNode() == null);
		assertTrue(n.getActivation() == Activatible.DEFAULT_ACTIVATION);
		assertTrue(n.getActivatibleRemovalThreshold() == Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);

		PamNode pn = (PamNode) factory.getNode("PamNodeImpl");

		assertEquals(pn.getGroundingPamNode(), pn);
		assertTrue(pn.getActivation() == Activatible.DEFAULT_ACTIVATION);
		assertTrue(pn.getActivatibleRemovalThreshold() == Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);

		n = factory.getNode("adfowifjwoqi");
		assertTrue(n == null);
	}

	@Test
	public void testGetNode8() {
		Node n = factory.getNode("NodeImpl", "foobar");
		assertEquals("foobar", n.getLabel());

		n = factory.getNode("aslfskj", "asdlfkj");
		assertTrue(n == null);

		n = factory.getNode("NodeImpl", null);
		assertTrue(n.getLabel() == null);
	}

	@Test
	public void testGetNode1() {
		node1.setActivatibleRemovalThreshold(0.11);
		node1.setActivation(0.22);
		node1.setGroundingPamNode(pamNode1);
		node1.setLabel("ABC");

		Node n = factory.getNode(node1);

		assertTrue(n.getActivatibleRemovalThreshold() == 0.11);
		assertTrue(n.getActivation() == 0.22);
		assertEquals(n.getGroundingPamNode(), pamNode1);
		assertEquals(n.getLabel(), "ABC");
	}

	@Test
	public void testGetNode2() {

		StrategyDef decayDef = new StrategyDef(SigmoidDecayStrategy.class
				.getCanonicalName(), "specialDecay", null, "decay", true);
		factory.addDecayStrategy("specialDecay", decayDef);

		StrategyDef exciteDef = new StrategyDef(SigmoidExciteStrategy.class
				.getCanonicalName(), "specialExcite", null, "excite", true);
		factory.addExciteStrategy("specialExcite", exciteDef);

		Map<String, String> defaultStrategies = new HashMap<String, String>();
		defaultStrategies.put("decay", "specialDecay");
		defaultStrategies.put("excite", "specialExcite");
		LinkableDef nodeDef = new LinkableDef(
				NodeImpl.class.getCanonicalName(), defaultStrategies,
				"special", null);
		factory.addNodeType(nodeDef);

		Node oNode = factory.getNode();

		assertFalse(oNode.getExciteStrategy() instanceof SigmoidExciteStrategy);
		assertFalse(oNode.getDecayStrategy() instanceof SigmoidDecayStrategy);

		oNode.setActivation(0.11);
		oNode.setActivatibleRemovalThreshold(0.22);
		oNode.setLabel("blah");
		oNode.setGroundingPamNode(pamNode1);

		Node newNode = factory.getNode(oNode, "special");

		assertTrue(newNode.getExciteStrategy() instanceof SigmoidExciteStrategy);
		assertTrue(newNode.getDecayStrategy() instanceof SigmoidDecayStrategy);
		assertTrue(newNode.getActivation() == 0.11);
		assertTrue(newNode.getActivatibleRemovalThreshold() == 0.22);
		assertEquals("blah", newNode.getLabel());
		assertEquals(oNode.getGroundingPamNode(), newNode.getGroundingPamNode());
	}

	@Test
	public void testGetNode3() {
		String requiredType = "NodeImpl";
		String desiredType = "PamNodeImpl";
		Node oNode = factory.getNode();
		oNode.setActivation(0.11);
		oNode.setActivatibleRemovalThreshold(0.22);
		oNode.setLabel("blah");
		oNode.setGroundingPamNode(pamNode1);

		Node n = factory.getNode(requiredType, oNode, desiredType);

		assertTrue(n instanceof PamNodeImpl);
		assertEquals(n.getActivation(), 0.11, epsilon);
		assertEquals(n.getActivatibleRemovalThreshold(), 0.22, epsilon);
		assertEquals("blah", n.getLabel());
		assertEquals(oNode.getGroundingPamNode(), n.getGroundingPamNode());

		// sub test
		requiredType = "PamNodeImpl";
		desiredType = "NodeImpl";
		n = factory.getNode(requiredType, oNode, desiredType);
		assertNull(n);

		// sub test
		requiredType = "NodeImpl";
		n = factory.getNode(requiredType, oNode, desiredType);
		assertTrue(n instanceof NodeImpl);
		assertEquals(n.getActivation(), 0.11, epsilon);
	}

	@Test
	public void testGetNode3Null() {
		String requiredType = "NodeImpl";
		String desiredType = "PamNodeImpl";
		Node oNode = null;

		Node n = factory.getNode(requiredType, oNode, desiredType);

		assertTrue(n instanceof PamNodeImpl);
		assertEquals(n.getActivation(), Activatible.DEFAULT_ACTIVATION, epsilon);
		assertEquals(n.getActivatibleRemovalThreshold(),
				Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD, epsilon);
		assertEquals(n, n.getGroundingPamNode());

		// sub test
		requiredType = "PamNodeImpl";
		desiredType = "NodeImpl";
		n = factory.getNode(requiredType, oNode, desiredType);
		assertNull(n);

		// sub test
		requiredType = "NodeImpl";
		n = factory.getNode(requiredType, oNode, desiredType);
		assertTrue(n instanceof NodeImpl);
		assertEquals(n.getActivation(), Activatible.DEFAULT_ACTIVATION, epsilon);
	}

	@Test
	public void testGetNode4() {
		Node n = factory.getNode(node1, "specialDecay", "specialExcite");
		assertTrue(n.getDecayStrategy() instanceof SigmoidDecayStrategy);
		assertTrue(n.getExciteStrategy() instanceof SigmoidExciteStrategy);

		assertTrue(n instanceof NodeImpl);
		assertTrue(n.getActivation() == node1.getActivation());
		assertTrue(n.getActivatibleRemovalThreshold() == node1
				.getActivatibleRemovalThreshold());
		assertEquals(n.getLabel(), node1.getLabel());
		assertEquals(n.getGroundingPamNode(), node1.getGroundingPamNode());
	}

	// @Test
	// public void testGetNode5() {
	// // factory.getNode(oNode, nodeType, decayStrategy, exciteStrategy)
	// }

	@Test
	public void testGetNode6() {
		Node n = factory.getNode("PamNodeImpl", "specialDecay",
				"specialExcite", "chuck", 0.99, 0.11);
		assertTrue(n instanceof PamNodeImpl);
		assertTrue(n.getDecayStrategy() instanceof SigmoidDecayStrategy);
		assertTrue(n.getExciteStrategy() instanceof SigmoidExciteStrategy);
		assertEquals(n.getLabel(), "chuck");
		assertTrue(0.99 == n.getActivation());
		assertTrue(0.11 == n.getActivatibleRemovalThreshold());

		n = factory.getNode(null, "specialDecay", "specialExcite", "chuck",
				0.99, 0.11);
		assertTrue(n == null);

		n = factory.getNode("PamNodeImpl", null, "specialExcite", "chuck",
				0.99, 0.11);
		assertTrue(n instanceof PamNodeImpl);
		assertFalse(n.getDecayStrategy() instanceof SigmoidDecayStrategy);
		assertTrue(n.getExciteStrategy() instanceof SigmoidExciteStrategy);

		n = factory.getNode("PamNodeImpl", "specialDecay", null, "chuck", 0.99,
				0.11);
		assertTrue(n instanceof PamNodeImpl);
		assertTrue(n.getDecayStrategy() instanceof SigmoidDecayStrategy);
		assertFalse(n.getExciteStrategy() instanceof SigmoidExciteStrategy);

		n = factory.getNode("PamNodeImpl", "specialDecay", "specialExcite",
				null, 0.99, 0.11);
		assertTrue(n instanceof PamNodeImpl);
	}

	@Test
	public void testGetNodeFactoryType() {
		Node n = factory.getNode();
		assertEquals("NodeImpl", n.getFactoryType());

		n = factory.getNode(n);
		assertEquals("NodeImpl", n.getFactoryType());

		n = factory.getNode("PamNodeImpl");
		assertEquals("PamNodeImpl", n.getFactoryType());

		n = factory.getNode(n, "NodeImpl");
		assertEquals("NodeImpl", n.getFactoryType());
		n = factory.getNode(n, "PamNodeImpl");
		assertEquals("PamNodeImpl", n.getFactoryType());

		n = factory.getNode("NodeImpl", "");
		assertEquals("NodeImpl", n.getFactoryType());

		n = factory.getNode(n, "", "");
		assertEquals("NodeImpl", n.getFactoryType());

		n = factory.getNode("NodeImpl", n, "PamNodeImpl");
		assertEquals("PamNodeImpl", n.getFactoryType());

		n = factory.getNode("PamNodeImpl", n, "PamNodeImpl");
		assertEquals("PamNodeImpl", n.getFactoryType());

		n = factory.getNode("NodeImpl", "", "", "", 0.0, 0.0);
		assertEquals("NodeImpl", n.getFactoryType());
	}

	@Test
	public void testGetLinkFactoryType() {
		Link l = factory.getLink(node1, node2, category1);
		assertEquals("LinkImpl", l.getFactoryType());

		l = factory.getLink("PamLinkImpl", node1, node2, category1);
		assertEquals("PamLinkImpl", l.getFactoryType());

		l = factory.getLink(node1, node2, category1, 0, 0);
		assertEquals("LinkImpl", l.getFactoryType());

		l = factory.getLink("LinkImpl", "PamLinkImpl", node1, node2, category1);
		assertEquals("PamLinkImpl", l.getFactoryType());
		l = factory.getLink("LinkImpl", "LinkImpl", node1, node2, category1);
		assertEquals("LinkImpl", l.getFactoryType());

		l = factory.getLink("LinkImpl", node2, node1, category1, null, null, 0,
				0);
		assertEquals("LinkImpl", l.getFactoryType());
	}

	// TODO test various parameter possibilities
	@Test
	public void testGetCodelet0() {
		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				NeighborhoodAttentionCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "testType",
				new HashMap<String, Object>(),
				new HashMap<ModuleName, String>());
		factory.addFrameworkTaskType(taskDef);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Hello", 500);

		Codelet c = (Codelet) factory.getFrameworkTask("testType", "", "", 100,
				0.66, 0.77, params, null);
		assertTrue(c instanceof NeighborhoodAttentionCodelet);
		assertEquals(c.getTicksPerRun(), 100);
		assertTrue(c.getActivation() == 0.66);
		assertTrue(c.getActivatibleRemovalThreshold() == 0.77);
		int hello = c.getParam("Hello", 0);
		assertEquals(500, hello);
	}

	@Test
	public void testGetTaskWithParams() {
		Map<String, Object> factoryParams = new HashMap<String, Object>();
		factoryParams.put("Hello", 1);
		factoryParams.put("Bye", false);
		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				NeighborhoodAttentionCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "testType", factoryParams,
				new HashMap<ModuleName, String>());
		factory.addFrameworkTaskType(taskDef);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Hello", 500);

		Codelet c = (Codelet) factory.getFrameworkTask("testType", "", "", 100,
				0.66, 0.77, params, null);
		assertTrue(c instanceof NeighborhoodAttentionCodelet);
		assertEquals(c.getTicksPerRun(), 100);
		assertTrue(c.getActivation() == 0.66);
		assertTrue(c.getActivatibleRemovalThreshold() == 0.77);
		int hello = c.getParam("Hello", 0);
		assertEquals(500, hello);
		assertFalse(c.getParam("Bye", true));
	}

	@Test
	public void testGetTaskWithParams1() {
		Map<String, Object> factoryParams = new HashMap<String, Object>();
		factoryParams.put("Hello", 1);
		factoryParams.put("Bye", false);
		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				NeighborhoodAttentionCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "testType", factoryParams,
				new HashMap<ModuleName, String>());
		factory.addFrameworkTaskType(taskDef);

		Map<String, Object> params = null;

		Codelet c = (Codelet) factory.getFrameworkTask("testType", "", "", 100,
				0.66, 0.77, params, null);
		assertTrue(c instanceof NeighborhoodAttentionCodelet);
		assertEquals(c.getTicksPerRun(), 100);
		assertTrue(c.getActivation() == 0.66);
		assertTrue(c.getActivatibleRemovalThreshold() == 0.77);
		int hello = c.getParam("Hello", 0);
		assertEquals(1, hello);
		assertFalse(c.getParam("Bye", true));
	}

	@Test
	public void testGetCodelet1() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Hello", 500);
		Codelet c = (Codelet) factory.getFrameworkTask("testType",
				"defaultDecay", "defaultExcite", 100, 0.66, 0.77, params, null);

		assertTrue(c.getDecayStrategy() instanceof LinearDecayStrategy);
		assertTrue(c.getExciteStrategy() instanceof LinearExciteStrategy);

		assertTrue(c instanceof NeighborhoodAttentionCodelet);
		assertEquals(c.getTicksPerRun(), 100);
		assertTrue(c.getActivation() == 0.66);
		assertTrue(c.getActivatibleRemovalThreshold() == 0.77);
		int hello = (Integer) c.getParam("Hello", -1);
		assertEquals(500, hello);
	}

	@Test
	public void testFrameworkTask() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Hello", 500);
		FrameworkTask t = (Codelet) factory.getFrameworkTask("testType",
				"defaultDecay", "defaultExcite", 100, 0.66, 0.77, params, null);

		assertTrue(t.getDecayStrategy() instanceof LinearDecayStrategy);
		assertTrue(t.getExciteStrategy() instanceof LinearExciteStrategy);

		assertTrue(t instanceof NeighborhoodAttentionCodelet);
		assertEquals(t.getTicksPerRun(), 100);
		assertTrue(t.getActivation() == 0.66);
		assertTrue(t.getActivatibleRemovalThreshold() == 0.77);
		int hello = (Integer) t.getParam("Hello", -1);
		assertEquals(500, hello);
	}

	@Test
	public void testFrameworkTaskModules() {
		Map<ModuleName, String> moduleDef = new HashMap<ModuleName, String>();
		moduleDef.put(ModuleName.PerceptualAssociativeMemory, "toRead");

		FrameworkTaskDef taskDef = new FrameworkTaskDef(MockFrameworkTask.class
				.getCanonicalName(), 1, new HashMap<String, String>(),
				"testType", new HashMap<String, Object>(), moduleDef);
		factory.addFrameworkTaskType(taskDef);

		Map<ModuleName, FrameworkModule> modulesMap = new HashMap<ModuleName, FrameworkModule>();
		FrameworkModule fm = new MockFrameworkModule();
		fm.setModuleName(ModuleName.PerceptualAssociativeMemory);
		modulesMap.put(ModuleName.PerceptualAssociativeMemory, fm);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Hello", 500);
		MockFrameworkTask t = (MockFrameworkTask) factory.getFrameworkTask(
				"testType", "defaultDecay", "defaultExcite", 100, 0.66, 0.77,
				params, modulesMap);

		assertTrue(t.getDecayStrategy() instanceof LinearDecayStrategy);
		assertTrue(t.getExciteStrategy() instanceof LinearExciteStrategy);

		assertTrue(t instanceof MockFrameworkTask);
		assertEquals(t.getTicksPerRun(), 100);
		assertTrue(t.getActivation() == 0.66);
		assertTrue(t.getActivatibleRemovalThreshold() == 0.77);
		int hello = (Integer) t.getParam("Hello", -1);
		assertEquals(500, hello);
		assertEquals("toRead", t.moduleUsage);
		assertEquals(fm, t.associatedModule);
	}

	@Test
	public void testFrameworkTaskModules2() {
		Map<ModuleName, String> moduleDef = new HashMap<ModuleName, String>();
		moduleDef.put(ModuleName.PerceptualAssociativeMemory, "toRead");

		FrameworkTaskDef taskDef = new FrameworkTaskDef(MockFrameworkTask.class
				.getCanonicalName(), 1, new HashMap<String, String>(),
				"testType", new HashMap<String, Object>(), moduleDef);
		factory.addFrameworkTaskType(taskDef);

		Map<ModuleName, FrameworkModule> modulesMap = new HashMap<ModuleName, FrameworkModule>();
		FrameworkModule fm = new MockFrameworkModule();
		fm.setModuleName(ModuleName.PerceptualAssociativeMemory);
		// modulesMap.put(ModuleName.PerceptualAssociativeMemory,fm);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Hello", 500);
		MockFrameworkTask t = (MockFrameworkTask) factory.getFrameworkTask(
				"testType", "defaultDecay", "defaultExcite", 100, 0.66, 0.77,
				params, modulesMap);

		assertTrue(t.getDecayStrategy() instanceof LinearDecayStrategy);
		assertTrue(t.getExciteStrategy() instanceof LinearExciteStrategy);

		assertTrue(t instanceof MockFrameworkTask);
		assertEquals(t.getTicksPerRun(), 100);
		assertTrue(t.getActivation() == 0.66);
		assertTrue(t.getActivatibleRemovalThreshold() == 0.77);
		int hello = (Integer) t.getParam("Hello", -1);
		assertEquals(500, hello);
		assertNull(t.moduleUsage);
		assertNull(t.associatedModule);
	}

	@Test
	public void testGetNodeStructure() {
		NodeStructure ns = factory.getNodeStructure();
		assertEquals(ns.getDefaultNodeType(), factory.getDefaultNodeType());
		assertEquals(ns.getDefaultLinkType(), factory.getDefaultLinkType());
	}

	@Test
	public void testGetNodeStructure1() {
		NodeStructure ns = factory.getNodeStructure("basdf", "asdfl");
		assertTrue(ns == null);

		ns = factory.getNodeStructure("LinkImpl", "NodeImpl");
		assertTrue(ns == null);

		ns = factory.getNodeStructure("NodeImpl", "ffsdfs");
		assertTrue(ns == null);

		ns = factory.getNodeStructure("Nsdfasdfl", "LinkImpl");
		assertTrue(ns == null);

		ns = factory.getNodeStructure(null, null);
		assertTrue(ns == null);

		ns = factory.getNodeStructure("PamNodeImpl", "LinkImpl");
		assertTrue(ns != null);
		assertEquals(ns.getDefaultNodeType(), "PamNodeImpl");
		assertEquals(ns.getDefaultLinkType(), "LinkImpl");
	}

	@Test
	public void testGetBehavior() {
		ProceduralMemoryImpl pm = new ProceduralMemoryImpl();
		Action a = new ActionImpl();
		Scheme s = pm.getNewScheme(a);
		Behavior b = factory.getBehavior(s);
		assertSame(s, b.getScheme());
		Behavior b2 = factory.getBehavior(s);
		assertNotSame(b, b2);
		assertSame(b.getScheme(), b2.getScheme());
	}

	@Test
	public void testGetBehavior0() {
		ProceduralMemoryImpl pm = new ProceduralMemoryImpl();
		Action a = new ActionImpl();
		Scheme s = pm.getNewScheme(a);
		String name = "edu.memphis.ccrg.lida.actionselection.BehaviorImpl";
		Behavior b = factory.getBehavior(s, name);
		assertSame(s, b.getScheme());
		Behavior b2 = factory.getBehavior(s, name);
		assertNotSame(b, b2);
		assertSame(b.getScheme(), b2.getScheme());
	}

	@Test
	public void testGetBehavior1() {
		Scheme s = null;
		String name = "edu.memphis.ccrg.lida.actionselection.BehaviorImpl";
		Behavior b = factory.getBehavior(s, name);
		assertNull(b);
	}

	@Test
	public void testGetBehavior2() {
		ProceduralMemoryImpl pm = new ProceduralMemoryImpl();
		Action a = new ActionImpl();
		Scheme s = pm.getNewScheme(a);
		String name = "BADNAME.BehaviorImpl";
		Behavior b = factory.getBehavior(s, name);
		assertNull(b);
	}

	@Test
	public void testGetBehavior3() {
		ProceduralMemoryImpl pm = new ProceduralMemoryImpl();
		Action a = new ActionImpl();
		Scheme s = pm.getNewScheme(a);
		String name = null;
		Behavior b = factory.getBehavior(s, name);
		assertNull(b);
	}

}
