package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
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
		NodeStructure nss = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		n1.setActivation(0.2);
		ns.addDefaultNode(n1);
		
		Node n2 = new NodeImpl();
		n2.setId(6);
		n2.setActivation(0.6);
		ns.addDefaultNode(n2);
		nss.addDefaultNode(n2);
		
		Node n3 = new NodeImpl();
		n3.setId(8);
		n3.setActivation(0.8);
		ns.addDefaultNode(n3);
		nss.addDefaultNode(n3);

		//Step 3-2:
		//Create workspaceBuffer and add them into mockWorkspace
		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
		WorkspaceBuffer CSMBuffer = new WorkspaceBufferImpl();
		WorkspaceBuffer epsodicBuffer = new WorkspaceBufferImpl();
		
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
		CSMBuffer.setModuleName(ModuleName.CurrentSituationalModel);
		epsodicBuffer.setModuleName(ModuleName.EpisodicBuffer);
		
		
		WorkspaceImpl wMoudle = new WorkspaceImpl();
		wMoudle.addSubModule(perceptualBuffer);
		wMoudle.addSubModule(CSMBuffer);
		wMoudle.addSubModule(epsodicBuffer);
		
		//Create association between workspace and epsodic memory
		EpisodicMemoryImpl em = new EpisodicMemoryImpl();
		PerceptualAssociativeMemoryImpl pam = new PerceptualAssociativeMemoryImpl(); 
		em.init();
		em.setAssociatedModule(pam, 0);
		em.addListener(wMoudle);
		wMoudle.addCueListener(em);

		
		//Step 3-3:
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);
		
		//Run method of target class
		wst.setAssociatedModule(wMoudle, 0);
		wst.runThisLidaTask();
		
		//Check running result of target class
		//1> Check epsodic Buffer
		WorkspaceBuffer epsodicBuffer2 = (WorkspaceBuffer) wMoudle
		.getSubmodule(ModuleName.EpisodicBuffer);
		NodeStructure ns2 = (NodeStructure) epsodicBuffer2.getModuleContent();
		
		assertEquals("Problem with class RunThisLidaTask for cue()", nss,ns2);
		
		//2> Check CSM Buffer
		WorkspaceBuffer CSMBuffer2 = (WorkspaceBuffer) wMoudle
		.getSubmodule(ModuleName.CurrentSituationalModel);
		NodeStructure ns3 = (NodeStructure) CSMBuffer2.getModuleContent();
		
		assertEquals("Problem with class RunThisLidaTask for CSM()", ns,ns3);
		
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
