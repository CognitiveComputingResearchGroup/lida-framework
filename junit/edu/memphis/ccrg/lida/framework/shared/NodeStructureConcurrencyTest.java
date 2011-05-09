package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

public class NodeStructureConcurrencyTest {

	private Node node1, node2, node3, node4;
	private Link link1, link2, link3;
	private PamNode category1, category2;
	private NodeStructureImpl ns1, ns2, ns3;
	private static ElementFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() {

		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils
				.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}

	/**
	 * This method is called before running each test case to initialize the
	 * objects
	 * 
	 */

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
		node4.setLabel("green");
		node4.setActivation(0.4);

		category1 = new PamNodeImpl();
		category1.setId(99999);
		category2 = new PamNodeImpl();
		category2.setId(100000);

		link1 = factory.getLink(node1, node2, category1);
		link2 = factory.getLink(node2, node3, category2);
		link3 = factory.getLink(node2, node4, category2);

		ns1 = new NodeStructureImpl();
		ns2 = new NodeStructureImpl();
		ns3 = new NodeStructureImpl();
	}

	@Test
	public void concurrencyTest() {
		Thread[] threads = new Thread[10];

		for (int i = 0; i < 10; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						int a = (int) (Math.random() * 5);
						switch (a) {
						case 0:
							Node n = factory.getNode();
							ns1.addDefaultNode(n);
							break;
						case 1:
							Node n1 = factory.getNode();
							Node n2 = factory.getNode();
							ns1.addDefaultLink(n1, n2, category1, 1.0, 0.0);
							break;

						case 2:
							int sum = 0;
							Collection<Node>nodes = ns1.getNodes();
							 for(Node nn:nodes){
								 sum += nn.getId();
							 }
							break;
						}
					}

				}

			});
		}
	}
}
