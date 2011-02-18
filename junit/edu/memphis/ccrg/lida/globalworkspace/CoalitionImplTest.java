package edu.memphis.ccrg.lida.globalworkspace;



import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

public class CoalitionImplTest {
	
	CoalitionImpl coalition;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure content;
	WorkspaceBufferImpl buffer;

	@Before
	public void setUp() throws Exception {
		
		
		buffer = new WorkspaceBufferImpl();
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		content= new NodeStructureImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
		
		
				
		link1 = new LinkImpl(node1,node2,LinkCategoryNode.CHILD);
		link2 = new LinkImpl(node2,node3,LinkCategoryNode.CHILD);
		
		
		content.addDefaultNode(node1);
		content.addDefaultNode(node2);
		content.addDefaultNode(node3);
		content.addDefaultLink(link1);
		content.addDefaultLink(link2);

		
	}

	

	@SuppressWarnings("deprecation")
	@Test
	public void testCoalitionImpl() {

		coalition = new CoalitionImpl((BroadcastContent) content,1.0);
		double d = ((node1.getActivation()+node2.getActivation()+node3.getActivation()+link1.getActivation()+link2.getActivation())/ (content.getNodeCount() + content.getLinkCount()));
		//assertEquals(d, coalition.getActivation());
		assertEquals (d,coalition.getActivation(),0.00001);	
		
	}

	@Test
	public void testGetContent() {	
				
		coalition = new CoalitionImpl((BroadcastContent) content,1.0);
		assertEquals ("Error ", coalition.getContent(),(BroadcastContent) content);
		 		 
		
	}

}
