package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

public class StructureBuildingCodeletModuleTest {

	private StructureBuildingCodeletModule sbcModule;
	private TaskSpawner taskSpawner;
	private WorkspaceBuffer perceptualBuffer, csm;
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
		
		csm = new WorkspaceBufferImpl();
		csm.setModuleName(ModuleName.CurrentSituationalModel);
		
		perceptualBuffer = new WorkspaceBufferImpl();
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
	public void testSetAssociatedModule() {
//StructureBuildingCodeletModule sbcm = new StructureBuildingCodeletModule();
//		
//		//Creates node and add them into a node structure
//		NodeStructure ns = new NodeStructureImpl();
//		NodeStructure ns2 = new NodeStructureImpl();
//		
//		Node n1 = new NodeImpl();
//		n1.setId(1);
//		ns.addDefaultNode(n1);
//		
//		Node n2 = new NodeImpl();
//		n2.setId(6);
//		ns2.addDefaultNode(n2);
//		
//		WorkspaceImpl wMoudle = new WorkspaceImpl();
//		
//		//Create workspaceBuffer of PerceptualBuffer and add it to workspace
//		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
//		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
//		wMoudle.addSubModule(perceptualBuffer);
//		// Add node structure into workspaceBuffer of percetualBuffer
//		wMoudle.receivePercept(ns);
//		
//		//Create workspaceBuffer of CurrentSituationalModel and add it to workspace
//		WorkspaceBuffer CSMBuffer = new WorkspaceBufferImpl();
//		CSMBuffer.setModuleName(ModuleName.CurrentSituationalModel);
//		wMoudle.addSubModule(CSMBuffer);
//		//Add node 
//		((NodeStructure) CSMBuffer.getBufferContent(null)).mergeWith(ns2);
//		
//		sbcm.setAssociatedModule(wMoudle, null);
//		
//		//Also test for getCodelet(String)
//		ElementFactory factory = ElementFactory.getInstance();
//		factory.addCodeletType(MockStructureBuildingCodeletImpl.class.getSimpleName(), 
//				MockStructureBuildingCodeletImpl.class.getCanonicalName());
//		
//		//Set a mock CodeletType to default type for testing defaultCodelet
//		sbcm.setDefaultCodeletType(MockStructureBuildingCodeletImpl.class.getSimpleName());
//		
//		
//		//Test for getCodelet(String)
//		MockStructureBuildingCodeletImpl mockSbcm = (MockStructureBuildingCodeletImpl) sbcm.getCodelet("MockStructureBuildingCodeletImpl");
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm.getReadableBuffers()).contains(perceptualBuffer));
//		
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm.getWritableBuffer()).equals(CSMBuffer));
//		
//		//Test for getCodelet(String, Map<String, Object>)
//		MockStructureBuildingCodeletImpl mockSbcm2 = (MockStructureBuildingCodeletImpl)sbcm.getCodelet("MockStructureBuildingCodeletImpl", null);
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm2.getReadableBuffers()).contains(perceptualBuffer));
//		
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm2.getWritableBuffer()).equals(CSMBuffer));
//		
//			
//		//Test for getDefaultCodelet()
//		MockStructureBuildingCodeletImpl mockSbcm3 = (MockStructureBuildingCodeletImpl) sbcm.getDefaultCodelet();
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm3.getReadableBuffers()).contains(perceptualBuffer));
//		
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm3.getWritableBuffer()).equals(CSMBuffer));
//
//		//Test for getDefaultCodelet(Map<String, Object>)
//		MockStructureBuildingCodeletImpl mockSbcm4 = (MockStructureBuildingCodeletImpl) sbcm.getDefaultCodelet(null);
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm4.getReadableBuffers()).contains(perceptualBuffer));
//		
//		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
//				(mockSbcm4.getWritableBuffer()).equals(CSMBuffer));
	}

	@Test
	public void testStructureBuildingCodeletModule() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddFrameworkGuiEventListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDefaultCodeletType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendEventToGui() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultCodeletMapOfStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultCodelet() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCodeletString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCodeletStringMapOfStringObject() {
		fail("Not yet implemented");
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
