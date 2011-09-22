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

public class SubNodeStructureImplTest {

	private static ElementFactory factory;
	private Node node1,node2,node3,node4,node5;
	private Link link1,link2,link3,link4,link5,link6;
	private SubNodeStructureImpl ns1;
	private PamNode category1,category2,category3,category4;

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
		
		category1 = new PamNodeImpl();
		category1.setId(99999);
		category2 = new PamNodeImpl();
		category2.setId(100000);
		
		link1 = factory.getLink(node1, node3, category1);
		link2 = factory.getLink(node1, node4, category2);
		link3 = factory.getLink(node3, node5, category2);
		link4 = factory.getLink(node4, node5, category2);
		link5 = factory.getLink(node2, node5, category2);
		link6 = factory.getLink(node1, link5, category2); 
		
		
		ns1 = new SubNodeStructureImpl();	
		
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);
		ns1.addDefaultNode(node5);
		
		ns1.addDefaultLink(link1);
		ns1.addDefaultLink(link2);
		ns1.addDefaultLink(link3);
		
		ns1.addDefaultLink(link4);
		ns1.addDefaultLink(link5);
		ns1.addDefaultLink(link6);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSubNodeStructure() {
		Collection<Node> nodes = new ArrayList<Node>();
		nodes.add(node1);
		nodes.add(node2);
		System.out.println(ns1.getNodeCount());
		System.out.println(ns1.getLinkCount());
		NodeStructure subNS= ns1.getSubNodeStructure(nodes, 6);
		System.out.println(subNS.getNodeCount());
		System.out.println(subNS.getLinkCount());
	}

	@Test
	public void testDeepFirstSearch() {
		fail("Not yet implemented");
	}

}
