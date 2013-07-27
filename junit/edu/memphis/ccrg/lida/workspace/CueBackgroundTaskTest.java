/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl;

/**
 * 
 * @author Daqi
 * 
 */

public class CueBackgroundTaskTest {

	@Test
	public final void testRunThisTask() {
		CueBackgroundTask cbt = new CueBackgroundTask();

		// Initialize with assigned value
		Map<String, Object> mapParas = new HashMap<String, Object>();
		mapParas.put("workspace.actThreshold", 0.5);
		cbt.init(mapParas);
		// Testing of method init();
		cbt.init();

		// step 3-1:
		// Create 3 nodes and add them into a node structure
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

		// Step 3-2:
		// Create workspaceBuffer and add them into mockWorkspace
		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);

		mockWorkspace wMoudle = new mockWorkspace();
		wMoudle.addSubModule(perceptualBuffer);

		// Step 3-3:
		// Add node structure into workspaceBuffer of percetualBuffer
		perceptualBuffer.addBufferContent((WorkspaceContent) ns);

		// Testing of setAssociateModule()
		cbt.setAssociatedModule(wMoudle, ModuleUsage.NOT_SPECIFIED);

		// Run method of target class
		cbt.runThisFrameworkTask();

	}

	@Test
	public final void testInit() {
		// Init() be tested in testRunThisFrameworkTask method above with
		// testing of
		// RunThisFrameworkTask() together.
	}

	@Test
	public final void testSetAssociatedModule() {
		// SetAssociatedModule() be tested in testRunThisFrameworkTask method
		// above with testing of
		// RunThisFrameworkTask() together.
	}
}

class mockWorkspace extends FrameworkModuleImpl implements Workspace {

	@Override
	public void cueEpisodicMemories(NodeStructure ns) {
		// Node (Id = 2) is removed cause activation(0.2) < actThreshold(0.5),
		// so here are only node (Id == 6) and node (Id == 8).
		assertTrue(
				"Problem with class RunThisFrameworkTask for testRunThisFrameworkTask()",
				(ns.getNode(2) == null) && (ns.getNode(6) != null)
						&& (ns.getNode(8) != null) && (ns.getNodeCount() == 2));

	}

	@Override
	public void addListener(ModuleListener listener) {

	}

	@Override
	public void addCueListener(CueListener l) {

	}

	@Override
	public void addWorkspaceListener(WorkspaceListener l) {

	}

	@Override
	public Object getModuleContent(Object... params) {

		return null;
	}

	@Override
	public void init() {

	}

	@Override
	public void decayModule(long ticks) {

	}

}
