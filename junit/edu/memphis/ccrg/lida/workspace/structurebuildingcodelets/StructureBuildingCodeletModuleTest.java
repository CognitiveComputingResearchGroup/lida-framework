package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;

public class StructureBuildingCodeletModuleTest {

	private StructureBuildingCodeletModule sbcModule;
	private TaskSpawner taskSpawner;
	private MockWorkspaceBufferImpl perceptualBuffer, csm;
	private WorkspaceImpl workspace;
	private Node node1,node2;
	private Link link1;
	private NodeStructure ns;
	private MockStructureBuildingCodeletImpl codelet;
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
		
		ns = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		PamNode category = (PamNode) factory.getNode("PamNodeImpl");
		link1 = factory.getLink(node1,node2,category);
		ns.addDefaultNode(node1);
		ns.addDefaultNode(node2);
		ns.addDefaultLink(link1);	
		
		csm.addBufferContent((WorkspaceContent) ns);
		
		codelet = new MockStructureBuildingCodeletImpl();
		codelet.setSoughtContent(ns);	
	
		codelet.setAssociatedModule(csm, ModuleUsage.TO_WRITE_TO);
		codelet.setAssociatedModule(perceptualBuffer, ModuleUsage.TO_READ_FROM);
	}

	@Test
	public void testGetDefaultCodelet() {
		//test1
		BasicStructureBuildingCodelet codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet();
		assertEquals(null, codelet);

		//test2
		sbcModule.setAssociatedModule(csm, "");		
		codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet();
		assertEquals(null, codelet);
		
		//test3
		workspace = new WorkspaceImpl();
		workspace.addSubModule(csm);
		sbcModule.setAssociatedModule(workspace, "");
		codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet();
		assertEquals(null, codelet);
		
		//test4
		workspace.addSubModule(perceptualBuffer);
		sbcModule.setAssociatedModule(workspace, "");
		codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet();
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.contains(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
	}
	
	@Test
	public void testGetDefaultCodeletMapOfStringObject() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");
		BasicStructureBuildingCodelet codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet(params);
		
		assertEquals(null, codelet);
		
		sbcModule.setAssociatedModule(workspace, "");
		
		codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet(params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.contains(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);

		params.put(ModuleUsage.TO_READ_FROM, "PerceptualBuffer");
		params.put(ModuleUsage.TO_WRITE_TO, "CurrentSituationalModel");
		
		codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet(params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.contains(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
		
		params.put(ModuleUsage.TO_READ_FROM, "CurrentSituationalModel");
		params.put(ModuleUsage.TO_WRITE_TO, "PerceptualBuffer");
		
		codelet = (BasicStructureBuildingCodelet) sbcModule.getDefaultCodelet(params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.contains(csm));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(perceptualBuffer, codelet.writableBuffer);
	}

	@Test
	public void testGetCodeletString() {
		sbcModule.setAssociatedModule(workspace, "");
		StructureBuildingCodeletImpl codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet("23");
		assertEquals(null, codelet);
		
		factory.addCodeletType("coolCodelet", MockStructureBuildingCodeletImpl.class.getCanonicalName());
		
		codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet("coolCodelet");
		assertNotNull(codelet);		
		assertTrue(codelet.readableBuffers.contains(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
		assertEquals(100, codelet.getParam("arg0", 100));
	}

	@Test
	public void testGetCodeletStringMapOfStringObject() {
		sbcModule.setAssociatedModule(workspace, "");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Ryan");
		
		StructureBuildingCodeletImpl codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet("223cd3", params);
		assertEquals(null, codelet);
		
		codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet("coolCodelet", params);
		assertNotNull(codelet);		
		assertTrue(codelet.readableBuffers.contains(perceptualBuffer));
		assertEquals(1, codelet.readableBuffers.size());
		assertEquals(csm, codelet.writableBuffer);
		assertEquals(100, codelet.getParam("arg0", 100));
		
		params.put(ModuleUsage.TO_READ_FROM, "CurrentSituationalModel");
		params.put(ModuleUsage.TO_WRITE_TO, "PerceptualBuffer");
		
		codelet = (StructureBuildingCodeletImpl) sbcModule.getCodelet("coolCodelet",params);
		assertNotNull(codelet);
		assertTrue(codelet.readableBuffers.contains(csm));
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
		
		assertTrue(factory.containsCodeletType("coolCodelet"));
		sbcModule.setDefaultCodeletType("coolCodelet");
		
		codelet = sbcModule.getDefaultCodelet();
		assertTrue(codelet instanceof MockStructureBuildingCodeletImpl);
	}

	@Test
	public void testAddCodelet() {
		assertEquals(0, taskSpawner.getRunningTasks().size());
		
		sbcModule.addCodelet(codelet);
		
		assertTrue(taskSpawner.containsTask(codelet));
		assertEquals(1, taskSpawner.getRunningTasks().size());
	}

	@Test
	public void testToString() {
		assertEquals(ModuleName.StructureBuildingCodeletModule.toString(), sbcModule.toString());
	}
	
	@Test
	public void testGetModuleContent() {
	}
	@Test
	public void testInit() {
	}
	@Test
	public void testAddListener() {
	}
	@Test
	public void testDecayModule() {
	}

}
