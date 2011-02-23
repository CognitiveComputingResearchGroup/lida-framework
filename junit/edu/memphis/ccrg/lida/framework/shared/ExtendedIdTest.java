package edu.memphis.ccrg.lida.framework.shared;

import junit.framework.TestCase;

import org.junit.Before;

/**
 * 
 * If you are really bored then you can test the case where the sinks link's sink is a link between 2 nodes
 * I've just tested up to the case where the sink's link is just between 2 nodes and
 * that should be sufficient because it tests the same code (the link constructor) just with different values in the sink ExtendedId
 * @author Ryan McCall
 *
 */
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
		
		assertHashCodeEquals(id1, id2);	
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
		
		assertDifferentHashCodeEquals(id1, id2);
	}

	//Two nodes case
	public void testLinkId(){
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		ExtendedId sinkNodeId = new ExtendedId(nodeId);
		int sourceNodeId = nodeId % 31;
		int category = (int) (Math.random() * Integer.MAX_VALUE);
		
		ExtendedId id = new ExtendedId(category, sourceNodeId, sinkNodeId);
		assertTrue(!id.isNodeId());
		assertEquals(sourceNodeId, id.getSourceNodeId());
		
		//equivalent id
		ExtendedId id2 = new ExtendedId(category, sourceNodeId, sinkNodeId);
		assertHashCodeEquals(id, id2);
		
		//non equivalent id
		ExtendedId id3 = new ExtendedId(category+1, sourceNodeId, sinkNodeId);
		assertDifferentHashCodeEquals(id, id3);
		
		//non equivalent id
		ExtendedId id4 = new ExtendedId(category, sourceNodeId+1, sinkNodeId);
		assertDifferentHashCodeEquals(id, id4);
		
		//non equivalent id
		ExtendedId sinkNodeId2 = new ExtendedId(nodeId+1);
		ExtendedId id5 = new ExtendedId(category, sourceNodeId, sinkNodeId2);
		assertDifferentHashCodeEquals(id, id5);
	}
	
	//One node, one link, link between 2 nodes case
	public void testLinkId2(){
		//sink will be a link between 2 nodes
		int slCat = 10;
		int slSource = 11;
		ExtendedId slSink = new ExtendedId(12);
		ExtendedId sinkLink = new ExtendedId(slCat, slSource, slSink);
		
		//Node to link link
		int category = 2;
		int sourceNodeId = 5;
		ExtendedId nodeLinkId = new ExtendedId(category, sourceNodeId, sinkLink);
		assertTrue(!nodeLinkId.isNodeId());
		
		//clone
		sinkLink = new ExtendedId(slCat, slSource, slSink);
		ExtendedId sameNodeLinkId = new ExtendedId(category, sourceNodeId, sinkLink);
		assertHashCodeEquals(nodeLinkId, sameNodeLinkId);
		
		//same structure, different sink link category
		sinkLink = new ExtendedId(slCat+1, slSource, slSink);
		ExtendedId diffNodeLinkId = new ExtendedId(category, sourceNodeId, sinkLink);
		assertDifferentHashCodeEquals(nodeLinkId, diffNodeLinkId);
		
		//same structure, different sink link source node
		sinkLink = new ExtendedId(slCat, slSource+1, slSink);
		diffNodeLinkId = new ExtendedId(category, sourceNodeId, sinkLink);
		assertDifferentHashCodeEquals(nodeLinkId, diffNodeLinkId);
		
		//same structure, different sink link sink node
		slSink = new ExtendedId(slSink.getSourceNodeId() + 1);
		sinkLink = new ExtendedId(slCat, slSource, slSink);
		diffNodeLinkId = new ExtendedId(category, sourceNodeId, sinkLink);
		assertDifferentHashCodeEquals(nodeLinkId, diffNodeLinkId);
	}
	
	private void assertDifferentHashCodeEquals(Object o1, Object o2){
		assertTrue(!o1.equals(o2));
		assertTrue(!o2.equals(o1));
		assertTrue(o1.hashCode() != o2.hashCode());
	}
	
	private void assertHashCodeEquals(Object o1, Object o2){
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));
		assertTrue(o1.hashCode() == o2.hashCode());
	}
	
}
