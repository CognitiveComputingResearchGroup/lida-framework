package edu.memphis.ccrg.lida.attention;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

public class AttentionModuleImplTest {
	
	AttentionModuleImpl attnModule;
	Workspace workspace;
	WorkspaceBuffer csm;
	GlobalWorkspace globalWorkspace;
	BroadcastContent bc;
	Coalition coal;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure ns;
	TaskSpawner taskSpawner;
	MockAttentionCodeletImpl codelet;
	LidaElementFactory factory = LidaElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		attnModule = new AttentionModuleImpl();
		globalWorkspace= new GlobalWorkspaceImpl();
		workspace = new WorkspaceImpl();
		csm = new WorkspaceBufferImpl();
		ns = new NodeStructureImpl();	
		TaskSpawner taskSpawner = new MockTaskSpawner();
		codelet = new MockAttentionCodeletImpl();
		
		csm.setModuleName(ModuleName.CurrentSituationalModel);
		workspace.addSubModule(csm);
		attnModule.setAssistingTaskSpawner(taskSpawner);
		
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
				
		link1 = new LinkImpl(node1,node2,LinkCategoryNode.CHILD);
		link2 = new LinkImpl(node2,node3,LinkCategoryNode.CHILD);
						
		ns.addDefaultNode(node1);
		ns.addDefaultNode(node2);
		ns.addDefaultNode(node3);
		ns.addDefaultLink(link1);
		ns.addDefaultLink(link2);	
		
		codelet.setSoughtContent(ns);	
		NodeStructure model = (NodeStructure) csm.getModuleContent();
		model.mergeWith(ns);
		
		codelet.setAssociatedModule(csm, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(globalWorkspace, ModuleUsage.TO_WRITE_TO);
		
	}
	
	@Test
	public void testSetAssociatedModule() {
		attnModule.setAssociatedModule(globalWorkspace,ModuleUsage.TO_WRITE_TO);
		assertEquals("Problem with setAssociatedModule",globalWorkspace,attnModule.getModuleContent("GlobalWorkspace"));
		
		attnModule.setAssociatedModule(workspace,ModuleUsage.TO_READ_FROM);
		assertEquals("Problem with setAssociatedModule",csm,workspace.getSubmodule(ModuleName.CurrentSituationalModel));
	
	}

	@Test
	public void testReceiveBroadcast() {	
		System.out.println("Testing receiveBroadcast() method. See console...");
		attnModule.receiveBroadcast((BroadcastContent)ns);		
	}

	@Test
	public void testGetNewAttentionCodelet() {
		
		AttentionCodelet codelet = attnModule.getNewCodelet();		
		assertNotNull("Problem with getNewAttentionCodelet()", codelet);		
	}

	@Test
	public void testRunAttentionCodelet() {
		
		System.out.println("Testing runAttentionCodelet() method. See console...");		
		attnModule.runCodelet(codelet);
	}	
}
