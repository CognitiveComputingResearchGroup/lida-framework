/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
public class UpdateCsmBackgroundTaskTest {

	@Test
	public final void testRunThisTask() {

		UpdateCsmBackgroundTask uct = new UpdateCsmBackgroundTask();

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
		WorkspaceBuffer CSMBuffer = new WorkspaceBufferImpl();

		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
		CSMBuffer.setModuleName(ModuleName.CurrentSituationalModel);

		WorkspaceImpl wMoudle = new WorkspaceImpl();
		wMoudle.addSubModule(perceptualBuffer);
		wMoudle.addSubModule(CSMBuffer);

		// Step 3-3:
		// Add node structure into workspaceBuffer of percetualBuffer
		perceptualBuffer.addBufferContent((WorkspaceContent) ns);

		// Testing of setAssociateMoudle()
		uct.setAssociatedModule(wMoudle, ModuleUsage.NOT_SPECIFIED);

		// Run method of target class
		uct.runThisFrameworkTask();

		// Check CSM Buffer
		NodeStructure ns2 = (NodeStructure) CSMBuffer.getModuleContent();

		assertTrue(
				"Problem with class UpdateCsmBackgroundTask for testRunThisFrameworkTask()",
				(NodeStructureImpl.compareNodeStructures(ns, ns2)));
	}

}
