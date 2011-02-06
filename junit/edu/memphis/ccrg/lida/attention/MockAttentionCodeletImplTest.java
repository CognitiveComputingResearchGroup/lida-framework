package edu.memphis.ccrg.lida.attention;


import static org.junit.Assert.*;

import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

public class MockAttentionCodeletImplTest {
	
	MockAttentionCodeletImpl codelet;
	WorkspaceBufferImpl buffer;
	GlobalWorkspace globalWorkspace;
	LidaModule module;
	NodeImpl node1,node2,node3;
	LinkImpl link1,link2;
	NodeStructure csmContent;
	CoalitionImpl coalition;
	
	
	

	@Before
	public void setUp() throws Exception {
		module = new GlobalWorkspaceImpl();
		codelet = new MockAttentionCodeletImpl();
		
		module.setModuleName(ModuleName.GlobalWorkspace);
		
		buffer = new WorkspaceBufferImpl();
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		
		csmContent= new NodeStructureImpl();
		
		node1.setId(1);
		node2.setId(2);
		node3.setId(3);		
				
		link1 = new LinkImpl(node1,node2,LinkCategoryNode.CHILD);
		link2 = new LinkImpl(node2,node3,LinkCategoryNode.CHILD);
		
		csmContent.addNode(node1);
		csmContent.addNode(node2);
		csmContent.addNode(node3);
		csmContent.addLink(link1);
		csmContent.addLink(link2);
		
		double activation;
		
		coalition = new CoalitionImpl ((BroadcastContent)csmContent, activation);
		
		
		
		
	}
 /*
  * protected void runThisLidaTask() {
		if (hasSoughtContent(currentSituationalModel)) {
			NodeStructure csmContent = getWorkspaceContent(currentSituationalModel);
			if (csmContent.getLinkableCount() > 0){
				globalWorkspace.addCoalition(new CoalitionImpl((BroadcastContent)csmContent, getActivation()));
				logger.log(Level.FINE, this + " adds coalition", LidaTaskManager.getCurrentTick());
			}
		}
	}
	
  */
	@Test
	public void testRunThisLidaTask() {
		codelet.runThisLidaTask();
		codelet.setSoughtContent(csmContent);
	    
		assertEquals("Problem ",true, globalWorkspace.addCoalition(coalition));
		
		
	}

	@Test
	public void testSetAssociatedModule() {
		codelet.setAssociatedModule(module,ModuleUsage.TO_WRITE_TO);
		
		assertEquals("Problem with setAssociatedModule",ModuleName.GlobalWorkspace,codelet.globalWorkspace.getModuleName());
	}

}
