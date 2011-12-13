package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Random;

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
	private Link link1,link2,link3,link4,link5,link6,link7;
	private SubNodeStructureImpl sns1,sns;
	private PamNode category1,category2,category3,category4;
	private NodeStructure subNS;
	
	private int idCounter = 0;
	private int categoryIdCounter = 0;
	private Random random;
	private List<LinkCategory> linkCategoryPool = new ArrayList<LinkCategory>();
	
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
		
		/* Test Case 2 (No Links All Nodes)
		 * 
		 */
		
		link1 = factory.getLink(node1, node3, category1);
		link2 = factory.getLink(node4, node3, category2);
		link3 = factory.getLink(node3, node5, category2);
		link4 = factory.getLink(node4, node5, category2);
		link5 = factory.getLink(node2, node5, category2);
		link6 = factory.getLink(node3, link3, category2); 
		//link7 = factory.getLink(node2, link6, category1);

		
		sns1 = new SubNodeStructureImpl();	

		sns1 = new SubNodeStructureImpl();	
		

		sns1.addDefaultNode(node1);
		sns1.addDefaultNode(node2);
		sns1.addDefaultNode(node3);
		sns1.addDefaultNode(node4);
		sns1.addDefaultNode(node5);
			
		sns1.addDefaultLink(link1);
		sns1.addDefaultLink(link2);
		sns1.addDefaultLink(link3);

		sns1.addDefaultLink(link1);
		sns1.addDefaultLink(link2);
		sns1.addDefaultLink(link3);
		
		sns1.addDefaultLink(link4);
		sns1.addDefaultLink(link5);
		sns1.addDefaultLink(link6);
		

		sns1.addDefaultLink(link4);
		sns1.addDefaultLink(link5);
		sns1.addDefaultLink(link6);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test

	/*Test Case 1 (Make sure if a 'copy' of sub node structure is created)
	 * 
	 * Technique used - Change some property (here 'label') of the extracted nodes
	 * and check if the property changed in the nodes of the original node structure.
	 */

	/*Test Case 2 (Check if a copy of sub node structure is created or not)
	 * 
	 * Technique used - Change some property (here 'label') of the extracted nodes
	 * and check if the property changed in the nodes of the original node structure.
	 */

	public void testGetSubNodeStructure1() {
		Collection<Node> nodeList = new ArrayList<Node>();
		

                //creating list of nodes to serve as root nodes for extraction

		nodeList.add(node1);
		nodeList.add(node2);
                
                //get a new SubNodeStructure with distance 2 from nodes in nodeList
		subNS= sns1.getSubNodeStructure(nodeList, 2);
		
                //get nodes from SubNodeStructure and change label
		Collection<Node> nod=subNS.getNodes();
		for(Node n:nod){
			n.setLabel("Pulin");
		}
		
                //get nodes from original NodeStructure
		nod=sns1.getNodes();
		
		for(Node n:nod){
			assertTrue( n.getLabel().compareTo("Pulin") != 0 );
		}
		
		// Just checking if expected linkables were obtained
		
		assertTrue(subNS.getNodeCount()==5);
		assertTrue(subNS.getLinkCount()==6);

	}	
	
	
	@Test

	/*Test Case 2 HUGE NodeStructure test
	 * 
	 * Mainly used to check performance of the algorithm
	 */

	/*Test Case 3 HUGE NodeStructure test
	 * 
	 * Mainly used to check performance of the algorithm
	 */

	public void testGetSubNodeStructure2() {

                //create a random large node structure

		Collection<Node> nodeList = new ArrayList<Node>();
		int distance=10;
		int seed = 23434535;
		random = new Random(seed);
		sns = new SubNodeStructureImpl();
		
		int nodes = 10000;	
		int linkCategories = 1000;
		double nodeLinkRatio = 1.0;
		
		int links = (int)(nodes / nodeLinkRatio);
		System.out.println("Creating NS with " + nodes + " nodes, " + links + " links");
		
		createLinkCategoryPool(linkCategories);
		addSomeNodes(nodes);
		addSomeLinks(links);
		
		long start,finish;
		
                /*randomly adding 2nd and the 10th node 
                 *to nodeList to serve as root nodes for extraction 
                 */
		nodeList.add((Node)sns.getNodes().toArray()[1]);
		nodeList.add((Node)sns.getNodes().toArray()[9]);
		
                //calculate time in extracting a SubNodeStructure
		start = System.currentTimeMillis();
		subNS=sns.getSubNodeStructure(nodeList, distance);
		finish = System.currentTimeMillis();
                
                
		System.out.println("time: " + (finish - start) + " ms");
		System.out.println("ms per linkable : " + (finish - start) / (1.0* subNS.getLinkableCount()));
		System.out.println("total linkables " + subNS.getLinkableCount());
			
	}
	
	
	
	@Test
	public void testDeepFirstSearch() {
		//fail("Not yet implemented");
	}


	private void createLinkCategoryPool(int linkCategories) {
		for(int i = 0; i < linkCategories; i++){
			PamNodeImpl temp = new PamNodeImpl();
			temp.setId(categoryIdCounter++);
			linkCategoryPool.add(temp);
		}
		
	}

	public void addSomeNodes(int num){
		for(int i = 0; i < num; i++){
			Node foo = factory.getNode();
			foo.setId(idCounter++);
			sns.addDefaultNode(foo);
		}
	}
	
	public void addSomeLinks(int num){
		for(int i = 0; i < num; i++){
			int randomSource = random.nextInt(idCounter);
			int randomSink = random.nextInt(idCounter);
			while(randomSink == randomSource){
				randomSink = random.nextInt(idCounter);
			}
			Node source = sns.getNode(randomSource);
			Node sink = sns.getNode(randomSink);
			int randomCategory = random.nextInt(linkCategoryPool.size());
			PamNode lcn = (PamNode) linkCategoryPool.get(randomCategory);
			lcn.setId(categoryIdCounter++);
			sns.addDefaultLink(source, sink, lcn, 1.0, -1.0);
		}
	}

}
