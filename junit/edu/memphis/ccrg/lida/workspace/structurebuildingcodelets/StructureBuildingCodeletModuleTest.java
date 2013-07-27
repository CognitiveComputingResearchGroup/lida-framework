/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

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
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;

public class StructureBuildingCodeletModuleTest {

	private StructureBuildingCodeletModule sbcModule;
	private TaskSpawner taskSpawner;
	private MockWorkspaceBufferImpl perceptualBuffer, csm;
	private WorkspaceImpl workspace;
	private Node node1, node2;
	private Link link1;
	private NodeStructure ns;
	private MockStructureBuildingCodeletImpl codelet;
	private double epsilon = .000001;
	private static final ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		sbcModule = new StructureBuildingCodeletModule();
		taskSpawner = new MockTaskSpawner();
		sbcModule.setAssistingTaskSpawner(taskSpawner);

		csm = new MockWorkspaceBufferImpl();
		csm.setModuleName(ModuleName.CurrentSituationalModel);

		perceptualBuffer = new MockWorkspaceBufferImpl();
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);

		workspace = new WorkspaceImpl();
		workspace.addSubModule(csm);
		workspace.addSubModule(perceptualBuffer);
		sbcModule.setAssociatedModule(workspace, "");

		ns = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		PamNode category = (PamNode) factory.getNode("PamNodeImpl");
		link1 = factory.getLink(node1, node2, category);
		ns.addDefaultNode(node1);
		ns.addDefaultNode(node2);
		ns.addDefaultLink(link1);

		csm.addBufferContent((WorkspaceContent) ns);

		codelet = new MockStructureBuildingCodeletImpl();
		codelet.setSoughtContent(ns);

		codelet.setAssociatedModule(csm, ModuleUsage.TO_WRITE_TO);
		codelet.setAssociatedModule(perceptualBuffer, ModuleUsage.TO_READ_FROM);

		Map<ModuleName, String> assoc = new HashMap<ModuleName, String>();
		assoc.put(ModuleName.PerceptualBuffer, ModuleUsage.TO_READ_FROM);
		assoc.put(ModuleName.CurrentSituationalModel, ModuleUsage.TO_WRITE_TO);

		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				BasicStructureBuildingCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "BasicStructureBuildingCodelet",
				new HashMap<String, Object>(), assoc);
		factory.addFrameworkTaskType(taskDef);
	}

	@Test
	public void testGetDefaultCodelet() {
		sbcModule.setAssociatedModule(workspace, "");
		BasicStructureBuildingCodelet codelet = (BasicStructureBuildingCodelet) sbcModule
				.getDefaultCodelet();
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
	}

	@Test
	public void testGetDefaultCodeletMapOfStringObject() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");
		BasicStructureBuildingCodelet codelet = (BasicStructureBuildingCodelet) sbcModule
				.getDefaultCodelet(params);

		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);

		codelet = (BasicStructureBuildingCodelet) sbcModule
				.getDefaultCodelet(params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);

		Map<ModuleName, String> assoc = new HashMap<ModuleName, String>();
		assoc.put(ModuleName.PerceptualBuffer, ModuleUsage.TO_WRITE_TO);
		assoc.put(ModuleName.CurrentSituationalModel, ModuleUsage.TO_READ_FROM);

		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				BasicStructureBuildingCodelet.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "BasicStructureBuildingCodelet",
				new HashMap<String, Object>(), assoc);
		factory.addFrameworkTaskType(taskDef);

		codelet = (BasicStructureBuildingCodelet) sbcModule
				.getDefaultCodelet(params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(csm));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(perceptualBuffer, codelet.writableBuffer);
	}

	@Test
	public void testGetCodeletString() {
		sbcModule.setAssociatedModule(workspace, "");
		StructureBuildingCodeletImpl codelet = (StructureBuildingCodeletImpl) sbcModule
				.getCodelet("23");
		assertEquals(null, codelet);

		Map<ModuleName, String> assoc = new HashMap<ModuleName, String>();
		assoc.put(ModuleName.PerceptualBuffer, ModuleUsage.TO_READ_FROM);
		assoc.put(ModuleName.CurrentSituationalModel, ModuleUsage.TO_WRITE_TO);

		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				MockStructureBuildingCodeletImpl.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "coolCodelet",
				new HashMap<String, Object>(), assoc);
		factory.addFrameworkTaskType(taskDef);

		codelet = (StructureBuildingCodeletImpl) sbcModule
				.getCodelet("coolCodelet");
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
		assertEquals(100, (int) codelet.getParam("arg0", 100));
	}

	@Test
	public void testGetCodeletStringMapOfStringObject() {
		sbcModule.setAssociatedModule(workspace, "");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Ryan");

		StructureBuildingCodeletImpl codelet = (StructureBuildingCodeletImpl) sbcModule
				.getCodelet("223cd3", params);
		assertEquals(null, codelet);

		codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet(
				"coolCodelet", params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
		assertEquals(10.0, (double) codelet.getParam("arg0", 0.0), epsilon);

		Map<ModuleName, String> assoc = new HashMap<ModuleName, String>();
		assoc.put(ModuleName.CurrentSituationalModel, ModuleUsage.TO_READ_FROM);
		assoc.put(ModuleName.PerceptualBuffer, ModuleUsage.TO_WRITE_TO);

		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				MockStructureBuildingCodeletImpl.class.getCanonicalName(), 1,
				new HashMap<String, String>(), "coolCodelet",
				new HashMap<String, Object>(), assoc);
		factory.addFrameworkTaskType(taskDef);

		codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet(
				"coolCodelet", params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.containsValue(csm));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(perceptualBuffer, codelet.writableBuffer);
	}

	@Test
	public void testSetDefaultCodeletType() {
		sbcModule.setAssociatedModule(workspace, "");
		StructureBuildingCodelet codelet = sbcModule.getDefaultCodelet();
		assertTrue(codelet instanceof BasicStructureBuildingCodelet);

		sbcModule.setDefaultCodeletType("34t90j");

		codelet = sbcModule.getDefaultCodelet();
		assertTrue(codelet instanceof BasicStructureBuildingCodelet);

		assertTrue(factory.containsTaskType("coolCodelet"));
		sbcModule.setDefaultCodeletType("coolCodelet");

		codelet = sbcModule.getDefaultCodelet();
		assertTrue(codelet instanceof MockStructureBuildingCodeletImpl);
	}

	@Test
	public void testAddCodelet() {
		assertEquals(0, taskSpawner.getTasks().size());

		sbcModule.addCodelet(codelet);

		assertTrue(taskSpawner.containsTask(codelet));
		assertEquals(1, taskSpawner.getTasks().size());
	}

	@Test
	public void testDecayModule() {
	}

}
