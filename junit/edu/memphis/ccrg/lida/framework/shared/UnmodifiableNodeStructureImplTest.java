package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.pam.PamNode;

public class UnmodifiableNodeStructureImplTest {

	private ElementFactory factory;
	private Node aNode, bNode, cNode;
	private NodeStructure aa = new NodeStructureImpl();
	private NodeStructure xx = new NodeStructureImpl();
	private PamNode category;

	@Before
	public void setUp() throws Exception {
		factory = ElementFactory.getInstance();
		aNode = factory.getNode();
		aNode.setLabel("A");

		bNode = factory.getNode();
		bNode.setLabel("B");

		cNode = factory.getNode();
		cNode.setLabel("C");
		category = (PamNode) factory.getNode("PamNodeImpl");
	}

	/**
	 * Test empty.
	 */
	@Test
	public void testEmptyEquality() {
		assertEqualsHashCode("Empty NS's: ", aa, xx, true);
	}

	/**
	 * Test add.
	 */
	@Test
	public void testAddEquality() {
		aa.addDefaultNode(aNode);
		xx.addDefaultNode(aNode);
		assertEqualsHashCode("Add 1 node: ", aa, xx, true);
	}
	
	/**
	 * Test copying.
	 */
	@Test
	public void testCopyingEquality(){
		aa.addDefaultNode(cNode);
		xx.addDefaultNode(cNode);
		NodeStructure cc = new NodeStructureImpl(aa);
		assertEqualsHashCode("NS copying: ", aa, cc, true);
		assertEqualsHashCode("NS copying: ", xx, cc, true);
	}

	/**
	 * Test add remove.
	 */
	@Test
	public void testAddRemoveEquality() {
		aa.addDefaultNode(aNode);
		aa.removeNode(aNode);
		xx.addDefaultNode(aNode);
		xx.removeNode(aNode);
		assertEqualsHashCode("Add/remove: ", aa, xx, true);
	}

	/**
	 * Test add link.
	 */
	@Test
	public void testAddLinkEquality() {
		aa.addDefaultNode(aNode);
		aa.addDefaultNode(bNode);
		aa.addDefaultLink(aNode.getId(), bNode.getExtendedId(), category, 0.0, 0.0);
		
		xx.addDefaultNode(aNode);
		xx.addDefaultNode(bNode);
		assertEqualsHashCode("Add link: ", aa, xx, false);
	}
	
	/**
	 * Test add link2.
	 */
	@Test
	public void testAddLinkEquality2() {
		aa.addDefaultNode(aNode);
		aa.addDefaultNode(bNode);
		aa.addDefaultLink(aNode.getId(), bNode.getExtendedId(), category, 0.0, 0.0);
		
		xx.addDefaultNode(aNode);
		xx.addDefaultNode(bNode);
		xx.addDefaultLink(aNode.getId(), bNode.getExtendedId(), category, 0.0, 0.0);
		assertEqualsHashCode("Add link: ", aa, xx, true);
	}

	/**
	 * Check equals hash code.
	 *
	 * @param testName the test name
	 * @param nsA the ns a
	 * @param nsB the ns b
	 * @param equalExpected the equal expected
	 */
	private void assertEqualsHashCode(String testName, NodeStructure nsA, NodeStructure nsB, boolean equalExpected) {
		UnmodifiableNodeStructureImpl a = new UnmodifiableNodeStructureImpl(nsA);
		UnmodifiableNodeStructureImpl b = new UnmodifiableNodeStructureImpl(nsB);
		boolean aEqualsB = a.equals(b);
		boolean bEqualsA = b.equals(a);
		boolean hashCodesEqual = (a.hashCode() == b.hashCode());

		assertEquals(testName + ": A equals B", aEqualsB, equalExpected);
		assertEquals(testName + ": B equals A", bEqualsA, equalExpected);
		assertEquals(testName + ": Hashcodes", hashCodesEqual, equalExpected);
		if (aEqualsB && bEqualsA){
			assertTrue(testName + ": Hashcodes not equal but should be.",
					   hashCodesEqual);
		}
	}

	@Test
	public void testUnmodifiableNodeStructureImplNodeStructureBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopy() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultLinkLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultLinkIntExtendedIdLinkCategoryDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultLinkIntIntLinkCategoryDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultLinks() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveLinkableLinkable() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveLinkableExtendedId() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearLinks() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearNodeStructure() {
		fail("Not yet implemented");
	}

	@Test
	public void testMergeWith() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDefaultLinkNodeLinkableLinkCategoryDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecayNodeStructure() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsLinkLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsLinkExtendedId() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsLinkableLinkable() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsLinkableExtendedId() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsNodeNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsNodeInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsNodeExtendedId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAttachedLinksLinkable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAttachedLinksLinkableLinkCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConnectedSinks() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConnectedSources() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultLinkType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultNodeType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLink() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinkCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinkable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinkableCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinkableMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinkables() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinks() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinksLinkCategory() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNodeInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNodeExtendedId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNodeCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNodes() {
		fail("Not yet implemented");
	}

}
