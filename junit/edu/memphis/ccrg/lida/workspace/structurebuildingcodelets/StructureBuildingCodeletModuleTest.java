package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * 
 * @author Daqi
 *
 */
public class StructureBuildingCodeletModuleTest {

	@Test
	public final void testGetModuleContent() {
		// N/A
	}

	@Test
	public final void testInit() {
		// N/A
	}

	@Test
	public final void testDecayModule() {
		//Skip because implement does not yet complete.
		
	}

	@Test
	public final void testSetAssociatedModule() {
		
		StructureBuildingCodeletModule sbcm = new StructureBuildingCodeletModule();
		
		//Creates node and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		NodeStructure ns2 = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(1);
		ns.addDefaultNode(n1);
		
		Node n2 = new NodeImpl();
		n2.setId(6);
		ns2.addDefaultNode(n2);
		
		WorkspaceImpl wMoudle = new WorkspaceImpl();
		
		//Create workspaceBuffer of PerceptualBuffer and add it to workspace
		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
		wMoudle.addSubModule(perceptualBuffer);
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);
		
		//Create workspaceBuffer of CurrentSituationalModel and add it to workspace
		WorkspaceBuffer CSMBuffer = new WorkspaceBufferImpl();
		CSMBuffer.setModuleName(ModuleName.CurrentSituationalModel);
		wMoudle.addSubModule(CSMBuffer);
		//Add node 
		((NodeStructure) CSMBuffer.getBufferContent(null)).mergeWith(ns2);
		
		sbcm.setAssociatedModule(wMoudle, null);
		
		//Also test for getCodelet(String)
		ElementFactory factory = ElementFactory.getInstance();
		factory.addCodeletType(mockStructureBuildingCodeletImpl.class.getSimpleName(), 
				mockStructureBuildingCodeletImpl.class.getCanonicalName());
		
		//Set a mock CodeletType to default type for testing defaultCodelet
		sbcm.setDefaultCodeletType(mockStructureBuildingCodeletImpl.class.getSimpleName());
		
		
		//Test for getCodelet(String)
		mockStructureBuildingCodeletImpl mockSbcm = (mockStructureBuildingCodeletImpl) sbcm.getCodelet("mockStructureBuildingCodeletImpl");
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm.getReadableBuffers()).contains(perceptualBuffer));
		
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm.getWritableBuffer()).equals(CSMBuffer));
		
		//Test for getCodelet(String, Map<String, Object>)
		mockStructureBuildingCodeletImpl mockSbcm2 = (mockStructureBuildingCodeletImpl)sbcm.getCodelet("mockStructureBuildingCodeletImpl", null);
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm2.getReadableBuffers()).contains(perceptualBuffer));
		
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm2.getWritableBuffer()).equals(CSMBuffer));
		
			
		//Test for getDefaultCodelet()
		mockStructureBuildingCodeletImpl mockSbcm3 = (mockStructureBuildingCodeletImpl) sbcm.getDefaultCodelet();
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm3.getReadableBuffers()).contains(perceptualBuffer));
		
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm3.getWritableBuffer()).equals(CSMBuffer));

		//Test for getDefaultCodelet(Map<String, Object>)
		mockStructureBuildingCodeletImpl mockSbcm4 = (mockStructureBuildingCodeletImpl) sbcm.getDefaultCodelet(null);
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm4.getReadableBuffers()).contains(perceptualBuffer));
		
		assertTrue("Problem with class StructureBuildingCodeletModule for testSetAssociatedModule()",
				(mockSbcm4.getWritableBuffer()).equals(CSMBuffer));
		
}

	@Test
	public final void testStructureBuildingCodeletModule() {
		// Tested in testAddFrameworkGuiEventListener() and testGetDefaultCodelet()
	}

	@Test
	public final void testAddFrameworkGuiEventListener() {
		StructureBuildingCodeletModule sbcm = new StructureBuildingCodeletModule();
		
		FrameworkGuiEvent fge = new FrameworkGuiEvent(ModuleName.LIDA, "01", new Object());
		mockFrameworkGuiEventListener mockgui = new mockFrameworkGuiEventListener();
		sbcm.addFrameworkGuiEventListener(mockgui);
		sbcm.sendEventToGui(fge);
		
	}

	@Test
	public final void testSendEventToGui() {
		//Be tested in testAddFrameworkGuiEventListener() method
	}

	@Test
	public final void testGetDefaultCodelet() {
		//Be tested in testSetAssociatedModule() method
	}

	@Test
	public final void testAddCodelet() {
		//Creates node and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
	    Node n1 = new NodeImpl();
		n1.setId(9);
		ns.addDefaultNode(n1);
		
		//Create a mock StructureBuildingCodelet and spawner
		StructureBuildingCodelet sbc = new mockStructureBuildingCodeletImpl();
		sbc.setSoughtContent(ns);
		mockTaskSpawner mockSpawner = new mockTaskSpawner();
		
		StructureBuildingCodeletModule sbcm = new StructureBuildingCodeletModule();
		sbcm.setAssistingTaskSpawner(mockSpawner);
		sbcm.addCodelet(sbc);
		
	}

	@Test
	public final void testToString() {
		StructureBuildingCodeletModule sbcm = new StructureBuildingCodeletModule();
		assertTrue("Problem with class StructureBuildingCodeletModule for testToString()",
				((ModuleName.StructureBuildingCodeletModule + "")).equals(sbcm.toString()));

	}

	@Test
	public final void testAddListener() {
		// N/A
	}

	@Test
	public final void testGetDefaultCodeletMapOfStringObject() {
		//Be tested in testSetAssociatedModule() method
		
	}
	
	@Test
	public final void testGetCodeletString() {
		//Be tested in testSetAssociatedModule() method
	}
	
	@Test
	public final void testGetCodeletStringMapOfStringObject() {
		//Be tested in testSetAssociatedModule() method
	}
}


class mockFrameworkGuiEventListener implements FrameworkGuiEventListener{

	@Override
	public void receiveFrameworkGuiEvent(FrameworkGuiEvent event) {
		assertTrue("Problem with class StructureBuildingCodeletModule for testAddFrameworkGuiEventListener()",
				event.getMessage() == "01");
		
	}
	
}

class mockTaskSpawner extends TaskSpawnerImpl implements TaskSpawner{
	@Override
	public void addTask(LidaTask task){
		NodeStructure ns = ((StructureBuildingCodelet)task).getSoughtContent();
		assertTrue("Problem with class StructureBuildingCodeletModule for testAddCodelet()",
				(ns.getNode(9) != null)&&(ns.getNodeCount() == 1));
	}
}

