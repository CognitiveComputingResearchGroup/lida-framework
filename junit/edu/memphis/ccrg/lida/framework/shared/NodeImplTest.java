/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the NodeImpl class
 * @author Siminder Kaur
 */
public class NodeImplTest extends TestCase{

	NodeImpl node1,node2,node3;
	PamNodeImpl pamNode1,pamNode2;
	NodeClass classN;
	
	/**
	 * This method is called before running each test case to initialize the objects
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();	
		pamNode1 = new PamNodeImpl();
		pamNode2 = new PamNodeImpl();
		
		pamNode1.setId(11);
		pamNode2.setId(20);
		
		node1.setId(1);			
		node1.setLabel("red");		
		node1.setImportance(100);
		node1.setNodeClass(classN);
		node1.setReferencedNode(pamNode1);	
						
		node2.setId(2);			
	}

	/**
	 * This method is called after running each test case
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * This method is used to test the NodeImpl.getId() method
	 */
	@Test
	public void testGetId() {		
		
		assertEquals("Problem with getId", 1, node1.getId());
	}	
	
	/**
	 * This method is used to test the NodeImpl.setId() method
	 */
	@Test
	public void testSetId() {
		
		node2.setId(22);					
		assertEquals("Problem with setId", 22, node2.getId());
	}	
	
	/**
	 * This method is used to test the NodeImpl.getLabel() method
	 */
	@Test
	public void testGetLabel() {		
							
		assertEquals("Problem with getLabel", "red", node1.getLabel());
	}
	
	/**
	 * This method is used to test the NodeImpl.setLabel() method
	 */
	@Test
	public void testSetLabel() {
		
		node2.setLabel("purple");					
		assertEquals("Problem with setLabel", "purple", node2.getLabel());
	}	
	
	/**
	 * This method is used to test the NodeImpl.getImportance() method
	 */
	@Test
	public void testGetImportance() {		
							
		assertEquals("Problem with getImportance", 100.0, node1.getImportance());
	}
	
	/**
	 * This method is used to test the NodeImpl.setImportance() method
	 */
	@Test
	public void testSetImportance() {
		
		node2.setImportance(200);					
		assertEquals("Problem with setImportance", 200.0, node2.getImportance());
	}	
	
	/**
	 * This method is used to test the NodeImpl.getReferencedNode() method
	 */
	@Test
	public void testGetReferencedNode() {		
							
		assertEquals("Problem with getReferencedNode", pamNode1, node1.getReferencedNode());
	}
	
	/**
	 * This method is used to test the NodeImpl.setReferencedNode() method
	 */
	@Test
	public void testSetReferencedNode() {
		
		node2.setReferencedNode(pamNode2);					
		assertEquals("Problem with setReferencedNode", pamNode2, node2.getReferencedNode());
	}	
	
	/**
	 * This method is used to test the NodeImpl.equals() method
	 */
	@Test
	public void testEquals() {
		
		node1.setId(1);
		node2.setId(1);
		assertEquals("Problem with equals", node1,node2);
	}	
	/**
	 * This method is used to test the NodeImpl.getNodeClass() method
	 */
	@Test
	public void testGetNodeClass() {		
		
		assertEquals("Problem with getNodeClass", classN,node1.getNodeClass());
	}	
	/**
	 * This method is used to test the NodeImpl.setNodeClass() method
	 */
	@Test
	public void testSetNodeClass() {
		
		node2.setNodeClass(classN);	
		assertEquals("Problem with setNodeClass", classN,node2.getNodeClass());
	}	
	
	/**
	 * This method is used to test the NodeImpl.hashCode() method
	 */
	@Test
	public void testHashCode() {
		
		int code = node1.hashCode();	
		assertEquals("Problem with setNodeClass", 1 % 31 ,code);
	}	
}
