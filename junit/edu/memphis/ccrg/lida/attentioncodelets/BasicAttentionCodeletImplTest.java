/**
 * 
 */
package edu.memphis.ccrg.lida.attentioncodelets;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the BasicAttentionCodeletImpl class
 * @author Siminder Kaur
 */

public class BasicAttentionCodeletImplTest extends TestCase{
	WorkspaceBufferImpl buffer;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure nodeStructure;
	BasicAttentionCodeletImpl attnCodelet;

	/**
	 * @throws java.lang.Exception e
	 */
	@Override
	@Before
	public void setUp(){
		new PerceptualAssociativeMemoryImpl();
		buffer = new WorkspaceBufferImpl();
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		nodeStructure= new NodeStructureImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
				
		link1 = new LinkImpl(node1,node2,PerceptualAssociativeMemoryImpl.NONE);
		link2 = new LinkImpl(node2,node3,PerceptualAssociativeMemoryImpl.NONE);
		
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
		
		boolean test = NodeStructureImpl.compareNodeStructures(nodeStructure,attnCodelet.retrieveWorkspaceContent(buffer));
		if(test==false)
		{
			fail("Problem with HasSoughtContent");
		}
	}

}
