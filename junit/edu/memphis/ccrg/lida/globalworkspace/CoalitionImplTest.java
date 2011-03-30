package edu.memphis.ccrg.lida.globalworkspace;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.BasicAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the CoalitionImpl class
 * @author Siminder Kaur, Nisrine
 */
public class CoalitionImplTest {
	
	CoalitionImpl coalition;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure content;
	WorkspaceBufferImpl buffer;
	AttentionCodelet codelet;

	@Before
	public void setUp() throws Exception {		
		new PerceptualAssociativeMemoryImpl();
		buffer = new WorkspaceBufferImpl();
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		content= new NodeStructureImpl();
		codelet = new BasicAttentionCodeletImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
		
		node1.setActivation(0.3);
		node2.setActivation(0.4);
		node3.setActivation(0.6);
				
		link1 = new LinkImpl(node1,node2,PerceptualAssociativeMemoryImpl.NONE);
		link2 = new LinkImpl(node2,node3,PerceptualAssociativeMemoryImpl.NONE);
		
		link1.setActivation(0.5);
		link1.setActivation(0.7);
		
		content.addDefaultNode(node1);
		content.addDefaultNode(node2);
		content.addDefaultNode(node3);
		content.addDefaultLink(link1);
		content.addDefaultLink(link2);		
	}

	@Test
	public void testCoalitionImpl() {

		coalition = new CoalitionImpl((BroadcastContent) content,0.7,codelet);
		double d = ((0.7*(node1.getActivation()+node2.getActivation()+node3.getActivation()+link1.getActivation()+link2.getActivation()))/ (content.getNodeCount() + content.getLinkCount()));
		
		assertEquals ("Problem with CoalitionImpl",d,coalition.getActivation(),0.00001);			
	}

	@Test
	public void testGetContent() {					
		coalition = new CoalitionImpl((BroadcastContent) content,1.0,codelet);
		assertEquals ("Problem with GetContent ", (BroadcastContent) content,coalition.getContent());		
	}
}
