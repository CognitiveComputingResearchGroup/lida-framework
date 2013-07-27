/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.FrameworkTaskDef;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the
 * AttentionCodeletModule class.
 * 
 * @author Ryan J. McCall
 * 
 */
public class AttentionCodeletModuleTest {

	private AttentionCodeletModule attentionModule;
	private TaskSpawner taskSpawner;
	private WorkspaceBuffer csm;
	private WorkspaceImpl workspace;
	private MockGlobalWorkspaceImpl globalWorkspace;
	private Node node1, node2;
	private Link link1;
	private NodeStructure ns;
	private MockAttentionCodeletImpl codelet;
	private double epsilon = .000001;
	private static final ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		attentionModule = new AttentionCodeletModule();
		taskSpawner = new MockTaskSpawner();
		attentionModule.setAssistingTaskSpawner(taskSpawner);

		csm = new WorkspaceBufferImpl();
		csm.setModuleName(ModuleName.CurrentSituationalModel);
		workspace = new WorkspaceImpl();
		workspace.addSubModule(csm);

		globalWorkspace = new MockGlobalWorkspaceImpl();
		globalWorkspace.setModuleName(ModuleName.GlobalWorkspace);

		ns = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		PamNode category = (PamNode) factory.getNode("PamNodeImpl");
		link1 = factory.getLink(node1, node2, category);
		ns.addDefaultNode(node1);
		ns.addDefaultNode(node2);
		ns.addDefaultLink(link1);

		csm.addBufferContent((WorkspaceContent) ns);
		Workspace w = new WorkspaceImpl();
		w.addSubModule(csm);

		attentionModule.setAssociatedModule(w, "");
		attentionModule.setAssociatedModule(globalWorkspace, "");

		codelet = new MockAttentionCodeletImpl();
		codelet.setSoughtContent(ns);

		codelet.setAssociatedModule(csm, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(globalWorkspace, ModuleUsage.TO_WRITE_TO);

		Map<ModuleName, String> assoc = new HashMap<ModuleName, String>();
		assoc.put(ModuleName.CurrentSituationalModel, ModuleUsage.TO_READ_FROM);
		assoc.put(ModuleName.GlobalWorkspace, ModuleUsage.TO_WRITE_TO);

		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				NeighborhoodAttentionCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "NeighborhoodAttentionCodelet",
				new HashMap<String, Object>(), assoc);
		factory.addFrameworkTaskType(taskDef);
	}

	@Test
	public void testLearning() {
		Coalition c = new CoalitionImpl(ns, null);
		attentionModule.receiveBroadcast(c);
		// TODO finish
		// more testing when fully implemented
	}

	@Test
	public void testGetDefaultCodelet() {
		// Without associated modules
		AttentionCodeletImpl codelet = (AttentionCodeletImpl) attentionModule
				.getDefaultCodelet();
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);

		assertEquals(null, globalWorkspace.coalition);

		codelet.setSoughtContent(ns);
		codelet.runThisFrameworkTask();

		assertNotNull(globalWorkspace.coalition);
	}

	@Test
	public void testGetCodeletString() {
		AttentionCodeletImpl codelet = (AttentionCodeletImpl) attentionModule
				.getCodelet("foo");
		assertEquals(null, codelet);

		FrameworkTaskDef def = new FrameworkTaskDef(
				MockAttentionCodeletImpl.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "coolCodelet",
				new HashMap<String, Object>(),
				new HashMap<ModuleName, String>());
		factory.addFrameworkTaskType(def);
		codelet = (AttentionCodeletImpl) attentionModule
				.getCodelet("coolCodelet");
		assertTrue(codelet instanceof MockAttentionCodeletImpl);

		// with associated modules
		attentionModule.setAssociatedModule(workspace, null);
		attentionModule.setAssociatedModule(globalWorkspace, null);

		codelet = (AttentionCodeletImpl) attentionModule
				.getCodelet("NeighborhoodAttentionCodelet");
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);
		assertEquals(100, (int) codelet.getParam("arg0", 100));

		assertEquals(null, globalWorkspace.coalition);

		codelet.setSoughtContent(ns);
		codelet.runThisFrameworkTask();

		assertNotNull(globalWorkspace.coalition);
	}

	@Test
	public void testGetCodeletStringMapOfStringObject() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");

		AttentionCodelet codelet = attentionModule.getCodelet(
				"NeighborhoodAttentionCodelet", params);
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);
		assertEquals(10.0, codelet.getParam("arg0", 0.0), epsilon);
		assertEquals("Javier", codelet.getParam("name", ""));

		codelet = attentionModule.getCodelet("foo", params);
		assertEquals(null, codelet);

		params.put("arg0", 20.0);
		params.put("name", "Ryan");
		codelet = (AttentionCodeletImpl) attentionModule.getCodelet(
				"coolCodelet", params);
		assertTrue(codelet instanceof MockAttentionCodeletImpl);
		assertEquals(20.0, codelet.getParam("arg0", 0.0), epsilon);
		assertEquals("Ryan", codelet.getParam("name", ""));
	}

	@Test
	public void testGetDefaultCodeletMapOfStringObject() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");

		AttentionCodelet codelet = attentionModule.getDefaultCodelet(params);
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);
		assertEquals(10.0, (double) codelet.getParam("arg0", 0.0), epsilon);
		assertEquals("Javier", codelet.getParam("name", ""));

		params = null;
		codelet = attentionModule.getDefaultCodelet(params);
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);
		assertEquals(20.0, (double) codelet.getParam("arg0", 20.0), epsilon);
		assertEquals("Ryan", codelet.getParam("name", "Ryan"));
	}

	@Test
	public void testAddCodelet() {
		assertEquals(0, taskSpawner.getTasks().size());

		attentionModule.addCodelet(codelet);

		assertTrue(taskSpawner.containsTask(codelet));
		assertEquals(1, taskSpawner.getTasks().size());
	}

	@Test
	public void testGetModuleContent() {
		attentionModule.setAssociatedModule(globalWorkspace, "");

		Object[] o = null;
		Object content = attentionModule.getModuleContent(o);
		assertEquals(null, content);

		content = attentionModule.getModuleContent(new Object[] {});
		assertEquals(null, content);

		content = attentionModule.getModuleContent(new Object[0]);
		assertEquals(null, content);

		content = attentionModule.getModuleContent(new Object[] { null });
		assertEquals(null, content);

		content = attentionModule.getModuleContent(new Object[] { 2456 });
		assertEquals(null, content);

		content = attentionModule.getModuleContent(new Object[] { "hi" });
		assertEquals(null, content);

		content = attentionModule
				.getModuleContent(new Object[] { "globalworkspace" });
		assertEquals(globalWorkspace, content);

		content = attentionModule
				.getModuleContent(new Object[] { "GLOBALWORKSPACE" });
		assertEquals(globalWorkspace, content);
	}

	@Test
	public void testSetDefaultCodeletType() {
		AttentionCodelet codelet = attentionModule.getDefaultCodelet();
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);

		attentionModule.setDefaultCodeletType("34t90j");

		codelet = attentionModule.getDefaultCodelet();
		assertTrue(codelet instanceof NeighborhoodAttentionCodelet);

		assertTrue(factory.containsTaskType("coolCodelet"));
		attentionModule.setDefaultCodeletType("coolCodelet");

		codelet = attentionModule.getDefaultCodelet();
		assertTrue(codelet instanceof MockAttentionCodeletImpl);
	}

	@Test
	public void testAddListener() {
		// when implemented
	}

	@Test
	public void testDecayModule() {
		// when implemented
	}
}
