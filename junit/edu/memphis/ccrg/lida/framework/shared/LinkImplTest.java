/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a JUnit class which can be used to test methods of the LinkImpl class
 * @author Siminder Kaur
 *
 */
public class LinkImplTest extends TestCase{
	
	NodeImpl node1,node2,node3,node4;
	LinkImpl link1,link2,link3;	
	LinkType linktype1,linktype2;		

	/**
	 * This method is called before running each test case to initialize the objects
	 */
	@Before
	public void setUp() throws Exception {
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		node4 = new NodeImpl();
		
		link1 = new LinkImpl();
		link2 = new LinkImpl();
		link3 = new LinkImpl();
		linktype1 = LinkType.PARENT ;
		linktype2 = LinkType.CHILD ;	

		node1.setId(1);	
		node2.setId(2);
		node3.setId(3);		
		node4.setId(4);		
		
	}

	/**
	 * This method is called after running each test case
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This method is used to test the LinkImpl.copy() method
	 */
	@Test
	public void testCopy() {
			
		link1.setSource(node1);
		link1.setSink(node2);
		link1.setType(linktype1);		
		
		link2 = link1.copy();
		
		assertEquals("Problem with copy", link1, link2);		
	}

	/**
	 * This method is used to test the LinkImpl.equals() method
	 */
	@Test
	public void testEqualsObject() {		
			
		link1.setSource(node1);
		link1.setSink(node2);
		link1.setType(linktype1);		
		
		link3.setSource(node1);
		link3.setSink(node2);
		link3.setType(linktype1);
		
		assertEquals("Problem with equals", link1, link3);
		
	}

	/**
	 * This method is used to test the LinkImpl.getSink() method
	 */
	@Test
	public void testGetSink() {
		
		link1.setSink(node1);
		assertEquals("Problem with getSink", node1, link1.getSink());
	}

	/**
	 * This method is used to test the LinkImpl.getSource() method
	 */
	@Test
	public void testGetSource() {
		
		link1.setSource(node1);
		assertEquals("Problem with getSource", node1, link1.getSource());
	}

	/**
	 * This method is used to test the LinkType.getSource() method
	 */
	@Test
	public void testGetType() {
		
		link1.setType(linktype1);
		assertEquals("Problem with getType", linktype1, link1.getType());
	}

	
	/**
	 * This method is used to test the LinkImpl.GetId() method
	 */
	@Test
	public void testGetId() {		
		link1.setSource(node1);
		link1.setSink(node2);
		link1.setType(linktype1);
		
		assertEquals("Problem with getId", "L(1:2:PARENT)", link1.getId());
		
	}

	/**
	 * This method is used to test the LinkImpl.SetSink() method
	 */
	@Test
	public void testSetSink() {
		link1.setSink(node1);
		assertEquals("Problem with setSink", node1, link1.getSink());
	}

	/**
	 * This method is used to test the LinkImpl.SetSource() method
	 */
	@Test
	public void testSetSource() {
		link1.setSource(node1);
		assertEquals("Problem with setSource", node1, link1.getSource());
	}

	/**
	 * This method is used to test the LinkImpl.SetType() method
	 */
	@Test
	public void testSetType() {
		link1.setType(linktype1);
		assertEquals("Problem with setType", linktype1, link1.getType());
	}

	/**
	 * This method is used to test the LinkImpl.GetReferencedLink() method
	 */
	@Test
	public void testGetReferencedLink() {
		link2.setSource(node2);
		link2.setSink(node3);
		link1.setReferencedLink(link2);		
		assertEquals("Problem with getReferencedLink", link2, link1.getReferencedLink());
	}

	/**
	 * This method is used to test the LinkImpl.SetReferencedLink() method
	 */
	@Test
	public void testSetReferencedLink() {
		link2.setSource(node2);
		link2.setSink(node3);
		link1.setReferencedLink(link2);
		assertEquals("Problem with setReferencedLink", link2, link1.getReferencedLink());
	}
	
	/**
	 * This method is used to test the LinkImpl.LinkImpl(Linkable source, Linkable sink, LinkType type, String ids) method
	 */
	@Test
	public void testLinkImpl() {
		LinkImpl link5= new LinkImpl(node1,node2,linktype1);
				
		assertEquals("Problem with LinkImpl constructor having parameters", node1, link5.getSource());
		assertEquals("Problem with LinkImpl constructor having parameters", node2, link5.getSink());
		assertEquals("Problem with LinkImpl constructor having parameters", linktype1, link5.getType());
		
	}

}
