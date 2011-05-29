/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl;

public class WorkspaceBufferImplTest {

	private WorkspaceBufferImpl buffer;
	@Before
	public void setUp() {
		buffer = new WorkspaceBufferImpl();
		   
	}
	
	//When buffer has 1 node
	@Test
	public void testGetModuleContent() {
		//Creates node and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns.addDefaultNode(n1);
		
		buffer.addBufferContent((WorkspaceContent) ns);

		// Execution of getModuleContent() method
		NodeStructure ns2 = (NodeStructure) buffer.getModuleContent();
		
		assertTrue((NodeStructureImpl.compareNodeStructures(ns, ns2)));
	}
	
	//When buffer has 2 nodes
	@Test
	public void testGetModuleContent2() {
		//Creates node and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns.addDefaultNode(n1);
		
		buffer.addBufferContent((WorkspaceContent) ns);
		
		Node n2 = new NodeImpl();
		n2.setId(5);
		ns.addDefaultNode(n2);
		
		buffer.addBufferContent((WorkspaceContent) ns);
		
		// Execution of getModuleContent() method
		NodeStructure ns2 = (NodeStructure) buffer.getModuleContent();

		assertTrue(ns2.containsNode(2));
		assertTrue(ns2.containsNode(n2));
		assertEquals(2,ns2.getNodeCount());
	}

	//When buffer doesn't has node
	@Test
	public void testGetModuleContent3() {
		//Creates node and add them into a node structure
		// Execution of getModuleContent() method
		NodeStructure ns2 = (NodeStructure) buffer.getModuleContent();

		assertEquals(0, ns2.getNodeCount());
	}
	
	@Test
	public final void testInit() {
		//NA
	}

	@Test
	public final void testWorkspaceBufferImpl() {
		//NA
	}


	//Decay for 2 nodes and 1 be removed because of activation is lower than the threshold
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
		buffer.setModuleName(ModuleName.PerceptualBuffer);
		wMoudle.addSubModule(buffer);
		
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);

		NodeStructure nsNew = (NodeStructure) buffer.getModuleContent();
		double beforeDecay = nsNew.getNode(6).getActivation();
		buffer.decayModule(5);
		double afterDecay = nsNew.getNode(6).getActivation();
		NodeStructure ns2 = (NodeStructure) buffer.getModuleContent();

		// After node(Id == 2) is removed cause decay, so here is only node (Id == 6).
		assertTrue((ns2.containsNode(6))&&(!ns2.containsNode(2))&&(beforeDecay > afterDecay));
	}
	
	//Decay 1 node and it be removed
	@Test
	public final void testDecayModule2() {
		//Creates nodes and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		Node n1 = new NodeImpl();
		n1.setId(2);
		n1.setActivation(0.15);
		n1.setActivatibleRemovalThreshold(0.1);
		ns.addDefaultNode(n1);
		
		//Create workspaceBuffer and add them into mockWorkspace
		WorkspaceImpl wMoudle = new WorkspaceImpl();
		buffer.setModuleName(ModuleName.PerceptualBuffer);
		wMoudle.addSubModule(buffer);
		
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);

		buffer.decayModule(5);
		NodeStructure ns2 = (NodeStructure) buffer.getModuleContent();

		// After node(Id == 2) is removed cause decay
		assertTrue((ns2.getNodeCount() == 0)&&(!ns2.containsNode(2)));
	}
	
	//Decay 0 node and there should nothing happen
	@Test
	public final void testDecayModule3() {
		//Creates nodes and add them into a node structure
		NodeStructure ns = new NodeStructureImpl();
		
		//Create workspaceBuffer and add them into mockWorkspace
		WorkspaceImpl wMoudle = new WorkspaceImpl();
		buffer.setModuleName(ModuleName.PerceptualBuffer);
		wMoudle.addSubModule(buffer);
		
		// Add node structure into workspaceBuffer of percetualBuffer
		wMoudle.receivePercept(ns);

		// Nothing happened and this decay method be executed successfully.
		buffer.decayModule(5);

	}
	
	
	@Test
	public final void testAddListener() {
		//NA
	}

    /**
     * Test the functionality of the <code>addBufferContent</code> method.
     * The buffer is checked after creation, and after addition of two nodes.
     */
    @Test
	public final void testAddBufferContent() {
		NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
        Node n2 = new NodeImpl();
		n1.setId(2);
        n2.setId(4);
		ns.addDefaultNode(n1);
        ns.addDefaultNode(n2);

        NodeStructure content
                = (NodeStructure) buffer.getBufferContent(null);

        // PRE: the buffer is empty, 0 nodes and 0 links.
        assertEquals(0, content.getNodeCount());
        assertEquals(0, content.getLinkCount());

        // A node structure with two nodes is added to the buffer.
        buffer.addBufferContent((WorkspaceContent)ns);
		content = (NodeStructure) buffer.getBufferContent(null);
		// POS: the buffer has 2 nodes and 0 links.
        assertTrue(content.containsNode(2) && content.containsNode(4));
        assertEquals(2, content.getNodeCount());
        assertEquals(0, content.getLinkCount());
	}

	//When add 1 node to a buffer which already has 1 node
	@Test
	public final void testAddBufferContent2() {
		//Create a NodeStructure with NodeId = 2
		NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns.addDefaultNode(n1);

		buffer.addBufferContent((WorkspaceContent)ns);
		
		NodeStructure ns2 = new NodeStructureImpl();
		Node n2 = new NodeImpl();
		n2.setId(5);
		ns2.addDefaultNode(n2);
				
		// Add the NodeStructure to buffer where already has 1 node
		buffer.addBufferContent((WorkspaceContent)ns2);		
		
		// Check whether action of adding is successful
		// In the same time, getBufferContent() method be tested too
		assertTrue((((NodeStructure)buffer.getBufferContent(null)).containsNode(2))&&
				(((NodeStructure)buffer.getBufferContent(null)).containsNode(5))&&
				(((NodeStructure)buffer.getBufferContent(null)).getNodeCount() == 2));
	}
	
	//When add 0 node to a buffer, then no node be added to buffer
	@Test
	public final void testAddBufferContent3() {
		//Create a NodeStructure with NodeId = 2
		NodeStructure ns = new NodeStructureImpl();

		buffer.addBufferContent((WorkspaceContent)ns);
				
		// Add the NodeStructure to buffer
		buffer.addBufferContent((WorkspaceContent)ns);		
		
		// Check whether action of adding is successful
		// In the same time, getBufferContent() method be tested too
		assertTrue(((NodeStructure)buffer.getBufferContent(null)).getNodeCount() == 0);
	}
	
	@Test
	public final void testGetBufferContent() {
            //To be tested in testAddBufferContent function above together
	}

}
