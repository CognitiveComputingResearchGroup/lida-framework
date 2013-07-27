package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

public class DefaultAttentionCodeletTest {

	private Map<String, Object> params;
	private DefaultAttentionCodelet codelet;
	private MockWorkspaceBufferImpl csm;
	private MockGlobalWorkspaceImpl globalWorkspace;
	private NodeStructure csmContent, soughtContent;
	private Node node1, node2, node3;
	private Link link12, link21, link32, link23;
	private static final ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() {
		params = new HashMap<String, Object>();
		codelet = new DefaultAttentionCodelet();

		csm = new MockWorkspaceBufferImpl();
		globalWorkspace = new MockGlobalWorkspaceImpl();

		codelet.setAssociatedModule(csm, "");
		codelet.setAssociatedModule(globalWorkspace, "");

		csmContent = new NodeStructureImpl();
		soughtContent = new NodeStructureImpl();
		node1 = factory.getNode();
		node1.setActivation(0.0);
		node2 = factory.getNode();
		node2.setActivation(1.0);
		node3 = factory.getNode();
		node3.setActivation(0.5);
		link12 = factory.getLink(node1, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);
		link21 = factory.getLink(node2, node1,
				PerceptualAssociativeMemoryNSImpl.NONE);

		link32 = factory.getLink(node2, node3,
				PerceptualAssociativeMemoryNSImpl.NONE);
		link23 = factory.getLink(node3, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);
	}

	@Test
	public void testContainsSoughtContent() {
		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link12);
		csmContent.addDefaultLink(link21);
		csm.addBufferContent((WorkspaceContent) csmContent);

		params.put("attentionThreshold", 0.5);
		params.put("retrievalDepth", 0);
		codelet.init(params);

		boolean contains = codelet.bufferContainsSoughtContent(csm);
		NodeStructure sought = codelet.getSoughtContent();

		assertTrue(contains);
		assertTrue(sought.containsNode(node2));
		assertEquals(1, sought.getLinkableCount());
	}

	@Test
	public void testContainsSoughtContent1() {
		csmContent.addDefaultNode(node1);
		csm.addBufferContent((WorkspaceContent) csmContent);

		params.put("attentionThreshold", 0.0);
		params.put("retrievalDepth", 0);
		codelet.init(params);

		boolean contains = codelet.bufferContainsSoughtContent(csm);
		NodeStructure sought = codelet.getSoughtContent();

		assertTrue(contains);
		assertTrue(sought.containsNode(node1));
		assertEquals(1, sought.getLinkableCount());
	}

	@Test
	public void testContainsSoughtContent2() {
		csmContent.addDefaultNode(node2);
		csm.addBufferContent((WorkspaceContent) csmContent);

		params.put("attentionThreshold", 1.0);
		params.put("retrievalDepth", 0);
		codelet.init(params);

		boolean contains = codelet.bufferContainsSoughtContent(csm);
		NodeStructure sought = codelet.getSoughtContent();

		assertTrue(contains);
		assertTrue(sought.containsNode(node2));
		assertEquals(1, sought.getLinkableCount());
	}

	@Test
	public void testRetrieveSoughtContent() {
		params.put("attentionThreshold", 0.5);
		params.put("retrievalDepth", 0);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link12);
		csmContent.addDefaultLink(link21);
		csm.addBufferContent((WorkspaceContent) csmContent);

		soughtContent.addDefaultNode(node1);
		codelet.setSoughtContent(soughtContent);

		NodeStructure retrieved = codelet.retrieveWorkspaceContent(csm);

		assertTrue(retrieved.containsNode(node1));
		assertEquals(1, retrieved.getLinkableCount());
	}

	@Test
	public void testRetrieveSoughtContent1() {
		params.put("attentionThreshold", 0.5);
		params.put("retrievalDepth", 1);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link12);
		csmContent.addDefaultLink(link21);
		csm.addBufferContent((WorkspaceContent) csmContent);

		soughtContent.addDefaultNode(node1);
		codelet.setSoughtContent(soughtContent);

		NodeStructure retrieved = codelet.retrieveWorkspaceContent(csm);

		assertTrue(retrieved.containsNode(node1));
		assertTrue(retrieved.containsNode(node2));
		assertEquals(4, retrieved.getLinkableCount());
	}

	@Test
	public void testRetrieveSoughtContent2() {
		params.put("attentionThreshold", 0.5);
		params.put("retrievalDepth", 1);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link12);
		csmContent.addDefaultLink(link21);
		csm.addBufferContent((WorkspaceContent) csmContent);

		soughtContent.addDefaultNode(node2);
		codelet.setSoughtContent(soughtContent);

		NodeStructure retrieved = codelet.retrieveWorkspaceContent(csm);

		assertTrue(retrieved.containsNode(node2));
		assertEquals(1, retrieved.getLinkableCount());
	}

	@Test
	public void testRetrieveSoughtContent3() {
		params.put("attentionThreshold", 0.5);
		params.put("retrievalDepth", 1);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link12);
		csmContent.addDefaultLink(link21);
		csmContent.addDefaultNode(node3);
		csmContent.addDefaultLink(link32);
		csmContent.addDefaultLink(link23);
		csm.addBufferContent((WorkspaceContent) csmContent);

		soughtContent.addDefaultNode(node2);
		codelet.setSoughtContent(soughtContent);

		NodeStructure retrieved = codelet.retrieveWorkspaceContent(csm);

		assertTrue(retrieved.containsNode(node2));
		assertTrue(retrieved.containsNode(node3));
		assertEquals(4, retrieved.getLinkableCount());
	}

	@Test
	public void testRetrieveSoughtContent4() {
		params.put("attentionThreshold", 0.5);
		params.put("retrievalDepth", 1);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link12);
		csmContent.addDefaultLink(link21);
		csmContent.addDefaultNode(node3);
		csmContent.addDefaultLink(link32);
		csmContent.addDefaultLink(link23);
		csm.addBufferContent((WorkspaceContent) csmContent);

		soughtContent.addDefaultNode(node3);
		codelet.setSoughtContent(soughtContent);

		NodeStructure retrieved = codelet.retrieveWorkspaceContent(csm);

		assertTrue(retrieved.containsNode(node2));
		assertTrue(retrieved.containsNode(node3));
		assertEquals(4, retrieved.getLinkableCount());
	}

}
