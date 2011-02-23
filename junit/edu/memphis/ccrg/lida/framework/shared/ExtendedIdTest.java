package edu.memphis.ccrg.lida.framework.shared;

import junit.framework.TestCase;

import org.junit.Before;

public class ExtendedIdTest extends TestCase {
	
	@Override
	@Before
	public void setUp() throws Exception {}
	
	public void testNodeId(){
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		ExtendedId id1 = new ExtendedId(nodeId);
		ExtendedId id2 = new ExtendedId(nodeId);
		
		assertTrue(id1.isNodeId());
		assertTrue(id2.isNodeId());
		
		assertEquals(nodeId, id1.getSourceNodeId());
		assertEquals(nodeId, id2.getSourceNodeId());
		
		assertTrue(id1.equals(id2));
		assertTrue(id2.equals(id1));
		
		assertTrue(id1.hashCode() == id2.hashCode());		
	}
	
	public void testNodeId0(){
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		int nodeId2 = nodeId % 31;
		ExtendedId id1 = new ExtendedId(nodeId);
		ExtendedId id2 = new ExtendedId(nodeId2);
		
		assertTrue(id1.isNodeId());
		assertTrue(id2.isNodeId());
		
		assertEquals(nodeId, id1.getSourceNodeId());
		assertEquals(nodeId2, id2.getSourceNodeId());
		
		assertTrue(!id1.equals(id2));
		assertTrue(!id2.equals(id1));
		
		assertTrue(id1.hashCode() != id2.hashCode());		
	}

	//Two nodes case
	public void testLinkId(){
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		int sourceNodeId = nodeId % 31;
		int category = 5;
		ExtendedId sinkNodeId = new ExtendedId(nodeId);
		ExtendedId id = new ExtendedId(category, sourceNodeId, sinkNodeId);
	}
	
}
