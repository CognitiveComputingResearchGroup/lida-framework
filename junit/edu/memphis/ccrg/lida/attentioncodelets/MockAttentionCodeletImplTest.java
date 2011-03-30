package edu.memphis.ccrg.lida.attentioncodelets;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the MockAttentionCodeletImpl class
 * @author Siminder Kaur
 */
public class MockAttentionCodeletImplTest {
	
	MockAttentionCodeletImpl codelet;
	WorkspaceBufferImpl buffer;
	GlobalWorkspace globalWorkspace;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure csmContent;
	
	@Before
	public void setUp() throws Exception {
		new PerceptualAssociativeMemoryImpl();
		codelet = new MockAttentionCodeletImpl();
				
		buffer = new WorkspaceBufferImpl();
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		csmContent= new NodeStructureImpl();
		globalWorkspace = new MockGlobalWorkspaceImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
				
		link1 = new LinkImpl(node1,node2,PerceptualAssociativeMemoryImpl.NONE);
		link2 = new LinkImpl(node2,node3,PerceptualAssociativeMemoryImpl.NONE);
		
		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultNode(node3);
		csmContent.addDefaultLink(link1);
		csmContent.addDefaultLink(link2);		
	}
 
	@Test
	public void testRunThisLidaTask() {		
		codelet.setSoughtContent(csmContent);	
		NodeStructure model = (NodeStructure) buffer.getModuleContent();
		model.mergeWith(csmContent);
		
		codelet.setAssociatedModule(buffer, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(globalWorkspace, ModuleUsage.TO_WRITE_TO);
		
		System.out.println("Testing method runThisLidaTask() See console...");
		codelet.call();
		
	}

	@Test
	public void testSetAssociatedModule() {
//		codelet.setAssociatedModule(globalWorkspace,ModuleUsage.TO_WRITE_TO);
//		
//		assertEquals("Problem with setAssociatedModule",globalWorkspace,codelet.globalWorkspace);
	}

}
