package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.WSBackgroundTask;
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
		
		for (Node n : ns.getNodes()) {
			for (Node n2 : ns2.getNodes()) {
				assert (n.getId() == n2.getId());
				}
		}


	}

	@Test
	public final void testInit() {
		//Initializing of nodeStructures when 
		WorkspaceBufferImpl wsb = new WorkspaceBufferImpl();
		
		// Initialize with assigned value
		Map<String, Object> mapParas = new HashMap<String, Object>();
		mapParas.put("removableThreshold", 0.05);
		wsb.init(mapParas);
		wsb.init();
		
		assert (((NodeStructure)wsb.getModuleContent()).getLowerActivationBound() == 0.05);
		
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
		
		WorkspaceBuffer perceptualBuffer2 = (WorkspaceBuffer) wMoudle
		.getSubmodule(ModuleName.PerceptualBuffer);

		perceptualBuffer2.decayModule(30);
		
		NodeStructure ns2 = (NodeStructure) perceptualBuffer2.getModuleContent();
		int yy = 2;
		for (Node n : ns2.getNodes()) {
			System.out.println("The node Id is " + n.getId());

			//assert (n.getId() != 2 );
		}
		

	}

	@Test
	public final void testWorkspaceBufferImpl() {
		// Tested in testInit() method above.

	}

	@Test
	public final void testSetRemovalThreshold() {
		// Tested in testInit() method above.
	}

}
