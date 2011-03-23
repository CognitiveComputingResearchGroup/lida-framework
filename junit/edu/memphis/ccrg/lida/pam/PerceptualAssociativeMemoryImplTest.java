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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.BasicDetector;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;

/**
 * Tests {@link PerceptualAssociativeMemoryImpl}
 * @author Ryan J. McCall, Siminder Kaur
 */
public class PerceptualAssociativeMemoryImplTest extends TestCase{
	
	private PerceptualAssociativeMemoryImpl pam;
	private NodeStructure nodeStructure;
	private PamNodeImpl node1,node2,node3;
	private DecayStrategy decayStrategy ;
	private ExciteStrategy exciteStrat;
	private TotalActivationStrategy tas;
	private PamLinkImpl link1,link2;
	
	private LidaElementFactory factory = LidaElementFactory.getInstance();
	
	@Override
	@Before
	public void setUp() throws Exception {
		pam = new PerceptualAssociativeMemoryImpl();
		nodeStructure = factory.getPamNodeStructure();

		node1 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName, "Node 1");
		node2 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName, "Node 2");
		node3 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName, "Node 3");
		decayStrategy = new SigmoidDecayStrategy();
		exciteStrat = new SigmoidExciteStrategy();
		tas = new TotalActivationStrategy() {
			@Override
			public double calculateTotalActivation(double bla, double ca) {
				return 0;
			}
		};
		
		link1 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node1, node2, LinkCategoryNode.CHILD);
		link2 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node2, node3, LinkCategoryNode.CHILD);
	}

	@Test
	public void testAddNewNode() {
		PamNodeImpl pn = (PamNodeImpl) pam.addNewNode("BOB");
		assertTrue(pn != null);
		assertTrue(pn.getLabel().equals("BOB"));
		assertTrue(pam.containsNode(pn));
		assertTrue(pam.getPamNodes().size() == 1);
		Node other = pam.getPamNode(pn.getId());
		assertTrue(other.equals(pn));
	}
	
	public void testAddNullString(){
		PamNodeImpl pn = (PamNodeImpl) pam.addNewNode(null);
		assertTrue(pn != null);
		assertTrue(pam.containsNode(pn));
		assertTrue(pam.getPamNodes().size() == 1);
		Node other = pam.getPamNode(pn.getId());
		assertTrue(other.equals(pn));
	}
	
	public void testAddNewNode2(){
		PamNode pn = pam.addNewNode(PamNodeImpl.factoryName, "BOB");
		assertTrue(pn != null);
		assertTrue(pn instanceof PamNodeImpl);
		assertTrue(pn.getLabel().equals("BOB"));	
		assertTrue(pam.getPamNodes().size() == 1);
		Node other = pam.getPamNode(pn.getId());
		assertTrue(other.equals(pn));
	}
	
	public void testAddNewNode3(){
		PamNode pn = pam.addNewNode("BAD5TYPE#T$", "BOB");
		assertTrue(pn == null);	
		assertTrue(pam.getPamNodes().size() == 0);
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#addDefaultNodes(java.util.Set)}.
	 * @throws Exception e
	 */
	@Test
	public void testAddDefaultNodes() throws Exception {		
		Set<PamNode> nodes = new HashSet<PamNode>();
		node2.setBaseLevelActivation(0.01);
		node2.setBaseLevelDecayStrategy(decayStrategy);
		node2.setBaseLevelExciteStrategy(exciteStrat);
		node2.setTotalActivationStrategy(tas);
		node2.setLearnableRemovalThreshold(0.9);
		
		node3.setBaseLevelActivation(0.02);
		node3.setBaseLevelDecayStrategy(decayStrategy);
		node3.setBaseLevelExciteStrategy(exciteStrat);
		node3.setTotalActivationStrategy(tas);
		node3.setLearnableRemovalThreshold(0.99);
		
		nodes.add(node2);
		nodes.add(node3);
		Set<PamNode> results = pam.addDefaultNodes(nodes);
		for(PamNode res: results){
			assertTrue(nodes.contains(res));
			if(res.equals(node2)){
				assertEquals(res.getBaseLevelActivation(), node2.getBaseLevelActivation());
				assertEquals(res.getBaseLevelDecayStrategy(), node2.getBaseLevelDecayStrategy());
				assertEquals(res.getBaseLevelExciteStrategy(), node2.getBaseLevelExciteStrategy());
				assertEquals(res.getTotalActivationStrategy(), node2.getTotalActivationStrategy());
				assertEquals(res.getLearnableRemovalThreshold(), node2.getLearnableRemovalThreshold());
			}else if(res.equals(node3)){
				assertEquals(res.getBaseLevelActivation(), node3.getBaseLevelActivation());
				assertEquals(res.getBaseLevelDecayStrategy(), node3.getBaseLevelDecayStrategy());
				assertEquals(res.getBaseLevelExciteStrategy(), node3.getBaseLevelExciteStrategy());
				assertEquals(res.getTotalActivationStrategy(), node3.getTotalActivationStrategy());
				assertEquals(res.getLearnableRemovalThreshold(), node3.getLearnableRemovalThreshold());
			}else{
				throw new Exception();
			}
		}
		assertTrue("Problem with AddNodes", pam.containsNode(node2) && pam.containsNode(node3));
		assertTrue(pam.getPamNodes().size() == 2);
	}
	
	public void testAddNullNodes(){
		Set<PamNode> results = pam.addDefaultNodes(null);
		assertTrue(results == null);
		assertTrue(pam.getPamNodes().size() == 0);
	}
	
	public void testAddNodeNodeType(){
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(factory.getNode());
		nodes.add(factory.getNode());
		pam.addDefaultNodes(nodes);
		assertTrue(pam.getPamNodes().size() == 2);
 	}
	
	public void testAddDefNode(){
		PamNode res = pam.addDefaultNode(null);
		assertTrue(res == null);
		assertTrue(pam.getPamNodes().size() == 0);
	}
	
	public void testAddDefNode2(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node1);
		assertTrue(pam.getPamNodes().size() == 1);
	}
	
	public void testAddDefNode1(){
		pam.addDefaultNode(node1);
		assertTrue(pam.containsNode(node1));
		assertTrue(pam.getPamNodes().size() == 1);
	}
	
	public void testAddNewLink(){
		Link l = pam.addNewLink(node1, node2, null, 0.0);
		Link l2 = pam.addNewLink(node3.getExtendedId(), node2.getExtendedId(), null, 0.0);
		assertEquals(l, null);
		assertEquals(l2, null);
		assertEquals(0, pam.getPamLinks().size());
	}
	
	public void testAddNewLink2(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);
		Link l = pam.addNewLink(node1, node2, LinkCategoryNode.CHILD, 0.0);
		assertEquals(pam.getPamLink(l.getExtendedId()), l);
		assertEquals(1, pam.getPamLinks().size());
		
		Link l2 = pam.addNewLink(node2.getExtendedId(), node3.getExtendedId(), LinkCategoryNode.CHILD, 0.0);
		assertEquals(pam.getPamLink(l2.getExtendedId()), l2);
		assertEquals(2, pam.getPamLinks().size());
		
		pam.addNewLink(node2.getExtendedId(), node3.getExtendedId(), LinkCategoryNode.CHILD, 0.0);
		assertEquals(2, pam.getPamLinks().size());
		
	}

	public void testAddDefaultLinks(){
		pam.addDefaultNode(node1);	
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);
		
		Set<PamLink> links = new HashSet<PamLink>();
		
		link1.setBaseLevelActivation(0.01);
		link1.setBaseLevelDecayStrategy(decayStrategy);
		link1.setBaseLevelExciteStrategy(exciteStrat);
		link1.setTotalActivationStrategy(tas);
		link1.setLearnableRemovalThreshold(0.9);
		
		links.add(link1);
		links.add(link2);
		
		Set<PamLink> storedLinks = pam.addDefaultLinks(links);
		for(PamLink pLink: storedLinks){
			assertTrue(links.contains(pLink));
			if(pLink.equals(link1)){
				assertEquals(pLink.getBaseLevelActivation(), link1.getBaseLevelActivation());
				assertEquals(pLink.getBaseLevelDecayStrategy(), link1.getBaseLevelDecayStrategy());
				assertEquals(pLink.getBaseLevelExciteStrategy(), link1.getBaseLevelExciteStrategy());
				assertEquals(pLink.getTotalActivationStrategy(), link1.getTotalActivationStrategy());
				assertEquals(pLink.getLearnableRemovalThreshold(), link1.getLearnableRemovalThreshold());
			}
		}
		assertTrue(pam.containsLink(link1));
		assertTrue(pam.containsLink(link2));
		assertTrue(pam.getPamLinks().size() == 2);
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
		assertTrue(pam.getAssistingTaskSpawner().getRunningTasks().size() == 1);
	}
	
	public void testAddFeatureDetector1() {	
		FeatureDetector detector = new BasicDetector(node1, null, pam);
		pam.setAssistingTaskSpawner(new MockTaskSpawner());
		pam.addFeatureDetector(detector);
		pam.addFeatureDetector(detector);
		assertTrue("Problem with AddFeatureDetector", pam.getAssistingTaskSpawner().containsTask(detector));
		assertTrue(pam.getAssistingTaskSpawner().getRunningTasks().size() == 2);
	}

	public void testAddPamListener(){
		PamListener pl = new PamListener() {
			@Override
			public void receivePercept(NodeStructure ns) {
				assertTrue(ns.getNodeCount() == 0);
				assertTrue(ns.getLinkCount() == 0);
			}
		};
		pam.addPamListener(pl);
		pam.addNodeStructureToPercept(new NodeStructureImpl());
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
	
	
	public void testPropagateActivationToParents(){
		double upscaleFactor= 0.1;
		pam.setUpscaleFactor(upscaleFactor);
		Node testNod1= factory.getNode("PamNodeImpl");
		testNod1.setActivation(1.0);
		Node testNod2= factory.getNode("PamNodeImpl");
		testNod2.setActivation(0.2);
		Node testNod3= factory.getNode("PamNodeImpl");
		testNod3.setActivation(0.3);
		Node testNod4= factory.getNode("PamNodeImpl");
		testNod4.setActivation(1.0);
		testNod1 = pam.addDefaultNode(testNod1);
		testNod2 = pam.addDefaultNode(testNod2);
		testNod3 = pam.addDefaultNode(testNod3);
		testNod4 = pam.addDefaultNode(testNod4);
		Link l12 = pam.addNewLink(testNod1, testNod2, LinkCategoryNode.CHILD, 0.0);
		Link l13 = pam.addNewLink(testNod1, testNod3, LinkCategoryNode.CHILD, 0.0);
		Link l41 = pam.addNewLink(testNod4, testNod1, LinkCategoryNode.CHILD, 0.0);
		
		assertTrue(testNod4.getActivation() == 1.0);
		assertTrue(l41.getActivation() == 0.0);
		
		NodeStructure ns = (NodeStructure) pam.getModuleContent();
		
		assertTrue(ns.getNodeCount() == 4);
		assertTrue(ns.getLinkCount() == 3);
		
		assertTrue(ns.getConnectedSinks(testNod1).size() == 2);
		assertTrue(ns.getConnectedSinks(testNod2).size() == 0);
		assertTrue(ns.getConnectedSinks(testNod3).size() == 0);
		assertTrue(ns.getConnectedSinks(testNod4).size() == 1);
		
		TaskSpawner ts = new MockTaskSpawner();
		pam.setAssistingTaskSpawner(ts);
		
		pam.propagateActivationToParents((PamNode) testNod1);
		
		Collection<LidaTask> tasks = ts.getRunningTasks();
		assertTrue(tasks.size() == 2);
		
		double amount = 1.0 * upscaleFactor;
		
		assertTrue(l12.getActivation() == amount);
		assertTrue(l13.getActivation() == amount);
		
		assertTrue(testNod2.getActivation() >= 0.3 && testNod2.getActivation() <= 0.3001);
		assertTrue(testNod3.getActivation() == 0.4);
		
		assertTrue(testNod4.getActivation() == 1.0);
		assertTrue(l41.getActivation() == 0.0);		
	}
	
	public void testAddNodeStructureToPercept(){
		final int numNodesInPercept = 3;
		
		PamListener pl = new PamListener() {
			@Override
			public void receivePercept(NodeStructure ns) {
				assertEquals(numNodesInPercept, ns.getNodeCount());
				assertTrue(ns.containsNode(node1));
				assertTrue(ns.containsNode(node2));
				assertTrue(ns.containsNode(node3));
				assertEquals(0, ns.getLinkCount());
			}
		};
		
		pam.addPamListener(pl);
		nodeStructure.addDefaultNode(node1);
		nodeStructure.addDefaultNode(node2);
		nodeStructure.addDefaultNode(node3);
		pam.addNodeStructureToPercept(nodeStructure);
	}
	
	public void testContainsNode(){
		pam.addDefaultNode(node1);
		assertTrue(pam.containsNode(node1));
		
		node1 = null;
		try{
			assertTrue(pam.containsNode(node1));
		}catch(Exception e){
			assertTrue(e instanceof NullPointerException);
		}
		
		assertTrue(pam.getPamNodes().size() == 1);
		
		Node foo = pam.addNewNode("foo");
		assertTrue(pam.containsNode(foo));
		
		Node bar = pam.addNewNode(PamNodeImpl.factoryName, "bar");
		assertTrue(pam.containsNode(bar));

	}
	
	public void testContainsNode0(){
		node2.setLearnableRemovalThreshold(0.1111);
		node2.setBaseLevelActivation(1.0);
		node2 = (PamNodeImpl) pam.addDefaultNode(node2);	
		pam.decayModule(1000);
		assertFalse(pam.containsNode(node2));
	}
	
	public void testContainsNodeEid(){
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		ExtendedId eid = new ExtendedId(node1.getId());
		assertTrue(pam.containsNode(eid));
		
		eid = new ExtendedId(node1.getId() + 1);
		assertFalse(pam.containsNode(eid));
	}
	
	public void testContainsLink(){
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);
		node1.setLearnableRemovalThreshold(0.0);
		node1.setActivation(0.0);
		node1.setBaseLevelActivation(0.0);
		pam.decayModule(1);
		assertTrue(pam.containsNode(node1)+"", !pam.containsNode(node1));
	}
	
	public void testContainsLink1(){
		PamNode a = pam.addNewNode("A");
		PamNode b = pam.addNewNode("B");
		Link l = pam.addNewLink(a, b, LinkCategoryNode.NONE, 0.0);
		assertTrue(pam.containsLink(l));
		assertFalse(pam.containsLink(this.link1));
		assertFalse(pam.containsLink(this.link2));
		assertTrue(pam.containsLink(l.getExtendedId()));
	}
	
	public void setPerceptThreshold(double t){
		pam.setPerceptThreshold(-1);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.0);
		
		pam.setPerceptThreshold(231);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 1.0);
		
		pam.setPerceptThreshold(0.3487);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.3487);
		
		pam.setPerceptThreshold(0.0);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 0.0);
		
		pam.setPerceptThreshold(1.0);
		assertTrue(PerceptualAssociativeMemoryImpl.getPerceptThreshold() == 1.0);
	}
	
	public void testUpscaleFactor(){
		pam.setUpscaleFactor(-1.0);
		assertEquals(0.0, pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(123);
		assertEquals(1.0, pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(0.5);
		assertEquals(0.5, pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(0.0);
		assertEquals(0.0, pam.getUpscaleFactor());
		
		pam.setUpscaleFactor(1.0);
		assertEquals(1.0, pam.getUpscaleFactor());
	}
	
	public void testDownscaleFactor(){
		pam.setDownscaleFactor(-1.0);
		assertEquals(0.0, pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(123);
		assertEquals(1.0, pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(0.5);
		assertEquals(0.5, pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(0.0);
		assertEquals(0.0, pam.getDownscaleFactor());
		
		pam.setDownscaleFactor(1.0);
		assertEquals(1.0, pam.getDownscaleFactor());
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

	public void testGetPamNode(){
		Node n = pam.getPamNode(223042328);
		assertEquals(null, n);
		
		pam.addDefaultNode(node3);
		n = pam.getPamNode(node3.getId());
		assertEquals(node3, n);
		
		node2.setId(69);
		pam.addDefaultNode(node2);
		n = (PamNode) pam.getPamNode(69);
		assertEquals(node2, n);
		
		//extended id
		n = pam.getPamNode(node3.getExtendedId());
		assertEquals(node3, n);
		
		n = pam.getPamNode(new ExtendedId(70));
		assertEquals(null, n);
	}
	
	public void testGetPamLink(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		Link l = pam.addNewLink(node1, node2, LinkCategoryNode.NONE, 0.0);
		
		assertEquals(l, pam.getPamLink(l.getExtendedId()));
		assertEquals(null, pam.getPamLink(new ExtendedId(99, 99, new ExtendedId(11))));
		
		ExtendedId id2 = new ExtendedId(LinkCategoryNode.NONE.getId(), node1.getId(), node2.getExtendedId());
		assertEquals(l, pam.getPamLink(id2));
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl#decayModule(long)}.
	 * @see LinearDecayStrategy
	 */
	@Test
	public void testDecayModule() {		
		node1.setBaseLevelActivation(1.0);
		node1.setLearnableRemovalThreshold(0.0);
		node1.setDecayStrategy(decayStrategy);
		node1 = (PamNodeImpl) pam.addDefaultNode(node1);	
		pam.decayModule(100);
		assertTrue(node1.getBaseLevelActivation()+"",node1.getBaseLevelActivation() == 0.0);
		assertFalse(pam.containsNode(node1));
	}
	
	public void testGetPamNodes(){
		Collection<Node> nodes = pam.getPamNodes();
		assertEquals(0, nodes.size());
		try{
			nodes.clear();
		}catch(Exception e){
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		pam.addDefaultNode(node1);
		pam.addNewNode("foo");
		
		nodes = pam.getPamNodes();
		assertTrue(nodes.size() == 2);		
	}
	
	public void testGetPamLinks(){
		pam.addDefaultNode(node1);
		pam.addDefaultNode(node2);
		pam.addDefaultNode(node3);
		
		Collection<Link> links = pam.getPamLinks();
		assertTrue(links.size() == 0);
		try{
			links.remove(link1);
		}catch(Exception e){
			assertTrue(e instanceof UnsupportedOperationException);
		}
		
		pam.addNewLink(node1, node2, LinkCategoryNode.NONE, 0.0);
		pam.addNewLink(node2, node3, LinkCategoryNode.NONE, 0.0);
		pam.addNewLink(node3, node1, LinkCategoryNode.NONE, 0.0);
		
		links = pam.getPamLinks();
		assertTrue(links.size() == 3);		
	}
	

}
