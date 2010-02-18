package edu.memphis.ccrg.lida.example.shared;

import static org.junit.Assert.*;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class NodeStructureImplTest extends TestCase{
	NodeImpl inputNode = new NodeImpl();
	NodeImpl resultNode = new NodeImpl();
	
	//Collection<Node> nodesToAdd = new Collection<Node>();
	Map<Long, Node> nodes;

	@Before
	public void setUp() throws Exception {		
		inputNode.setId(1);		
		inputNode.setLabel("red");
		inputNode.setActivation(1);
		//inputNode.setImportance(1);
		
		resultNode.setId(1);
		resultNode.setLabel("blue");
		resultNode.setActivation(2);
		//resultNode.setImportance(1);
		
		/*Node m=null,n = null ;	
		m.setId(3);
		n.setId(3);
		long l=1;
		
		//for (long i=0;i<5;i++){
			nodes.put(l, n);
		//}*/
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNodeStructureImpl() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testNodeStructureImplStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testNodeStructureImplNodeStructure() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddLinks() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNode() {
	
		assertEquals("Problem with addNode", inputNode,resultNode);	
		
		
	}

	@Test
	public void testAddNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteLinkable() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteNode() {
		fail("Not yet implemented");
	}

}
