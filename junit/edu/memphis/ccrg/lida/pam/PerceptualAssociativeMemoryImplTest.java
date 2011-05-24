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
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.mockclasses.MockDetectionAlgorithm;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;

/**
 * Tests {@link PerceptualAssociativeMemoryImpl}
 * @author Ryan J. McCall, Siminder Kaur
 */
public class PerceptualAssociativeMemoryImplTest {
	
	private PerceptualAssociativeMemoryImpl pam;
	private NodeStructure nodeStructure;
	private PamNodeImpl node1,node2,node3;
	private DecayStrategy decayStrategy ;
	private ExciteStrategy exciteStrat;
	private TotalActivationStrategy tas;
	private PamLinkImpl link1,link2;
	
	private static ElementFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}
	
	@Before
	public void setUp() throws Exception {		
		pam = new PerceptualAssociativeMemoryImpl();
		nodeStructure = new NodeStructureImpl("PamNodeImpl", "PamLinkImpl");

		node1 = (PamNodeImpl) factory.getNode("PamNodeImpl", "Node 1");
		node2 = (PamNodeImpl) factory.getNode("PamNodeImpl", "Node 2");
		node3 = (PamNodeImpl) factory.getNode("PamNodeImpl", "Node 3");
		decayStrategy = new SigmoidDecayStrategy();
		exciteStrat = new SigmoidExciteStrategy();
		tas = new TotalActivationStrategy() {
			@Override
			public double calculateTotalActivation(double bla, double ca) {
				return 0;
			}

			@Override
			public Object getParam(String name, Object defaultValue) {
				return null;
			}

			@Override
			public void init(Map<String, ?> parameters) {
				
			}

			@Override
			public void init() {
			}
		};
		
		link1 = (PamLinkImpl) factory.getLink("PamLinkImpl", node1, node2, PerceptualAssociativeMemoryImpl.NONE);
		link2 = (PamLinkImpl) factory.getLink("PamLinkImpl", node2, node3, PerceptualAssociativeMemoryImpl.NONE);
	}

	@Test
	public void testAddDefaultNode(){
		node2.setBaseLevelActivation(0.01);
		node2.setBaseLevelDecayStrategy(decayStrategy);
		node2.setBaseLevelExciteStrategy(exciteStrat);
		node2.setTotalActivationStrategy(tas);
		node2.setBaseLevelRemovalThreshold(0.9);
		
		PamNode res = pam.addDefaultNode(node2);
		
		assertTrue(res.getBaseLevelActivation()== node2.getBaseLevelActivation());
		assertEquals(res.getTotalActivationStrategy(), node2.getTotalActivationStrategy());
		assertTrue(res.getLearnableRemovalThreshold()== node2.getLearnableRemovalThreshold());
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addDefaultNodes(java.util.Set)}.
	 */
	@Test
	public void testAddDefaultNodes(){		
		Set<PamNode> nodes = new HashSet<PamNode>();
		node2.setBaseLevelActivation(0.01);
		node2.setBaseLevelDecayStrategy(decayStrategy);
		node2.setBaseLevelExciteStrategy(exciteStrat);
		node2.setTotalActivationStrategy(tas);
		node2.setBaseLevelRemovalThreshold(0.9);
		
		node3.setBaseLevelActivation(0.02);
		node3.setBaseLevelDecayStrategy(decayStrategy);
		node3.setBaseLevelExciteStrategy(exciteStrat);
		node3.setTotalActivationStrategy(tas);
		node3.setBaseLevelRemovalThreshold(0.99);
		
		nodes.add(node2);
		nodes.add(node3);
		Set<PamNode> results = pam.addDefaultNodes(nodes);
		for(PamNode res: results){
			assertTrue(nodes.contains(res));
			if(res.equals(node2)){
				assertTrue(res.getBaseLevelActivation()== node2.getBaseLevelActivation());
				assertEquals(res.getTotalActivationStrategy(), node2.getTotalActivationStrategy());
				assertTrue(res.getLearnableRemovalThreshold()== node2.getLearnableRemovalThreshold());
			}else if(res.equals(node3)){
				assertTrue(res.getBaseLevelActivation()== node3.getBaseLevelActivation());
				assertEquals(res.getTotalActivationStrategy(), node3.getTotalActivationStrategy());
				assertTrue(res.getLearnableRemovalThreshold()== node3.getLearnableRemovalThreshold());
			}else{
				assertFalse(true);
			}
		}
		assertTrue(pam.containsNode(node2));
		assertTrue(pam.containsNode(node3));
		assertEquals(2, pam.getNodes().size() - pam.getLinkCategories().size());
	}
	@Test
	public void testAddNullNodes(){
		Set<PamNode> results = pam.addDefaultNodes(null);
		assertTrue(results == null);
		assertTrue(pam.getNodes().size() - pam.getLinkCategories().size() == 0);
	}
	@Test
	public void testAddNodeNodeType(){
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(factory.getNode());
		nodes.add(factory.getNode());
		pam.addDefaultNodes(nodes);
		assertEquals(2, pam.getNodes().size() - pam.getLinkCategories().size());
 	}
	@Test
	public void testAddDefNode(){
		PamNode res = pam.addDefaultNode(null);
		assertTrue(res == null);
		assertEquals(0, pam.getNodes().size() - pam.getLinkCategories().size());
	}
	@Test
	public void testAddDefNode2(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node1);
		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
	}
	@Test
	public void testAddDefNode1(){
		pam.addDefaultNode(node1);
		assertTrue(pam.containsNode(node1));
		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void testAddDefaultLinks(){
		pam.addDefaultNode(node1);	
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);
		
		Set<PamLink> links = new HashSet<PamLink>();
		
		link1.setBaseLevelActivation(0.01);
		link1.setBaseLevelDecayStrategy(decayStrategy);
		link1.setBaseLevelExciteStrategy(exciteStrat);
		link1.setTotalActivationStrategy(tas);
		link1.setBaseLevelRemovalThreshold(0.9);
		
		links.add(link1);
		links.add(link2);
		
		Set<PamLink> storedLinks = pam.addDefaultLinks(links);
		for(PamLink pLink: storedLinks){
			assertTrue(links.contains(pLink));
			if(pLink.equals(link1)){
				assertTrue(pLink.getBaseLevelActivation()== link1.getBaseLevelActivation());
//				assertEquals(pLink.getBaseLevelDecayStrategy(), link1.getBaseLevelDecayStrategy());
//				assertEquals(pLink.getBaseLevelExciteStrategy(), link1.getBaseLevelExciteStrategy());
				assertEquals(pLink.getTotalActivationStrategy(), link1.getTotalActivationStrategy());
				assertTrue(pLink.getLearnableRemovalThreshold()== link1.getLearnableRemovalThreshold());
			}
		}
		assertTrue(pam.containsLink(link1));
		assertTrue(pam.containsLink(link2));
		assertEquals(2, pam.getLinks().size());
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addPerceptualAlgorithm(edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm)}.
	 */
	@Test
	public void testAddFeatureDetector() {	
		DetectionAlgorithm detector = new MockDetectionAlgorithm(node1, null, pam);
		pam.setAssistingTaskSpawner(new MockTaskSpawner());
		pam.addPerceptualAlgorithm(detector);
		assertTrue(pam.getAssistingTaskSpawner().containsTask(detector));
		assertEquals(1, pam.getAssistingTaskSpawner().getRunningTasks().size());
	}
	@Test
	public void testDetectionAlgorithm1() {	
		DetectionAlgorithm detector = new MockDetectionAlgorithm(node1, null, pam);
		pam.setAssistingTaskSpawner(new MockTaskSpawner());
		pam.addPerceptualAlgorithm(detector);
		pam.addPerceptualAlgorithm(detector);
		assertTrue(pam.getAssistingTaskSpawner().containsTask(detector));
		assertEquals(2, pam.getAssistingTaskSpawner().getRunningTasks().size());
	}
	@Test
	public void testAddPamListener(){
		MockPamListener pl = new MockPamListener();
		pam.addPamListener(pl);
		pam.addNodeStructureToPercept(new NodeStructureImpl());
		
		NodeStructure ns = pl.ns;
		assertEquals(0,ns.getNodeCount());
		assertEquals(0,ns.getLinkCount());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#setPropagationStrategy(edu.memphis.ccrg.lida.pam.PropagationStrategy)}.
	 */
	@Test
	public void testSetPropagationStrategy() {
		PropagationStrategy ps = new UpscalePropagationStrategy();
		pam.setPropagationStrategy(ps);		
		assertEquals(ps, pam.getPropagationStrategy());
	}
	
	private final double epsilon = 0.000001;
	
	@Test
	public void testPropagateActivationToParents(){
		Node testNod1= factory.getNode("PamNodeImpl");
		testNod1.setActivation(1.0);
		testNod1.setLabel("Node1");
		Node testNod2= factory.getNode("PamNodeImpl");
		testNod2.setActivation(0.2);
		testNod2.setLabel("Node2");
		Node testNod3= factory.getNode("PamNodeImpl");
		testNod3.setActivation(0.3);
		testNod3.setLabel("Node3");
		Node testNod4= factory.getNode("PamNodeImpl");
		testNod4.setActivation(1.0);
		testNod4.setLabel("Node4");
		testNod1 = pam.addDefaultNode(testNod1);
		testNod2 = pam.addDefaultNode(testNod2);
		testNod3 = pam.addDefaultNode(testNod3);
		testNod4 = pam.addDefaultNode(testNod4);
		
		PamLinkImpl l12 = (PamLinkImpl) factory.getLink("PamLinkImpl", testNod1, testNod2, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l12);
		PamLinkImpl l13 = (PamLinkImpl) factory.getLink("PamLinkImpl", testNod1, testNod3, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l13);
		PamLinkImpl l41 = (PamLinkImpl) factory.getLink("PamLinkImpl", testNod4, testNod1, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l41);
		
		assertEquals(1.0,testNod1.getActivation(), epsilon);
		assertEquals(0.2,testNod2.getActivation(), epsilon);
		assertEquals(0.3,testNod3.getActivation(), epsilon);
		assertEquals(1.0,testNod4.getActivation(), epsilon);
		assertEquals(Activatible.DEFAULT_ACTIVATION,l12.getActivation(), epsilon);
		assertEquals(Activatible.DEFAULT_ACTIVATION,l13.getActivation(), epsilon);
		assertEquals(Activatible.DEFAULT_ACTIVATION,l41.getActivation(), epsilon);
		
		NodeStructure ns = (NodeStructure) pam.getModuleContent();
		
		assertEquals(4,ns.getNodeCount() - pam.getLinkCategories().size());
		assertEquals(3,ns.getLinkCount());
		
		assertEquals(2,ns.getConnectedSinks(testNod1).size());
		assertEquals(0,ns.getConnectedSinks(testNod2).size());
		assertEquals(0,ns.getConnectedSinks(testNod3).size());
		assertEquals(1,ns.getConnectedSinks(testNod4).size());
		
		assertEquals(1,ns.getConnectedSources(testNod1).size());
		assertEquals(1,ns.getConnectedSources(testNod2).size());
		assertEquals(1,ns.getConnectedSources(testNod3).size());
		assertEquals(0,ns.getConnectedSources(testNod4).size());
		
		TaskSpawner ts = new PamMockTaskSpawner();
		pam.setAssistingTaskSpawner(ts);
		
		double upscaleFactor = 0.1;
		pam.setUpscaleFactor(upscaleFactor);
		pam.propagateActivationToParents((PamNode) testNod1);
		
		//Should be 2 tasks to pass the activation to Nodes 2, 3.
		Collection<FrameworkTask> tasks = ts.getRunningTasks();
		assertEquals(2, tasks.size());
		
		assertEquals(testNod3.getActivation()+"", testNod3.getActivation(), (0.4 + Activatible.DEFAULT_ACTIVATION), epsilon);
		assertEquals(testNod2.getActivation()+"",testNod2.getActivation(), (0.3 + Activatible.DEFAULT_ACTIVATION), epsilon);
		
		double amount = 1.0 * upscaleFactor + Activatible.DEFAULT_ACTIVATION;
		assertEquals(amount, pam.getLink(l12.getExtendedId()).getActivation(), epsilon);
		assertEquals(amount, pam.getLink(l13.getExtendedId()).getActivation(), epsilon);
		
		assertEquals(1.0, testNod4.getActivation(), epsilon);
		assertEquals(Activatible.DEFAULT_ACTIVATION, l41.getActivation(), epsilon);		
	}
	@Test
	public void testAddNodeStructureToPercept(){
		MockPamListener pl = new MockPamListener();
		pam.addPamListener(pl);
		nodeStructure.addDefaultNode(node1);
		nodeStructure.addDefaultNode(node2);
		nodeStructure.addDefaultNode(node3);
		
		pam.addNodeStructureToPercept(nodeStructure);
		
		NodeStructure ns = pl.ns;
		assertEquals(3, ns.getNodeCount());
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
		assertTrue(ns.containsNode(node3));
		assertEquals(0, ns.getLinkCount());
	}
	@Test
	public void testContainsNode(){
		pam.addDefaultNode(node1);
		assertTrue(pam.containsNode(node1));
		
		node1 = null;
		try{
			assertTrue(pam.containsNode(node1));
		}catch(Exception e){
			assertTrue(e instanceof NullPointerException);
		}
		
		assertTrue(pam.getNodes().size() - pam.getLinkCategories().size() == 1);
	}
	@Test
	public void testContainsNode0(){
		node2.setBaseLevelRemovalThreshold(0.1111);
		node2.setBaseLevelActivation(1.0);
		node2 = (PamNodeImpl) pam.addDefaultNode(node2);	
		assertTrue(pam.containsNode(node2));
		//TODO
//		pam.decayModule(1000);
	}
	@Test
	public void testContainsNodeEid(){
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		ExtendedId eid = new ExtendedId(node1.getId());
		assertTrue(pam.containsNode(eid));
		
		eid = new ExtendedId(node1.getId() + 1);
		assertFalse(pam.containsNode(eid));
	}
	@Test
	public void testContainsLink(){
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		node1.setBaseLevelRemovalThreshold(0.0);
		node1.setActivation(0.0);
		node1.setBaseLevelActivation(0.0);
		assertTrue(pam.containsNode(node1));
		
		//TODO
//		pam.decayModule(1);
	}
	@Test
	public void testContainsLink1(){
		PamNode a = pam.addDefaultNode(node1);
		PamNode b = pam.addDefaultNode(node2);
		Link l = factory.getLink("PamLinkImpl", a, b, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l);
		assertTrue(pam.containsLink(l));
		assertFalse(pam.containsLink(this.link2));
		assertTrue(pam.containsLink(l.getExtendedId()));
	}
	@Test
	public void testSetPerceptThreshold(){
		pam.setPerceptThreshold(0.7);
		
		pam.setPerceptThreshold(-1);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.7);
		
		pam.setPerceptThreshold(231);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.7);
		
		pam.setPerceptThreshold(0.3487);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.3487);
		
		pam.setPerceptThreshold(0.0);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.0);
		
		pam.setPerceptThreshold(1.0);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 1.0);
	}
	@Test
	public void testUpscaleFactor(){
		pam.setUpscaleFactor(-1.0);
		assertTrue(0.0== pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(123);
		assertTrue(1.0== pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(0.5);
		assertTrue(0.5== pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(0.0);
		assertTrue(0.0== pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(1.0);
		assertTrue(1.0== pam.getUpscaleFactor());
	}
	@Test
	public void testDownscaleFactor(){
		pam.setDownscaleFactor(-1.0);
		assertTrue(0.0== pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(123);
		assertTrue(1.0== pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(0.5);
		assertTrue(0.5== pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(0.0);
		assertTrue(0.0== pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(1.0);
		assertTrue(1.0== pam.getDownscaleFactor());
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
	public void testGetPamNode(){
		Node n = pam.getNode(223042328);
		assertEquals(null, n);
		
		pam.addDefaultNode(node3);
		n = pam.getNode(node3.getId());
		assertEquals(node3, n);
		
		node2.setId(69);
		pam.addDefaultNode(node2);
		n = (PamNode) pam.getNode(69);
		assertEquals(node2, n);
		
		//extended id
		n = pam.getNode(node3.getExtendedId());
		assertEquals(node3, n);
		
		n = pam.getNode(new ExtendedId(70));
		assertEquals(null, n);
	}
	@Test
	public void testGetPamLink(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		Link l = factory.getLink("PamLinkImpl", node1, node2, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l);
		
		assertEquals(l, pam.getLink(l.getExtendedId()));
		assertEquals(null, pam.getLink(new ExtendedId(99, new ExtendedId(11), 99)));
		
		ExtendedId id2 = new ExtendedId(node1.getId(), node2.getExtendedId(), PerceptualAssociativeMemoryImpl.NONE.getId());
		assertEquals(l, pam.getLink(id2));
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#taskManagerDecayModule(long)}.
	 * @see LinearDecayStrategy
	 */
	@Test
	public void testDecayModule() {
		//TODO
//		node1.setBaseLevelActivation(1.0);
//		node1.setLearnableRemovalThreshold(0.0001);
//		node1.setDecayStrategy(decayStrategy);
//		node1 = (PamNodeImpl) pam.addDefaultNode(node1);	
//		assertTrue(pam.containsNode(node1));
//		assertFalse(node1.isRemovable());
//		
//		pam.decayModule(100);
//		assertTrue(node1.getBaseLevelActivation()+"",node1.getBaseLevelActivation() <= 0.001);
//		assertFalse(pam.containsNode(node1));
	}
	
	@Test
	public void testGetPamNodes(){
		Collection<Node> nodes = pam.getNodes();
		assertEquals(0, nodes.size() - pam.getLinkCategories().size());
		try{
			nodes.clear();
			assertTrue(false);
		}catch(UnsupportedOperationException e){
		}
		
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		
		nodes = pam.getNodes();
		assertTrue(nodes.size() - pam.getLinkCategories().size() == 2);		
	}
	@Test
	public void testGetPamLinks(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);
		
		Collection<Link> links = pam.getLinks();
		assertTrue(links.size() == 0);
		try{
			links.remove(link1);
			assertTrue(false);
		}catch(UnsupportedOperationException e){
		}
		Link l = factory.getLink("PamLinkImpl", node1, node2, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l);
		
		l = factory.getLink("PamLinkImpl", node2, node3, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l);
		
		l = factory.getLink("PamLinkImpl", node3, node1, PerceptualAssociativeMemoryImpl.NONE);
		pam.addDefaultLink(l);
				
		links = pam.getLinks();
		assertTrue(links.size() == 3);		
	}
	@Test
	public void testGetLinkCategories() {
		Collection<LinkCategory> cats = pam.getLinkCategories();
		//This assumes PamNode will be LinkCategory which is reasonable
		assertTrue(cats.size() == pam.getNodes().size());
		
		for(LinkCategory cat: cats){
			assertTrue(cat instanceof PamNode);
		}
	}
	@Test
	public void testGetLinkCategory() {
		int id = PerceptualAssociativeMemoryImpl.NONE.getId();
		assertEquals(PerceptualAssociativeMemoryImpl.NONE, pam.getLinkCategory(id));
		
		Node newCat = factory.getNode("PamNodeImpl");
		newCat.setId(666);
		pam.addLinkCategory((LinkCategory) newCat);
		assertTrue(pam.getLinkCategory(666) != null);
		assertEquals(pam.getLinkCategory(666), newCat);
	}
	
	@Test
	public void testAddLinkCategory(){
		int categoryCount = pam.getLinkCategories().size();
		
		LinkCategory lc = pam.addLinkCategory(null);
		
		assertEquals(null, lc);
		assertEquals(categoryCount, pam.getLinkCategories().size());
		
		lc = pam.addLinkCategory(node1);
		
		assertEquals(node1, lc);
		assertEquals(categoryCount+1, pam.getLinkCategories().size());
	}

}
