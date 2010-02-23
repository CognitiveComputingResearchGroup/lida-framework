package edu.memphis.ccrg.lida.example.shared;

import static org.junit.Assert.*;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Siminder Kaur
 * 
 */

public class NodeStructureImplTest extends TestCase{
	NodeImpl node1 = new NodeImpl();
	NodeImpl node2 = new NodeImpl();
	NodeImpl node3 = new NodeImpl();
	LinkImpl link1 = new LinkImpl();
	LinkImpl link2 = new LinkImpl();
	Map<Long, Node> nodes;
	NodeStructureImpl nodeStructure1 = new NodeStructureImpl();	
	NodeStructureImpl nodeStructure2 = new NodeStructureImpl();

	@Before
	public void setUp() throws Exception {		
		node1.setId(1);		
		node1.setLabel("red");
		node1.setActivation(1);
		node1.setImportance(1);
		
		node2.setId(2);
		node2.setLabel("blue");
		node2.setActivation(2);
		node2.setImportance(1);
		
		node3.setId(3);
		
		link1.setIds("link1");
		link1.setSource(node1);
		link1.setSink(node2);
		
		link2.setIds("link2");	
		link2.setSource(node2);
		link2.setSink(node3);
		
		/*long i=1;
		nodes.put(i, node1);
		nodes.put(i+1, node2);*/	

		nodeStructure1.addNode(node1);		
		nodeStructure1.addNode(node2);
		nodeStructure1.addNode(node3);
		nodeStructure1.addLink(link1);
	}
	

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAddLink() {
		
		nodeStructure1.addLink(link2);
		if(!nodeStructure1.hasLink(link2))		
			fail("Problem with addLink");
	}

	@Test
	public void testAddLinks() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNode() {		
		//nodeStructure1.addNode(node3);
		if(!nodeStructure1.hasNode(node2))		
			fail("Problem with addNode");			
	}

	@Test
	public void testAddNodes() {
		/*Collection<Node> nodesToAdd = new Collection<Node>();
		
		nodesToAdd.add(node2);		
		nodeStructure2.addNodes(nodesToAdd);
		
		if(!nodeStructure2.hasNode(node1) )		
			fail("Problem with addNodes");
		*/
	}

	@Test
	public void testDeleteLink() {

		nodeStructure1.deleteLink(link1);
		if(nodeStructure1.hasLink(link1))		
			fail("Problem with deleteLink");
	}

	@Test
	public void testClearNodes() {

			nodeStructure1.clearNodes();
			if (nodeStructure1.getNodeCount()>0)
			fail("Problem with clearNodes");
	}	

	@Test
	public void testDeleteNode() {
		
		nodeStructure1.deleteNode(node3);
		if(nodeStructure1.hasNode(node3))		
			fail("Problem with deleteNode");
	}
}
