package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

public class SubNodeStructureImplTest2 {

	private static ElementFactory factory;
	private Node node1,node2,node3,node4,node5,node6,node7;
	private Link link1,link2,link3,link4,link5,link6,link7,link8;
	private SubNodeStructureImpl ns1;
	private PamNode category1,category2,category3,category4;
	
	Collection<Node> nodes = new ArrayList<Node>();

	NodeStructure subNS;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		node1 = factory.getNode();
		node1.setLabel("red");
		node1.setActivation(0.1);

		node2 = factory.getNode();
		node2.setLabel("blue");
		node2.setActivation(0.2);

		node3 = factory.getNode();
		node3.setLabel("purple");
		node3.setActivation(0.3);
		
		node4 = factory.getNode();
		node4.setLabel("purpl");
		node4.setActivation(0.4);
		
		node5 = factory.getNode();
		node5.setLabel("purp");
		node5.setActivation(0.5);
		
		node6 = factory.getNode();
		node6.setLabel("purp2");
		node6.setActivation(0.5);
		
		node7 = factory.getNode();
		node7.setLabel("purp3");
		node7.setActivation(0.5);
		
		category1 = new PamNodeImpl();
		category1.setId(99999);
		category2 = new PamNodeImpl();
		category2.setId(100000);
		
		link1 = factory.getLink(node1, node3, category1);
		link2 = factory.getLink(node4, node3, category2);
		link3 = factory.getLink(node3, node5, category2);
		link4 = factory.getLink(node4, node5, category2);
		link5 = factory.getLink(node2, node5, category2);
		link6 = factory.getLink(node3, link3, category2); 
		link7 = factory.getLink(node6, node4, category1);
		link8 = factory.getLink(node7, node4, category1);
		
		ns1 = new SubNodeStructureImpl();	
		
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);
		ns1.addDefaultNode(node5);
		ns1.addDefaultNode(node6);
		
		ns1.addDefaultLink(link1);
		ns1.addDefaultLink(link2);
		ns1.addDefaultLink(link3);
		
		ns1.addDefaultLink(link4);
		ns1.addDefaultLink(link5);
		ns1.addDefaultLink(link6);
		ns1.addDefaultLink(link7);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSubNodeStructure1() {

		/*
		 * test for different distance value (-1, 0 , 1, 2, 100)
		 */
		nodes.add(node4);

		//distance = -1
		subNS = ns1.getSubNodeStructure(nodes, -1);
		
		assertTrue(!subNS.containsNode(node4));
		assertTrue(subNS.getNodeCount() == 0);
		assertTrue(subNS.getLinkCount() == 0);

		//distance = 0
		subNS= ns1.getSubNodeStructure(nodes, 0);
		
		assertTrue(subNS.containsNode(node4));
		assertTrue(subNS.getNodeCount() == 1);
		assertTrue(subNS.getLinkCount() == 0);
		
		nodes.clear();
		nodes.add(node3);
		nodes.add(node5);
		
		subNS= ns1.getSubNodeStructure(nodes, 0);
		
		assertTrue(subNS.containsNode(node3));
		assertTrue(subNS.containsNode(node5));
		assertTrue(subNS.getNodeCount() == 2);
		assertTrue(subNS.getLinkCount() == 2);
		
		nodes.clear();
		nodes.add(node4);
		
		//distance = 1
		subNS= ns1.getSubNodeStructure(nodes, 1);
		
		assertTrue(subNS.containsNode(node4));
		assertTrue(subNS.containsNode(node3));
		assertTrue(subNS.containsNode(node5));
		assertTrue(subNS.containsNode(node6));
		assertTrue(subNS.getNodeCount() == 4);
		assertTrue(subNS.getLinkCount() == 5);
		
		//distance = 2
		subNS= ns1.getSubNodeStructure(nodes, 2);
		
		assertTrue(subNS.containsNode(node4));
		assertTrue(subNS.containsNode(node3));
		assertTrue(subNS.containsNode(node5));
		assertTrue(subNS.containsNode(node1));
		assertTrue(subNS.containsNode(node2));
		assertTrue(subNS.containsNode(node6));
		assertTrue(subNS.getNodeCount() == 6);
		assertTrue(subNS.getLinkCount() == 7);
		
		//distance = 100 -> same result to distance == 2
        //and it's should not time consuming
		subNS= ns1.getSubNodeStructure(nodes, 100);
		
		assertTrue(subNS.containsNode(node4));
		assertTrue(subNS.containsNode(node3));
		assertTrue(subNS.containsNode(node5));
		assertTrue(subNS.containsNode(node1));
		assertTrue(subNS.containsNode(node2));
		assertTrue(subNS.containsNode(node6));
		assertTrue(subNS.getNodeCount() == 6);
		assertTrue(subNS.getLinkCount() == 7);
		
	}
	
	@Test
	public void testGetSubNodeStructure2() {
		/*
		 * test for different number of specified node (0, 1, 2)
		 */
		nodes.clear();

		//number of node = 0
		subNS= ns1.getSubNodeStructure(nodes, 1);
		assertTrue(subNS.getNodeCount() == 0);
		assertTrue(subNS.getLinkCount() == 0);
		
		//number of node = 1
        //Be tested at above test cases for distance
		
		//number of node = 2
		nodes.add(node1);
		nodes.add(node5);
		subNS= ns1.getSubNodeStructure(nodes, 1);
		assertTrue(subNS.containsNode(node4));
		assertTrue(subNS.containsNode(node3));
		assertTrue(subNS.containsNode(node5));
		assertTrue(subNS.containsNode(node1));
		assertTrue(subNS.containsNode(node2));
		assertTrue(subNS.getNodeCount() == 5);
		assertTrue(subNS.getLinkCount() == 6);
		
	}
	
	@Test
	public void testGetSubNodeStructure3() {
		/*
		 * If specified node is not in NodeStructure
		 */
		nodes.clear();
		nodes.add(node4);
		nodes.add(node6);
		nodes.add(node7);
		subNS= ns1.getSubNodeStructure(nodes, 1);

		assertTrue(subNS.containsNode(node4));
		assertTrue(subNS.containsNode(node3));
		assertTrue(subNS.containsNode(node5));
		assertTrue(subNS.containsNode(node6));
		/*
		 * the node7 be not involved in the sub NodeStructure,
		 * because it does not belong to the full NodeStructure. 
		 */
		assertTrue(subNS.getNodeCount() == 4);
		assertTrue(subNS.getLinkCount() == 5);
		
	}

}
