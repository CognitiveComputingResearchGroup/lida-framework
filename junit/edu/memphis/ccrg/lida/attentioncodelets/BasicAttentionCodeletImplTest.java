/**
 * 
 */
package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;



import edu.memphis.ccrg.lida.attentioncodelets.BasicAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the BasicAttentionCodeletImpl class
 * @author Siminder Kaur
 */

public class BasicAttentionCodeletImplTest {
	WorkspaceBufferImpl buffer;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure nodeStructure;
	BasicAttentionCodeletImpl attnCodelet;

	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		buffer = new WorkspaceBufferImpl();
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		nodeStructure= new NodeStructureImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
				
		link1 = new LinkImpl(node1,node2,LinkCategoryNode.CHILD);
		link2 = new LinkImpl(node2,node3,LinkCategoryNode.CHILD);
		
		nodeStructure.addDefaultNode(node1);
		nodeStructure.addDefaultNode(node2);
		nodeStructure.addDefaultNode(node3);
		nodeStructure.addDefaultLink(link1);
		nodeStructure.addDefaultLink(link2);
		
		attnCodelet = new BasicAttentionCodeletImpl();
	}

	/**
	 * Test method for HasSoughtContent().
	 */
	@Test
	public void testHasSoughtContent() {
		
		attnCodelet.setSoughtContent(nodeStructure);
		NodeStructure model = (NodeStructure) buffer.getModuleContent();
		model.mergeWith(nodeStructure);		
		
		assertEquals("Problem with HasSoughtContent",true,attnCodelet.hasSoughtContent(buffer));
	}

	/**
	 * Test method for GetWorkspaceContent().
	 */
	@Test
	public void testGetWorkspaceContent() {
		NodeStructure model = (NodeStructure) buffer.getModuleContent();
		model.mergeWith(nodeStructure);
		
		boolean test = NodeStructureImpl.compareNodeStructures(nodeStructure,attnCodelet.retreiveWorkspaceContent(buffer));
		if(test==false)
		{
			fail("Problem with HasSoughtContent");
		}
	}

}
