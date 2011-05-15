package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;

public class WorkspaceBufferImplTest {

	@Test
	public final void testGetModuleContent() {
		//Creates node and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		n1.setActivation(0.2);
		ns.addDefaultNode(n1);
		
		//Create workspaceBuffer and add them into mockWorkspace
		WorkspaceImpl wMoudle = new WorkspaceImpl();
		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
		wMoudle.addSubModule(perceptualBuffer);
		
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);
		
		WorkspaceBuffer perceptualBuffer2 = (WorkspaceBuffer) wMoudle
		.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns2 = (NodeStructure) perceptualBuffer2.getModuleContent();
		
		assertTrue("Problem with class WorkspaceBufferImpl for GetModuleContent()",
				(NodeStructureImpl.compareNodeStructures(ns, ns2)));
	}

	@Test
	public final void testInit() {
		//NA
	}

	@Test
	public final void testWorkspaceBufferImpl() {
		NodeStructure ns = new NodeStructureImpl();
		WorkspaceBuffer buffer = new WorkspaceBufferImpl();
		String s1 = ns.toString();
		String s2 = ((NodeStructure)buffer.getBufferContent(null)).toString();
		assertTrue("Problem with class WorkspaceBufferImpl for workspaceBufferImpl()",
				 s1.equals(s2));

	}

	@Test
	public final void testDecayModule() {
		//Creates nodes and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		n1.setActivation(0.15);
		n1.setActivatibleRemovalThreshold(0.1);
		ns.addDefaultNode(n1);
		
		Node n2 = new NodeImpl();
		n2.setId(6);
		n2.setActivation(0.6);
		ns.addDefaultNode(n2);
		
		//Create workspaceBuffer and add them into mockWorkspace
		WorkspaceImpl wMoudle = new WorkspaceImpl();
		WorkspaceBuffer perceptualBuffer = new WorkspaceBufferImpl();
		perceptualBuffer.setModuleName(ModuleName.PerceptualBuffer);
		wMoudle.addSubModule(perceptualBuffer);
		
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);

		perceptualBuffer.taskManagerDecayModule(1);
		
		NodeStructure ns2 = (NodeStructure) perceptualBuffer.getModuleContent();

		// After node(Id == 2) is removed cause decay, so here is only node (Id == 6).
		assertTrue("Problem with class WorkspaceBufferImpl for DecayModule()",
				(ns2.containsNode(6))&&(!ns2.containsNode(2)));
	}

	@Test
	public final void testAddListener() {
		//NA
	}

	@Test
	public final void testAddBufferContent() {
		//Create a NodeStructure with NodeId = 2
		NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns.addDefaultNode(n1);
		
		// Add the NodeStructure to buffer
		WorkspaceBuffer buffer = new WorkspaceBufferImpl();
		buffer.addBufferContent((WorkspaceContent)ns);
		
		// Check whether action of adding is successful
		// In the same time, getBufferContent() method be tested too
		assertTrue("Problem with class WorkspaceBufferImpl for addBufferContent()",
				((NodeStructure)buffer.getBufferContent(null)).containsNode(2));
			
	}

	@Test
	public final void testGetBufferContent() {
	//Be tested in testAddBufferContent function above together
	}

}
