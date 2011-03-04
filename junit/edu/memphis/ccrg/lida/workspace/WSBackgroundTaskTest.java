package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

import edu.memphis.ccrg.lida.workspace.workspaceBuffer.*;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.episodicmemory.*;
import edu.memphis.ccrg.lida.pam.*;

import java.util.HashMap;
import java.util.Map;

public class WSBackgroundTaskTest {

	@Test
	public final void testRunThisLidaTask() throws Exception{

		WSBackgroundTask wst = new WSBackgroundTask();
		

		// Initialize with assigned value
		Map<String, Object> mapParas = new HashMap<String, Object>();
		mapParas.put("workspace.actThreshold", 0.5);
		mapParas.put("workspace.cueFrequency", 0);
		wst.init(mapParas);
		// Testing of method init();	
		wst.init();
		
		//Create a mockWorkspace
		//step 3-1:
		//Create 3 nodes and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		n1.setActivation(0.2);
		ns.addDefaultNode(n1);
		
		Node n2 = new NodeImpl();
		n2.setId(6);
		n2.setActivation(0.6);
		ns.addDefaultNode(n2);
		
		Node n3 = new NodeImpl();
		n3.setId(8);
		n3.setActivation(0.8);
		ns.addDefaultNode(n3);

		//Step 3-2:
		//Create workspaceBuffer and add them into mockWorkspace
		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
		WorkspaceBuffer CSMBuffer = new WorkspaceBufferImpl();
		WorkspaceBuffer epsodicBuffer = new WorkspaceBufferImpl();
		
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
		CSMBuffer.setModuleName(ModuleName.CurrentSituationalModel);
		//For test only, because actually there is not episodicBuffer.
		epsodicBuffer.setModuleName(ModuleName.EpisodicBuffer);
		
		
		mockWorkspaceImpl wMoudle = new mockWorkspaceImpl();
		wMoudle.addSubModule(perceptualBuffer);
		wMoudle.addSubModule(CSMBuffer);
		wMoudle.addSubModule(epsodicBuffer);
		
		//Step 3-3:
		// Add node structure into workspaceBuffer of percetualBuffer
		((NodeStructure)wMoudle.getSubmodule(ModuleName.PerceptualBuffer)
				.getModuleContent()).mergeWith(ns);
		
		//Run method of target class
		wst.setAssociatedModule(wMoudle, 0);
		wst.runThisLidaTask();
		
		//Check running result of target class
		//1> Check epsodic Buffer
		WorkspaceBuffer epsodicBuffer2 = (WorkspaceBuffer) wMoudle
		.getSubmodule(ModuleName.EpisodicBuffer);
		NodeStructure ns2 = (NodeStructure) epsodicBuffer2.getModuleContent();

		// In method cue(), after node(Id = 2) is removed cause activation(0.2) < actThreshold(0.5),
		// so here is only node (Id == 6)and node (Id == 8).
		assertTrue("Problem with class RunThisLidaTask for cue()",
				(ns2.getNode(2) == null)&&(ns2.getNode(6) != null)&&(ns2.getNode(8) != null)
				&&(ns2.getNodeCount() == 2));
		
		//2> Check CSM Buffer
		WorkspaceBuffer CSMBuffer2 = (WorkspaceBuffer) wMoudle
		.getSubmodule(ModuleName.CurrentSituationalModel);
		NodeStructure ns3 = (NodeStructure) CSMBuffer2.getModuleContent();
		
		// In method CSM(), both 3 nodes is retrieved from perceptual Buffer to CSM
		assertTrue("Problem with class RunThisLidaTask for cue()",
				(ns3.getNode(2) != null)&&(ns3.getNode(6) != null)&&(ns3.getNode(8) != null)
				&&(ns3.getNodeCount() == 3));
		
	}

	@Test
	public final void testInit() throws Exception {
		//Init() be tested in testRunThisLidaTask method above with testing of 
		//RunThisLidaTask() together.

	}

	@Test
	public final void testSetAssociatedModule() throws Exception {
		//SetAssociatedModule() be tested in testRunThisLidaTask method above with testing of 
		//RunThisLidaTask() together.

	}

	@Test
	public final void testToString() {
		WSBackgroundTask wst = new WSBackgroundTask();
		String sAns = "WSBackgroundTask";
		
		assertEquals("Problem with class RunThisLidaTask for testToString()", wst.toString(), sAns);

	}

}

class mockWorkspaceImpl  extends LidaModuleImpl implements Workspace {
	

	@Override
	public void cueEpisodicMemories(NodeStructure ns) {
		//Temporally save content of nodeStructure to episodicBuffer for test.
		WorkspaceBuffer csm = (WorkspaceBuffer)getSubmodule(ModuleName.EpisodicBuffer);
		((NodeStructure) csm.getModuleContent()).mergeWith(ns);
		
	}

	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCueListener(CueListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWorkspaceListener(WorkspaceListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getModuleContent(Object... params) {
		// Not applicable
		throw new UnsupportedOperationException();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
