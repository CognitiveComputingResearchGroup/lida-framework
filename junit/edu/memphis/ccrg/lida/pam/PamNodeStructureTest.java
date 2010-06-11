/**
 * 
 */
package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;

/**
 * @author Siminder Kaur
 *
 */
public class PamNodeStructureTest extends TestCase{
	
	PamNodeStructure nodeStructure1,nodeStructure2;
	PamNodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	
	@Before
	public void setUp() throws Exception {
		 nodeStructure1 =  new PamNodeStructure();
		 nodeStructure2 =  new PamNodeStructure();	 
		 
		 node1 = new PamNodeImpl();
		 node2 = new PamNodeImpl();
		 node3 = new PamNodeImpl();
		 
		 link1 = new LinkImpl();
		 link2 = new LinkImpl();
		 
		 node1.setId(1);
		 node2.setId(2);
		 node3.setId(3);
		 
		 link1.setSource(node1);
		 link1.setSink(node2);
		 link2.setSource(node1);
		 link2.setSink(node3);
		 
		 nodeStructure1.addNode(node1);
		 nodeStructure1.addNode(node2);
		 nodeStructure1.addLink(link1);
		 
	}
	
	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#setUpscale(java.lang.Double)}.
	 */
	@Test
	public void testSetUpscale() {
		nodeStructure1.setUpscale(2.0);
		assertEquals("Problem with SetUpscale", 2.0, nodeStructure1.getUpscale());	
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#getUpscale()}.
	 */
	@Test
	public void testGetUpscale() {
		nodeStructure1.setUpscale(2.0);
		assertEquals("Problem with GetUpscale", 2.0, nodeStructure1.getUpscale());	
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#setDownscale(java.lang.Double)}.
	 */
	@Test
	public void testSetDownscale() {
		nodeStructure1.setDownscale(2.0);
		assertEquals("Problem with SetDownscale", 2.0, nodeStructure1.getDownscale());	
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#getDownscale()}.
	 */
	@Test
	public void testGetDownscale() {
		nodeStructure1.setDownscale(2.0);
		assertEquals("Problem with SetDownscale", 2.0, nodeStructure1.getDownscale());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#getSelectivity()}.
	 */
	@Test
	public void testGetSelectivity() {
		nodeStructure1.setSelectivity(2.0);
		assertEquals("Problem with GetSelectivity", 2.0, nodeStructure1.getSelectivity());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#setSelectivity(java.lang.Double)}.
	 */
	@Test
	public void testSetSelectivity() {
		nodeStructure1.setSelectivity(2.0);
		assertEquals("Problem with GetSelectivity", 2.0, nodeStructure1.getSelectivity());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#addPamNodes(java.util.Collection)}.
	 */
	@Test
	public void testAddPamNodes() {
		List<PamNode> nodes = new ArrayList<PamNode>();
		nodes.add(node1);
		nodes.add(node2);
		nodeStructure2.addPamNodes(nodes);
		assertTrue("Problem with AddPamNodes", nodeStructure2.getNodes().containsAll(nodes));	
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#addPamNode(edu.memphis.ccrg.lida.pam.PamNode)}.
	 */
	@Test
	public void testAddPamNode() {
		
		nodeStructure2.setSelectivity(4.0);
		nodeStructure2.setUpscale(3.0);
		nodeStructure2.addNode(node1);
		assertTrue("Problem with AddPamNode", nodeStructure2.hasNode(node1));
		assertEquals("Problem with AddPamNode", 4.0,nodeStructure2.getSelectivity());
		assertEquals("Problem with AddPamNode", 3.0,nodeStructure2.getUpscale());
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#hasNoChildren(edu.memphis.ccrg.lida.framework.shared.Linkable)}.
	 */
	@Test
	public void testHasNoChildren() {
		nodeStructure2.addNode(node1);
		assertEquals("Problem with HasNoChildren", true,nodeStructure2.hasNoChildren(node1));
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#hasNoParents(edu.memphis.ccrg.lida.framework.shared.Linkable)}.
	 */
	@Test
	public void testHasNoParents() {
		nodeStructure2.addNode(node1);
		assertEquals("Problem with HasNoChildren", true,nodeStructure2.hasNoParents(node1));
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#setNodesExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}.
	 */
	@Test
	public void testSetNodesExciteStrategy() {
		DefaultExciteStrategy behavior = new DefaultExciteStrategy();
				
		nodeStructure1.setNodesExciteStrategy(behavior);		
		
		assertEquals("Problem with SetNodesExciteStrategy", behavior, node1.getExciteStrategy());
		assertEquals("Problem with SetNodesExciteStrategy", behavior, node2.getExciteStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#setNodesDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)}.
	 */
	@Test
	public void testSetNodesDecayStrategy() {
				
		DecayStrategy behavior = new LinearDecayStrategy();
				
		assertEquals("Problem with SetNodesDecayStrategy", behavior, node1.getDecayStrategy());
		assertEquals("Problem with SetNodesDecayStrategy", behavior, node2.getDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#getParents(edu.memphis.ccrg.lida.framework.shared.Node)}.
	 */
	@Test
	public void testGetParents() {
		nodeStructure2.addNode(node1);
		nodeStructure2.addNode(node2);
		nodeStructure2.addNode(node3);
		nodeStructure2.addLink(link1);
		nodeStructure2.addLink(link2);
		
		Set<PamNode> parents = new HashSet<PamNode>();
		parents.add(node2);	
		parents.add(node3);
		
		assertEquals("Problem with GetParents", parents, nodeStructure2.getParents(node1));
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#getChildren(edu.memphis.ccrg.lida.framework.shared.Linkable)}.
	 */
	@Test
	public void testGetChildren() {
		
		link1.setSource(node1);
		link1.setSink(node3);
		link2.setSource(node2);
		link2.setSink(node3);
		 
		nodeStructure2.addNode(node1);
		nodeStructure2.addNode(node2);
		nodeStructure2.addNode(node3);
		nodeStructure2.addLink(link1);
		nodeStructure2.addLink(link2);
		
		Set<PamNode> children = new HashSet<PamNode>();
		children.add(node1);	
		children.add(node2);
		
		assertEquals("Problem with GetChildren", children, nodeStructure2.getChildren(node3));
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeStructure#decayNodes(long)}.
	 */
	@Test
	public void testDecayNodes() {
		node1.setActivation(0.7);
		node2.setActivation(0.6);
		nodeStructure2.addNode(node1);
		nodeStructure2.addNode(node2);
		
		nodeStructure2.decayNodes(100);
		
		assertTrue("Problem with DecayNodes",((node1.getActivation()<0.7) && (node2.getActivation()<0.6)));
		
	}
}
