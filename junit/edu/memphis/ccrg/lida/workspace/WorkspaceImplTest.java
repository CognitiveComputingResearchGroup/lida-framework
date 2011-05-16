/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This class is the JUnit test for <code>WorkspaceImpl</code> class.
 * @author Rodrigo Silva-Lugo, Daqi Dong
 */
public class WorkspaceImplTest {
	
	private static final ElementFactory factory = ElementFactory.getInstance();

    /**
     *
     */
    public WorkspaceImplTest() {
    }

    /**
     *
     * @throws Exception e
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }


    /**
     * Test of addListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddListener() {
        // TODO review test
        WorkspaceImpl instance = new WorkspaceImpl();
        
    	// Type of listener is neither WorkspaceListener nor CueListener 
        // Warning should appear
        ModuleListener listener = new mockModuleListenerImpl();
        instance.addListener(listener);
        
        // Type of listener is WorkspaceListener (mockWorkListenerImpl)
        WorkspaceListener wListener2 = new mockWorkListenerImpl();
        instance.addListener(wListener2);
        
        // Type of listener is CueListener (mockCueListenerImpl)
        CueListener cListener = new mockCueListenerImpl();
        instance.addListener(cListener);
        
    }

    /**
     * Test of addCueListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddCueListener() {
        // TODO review test
        CueListener l = new mockCueListenerImpl();
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.addCueListener(l);
    }

    /**
     * Test of addWorkspaceListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddWorkspaceListener() {
        // TODO review test
        WorkspaceListener listener = new mockWorkListenerImpl();;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.addWorkspaceListener(listener);
    }

    /**
     * Test of cueEpisodicMemories method, of class WorkspaceImpl.
     */
    @Test
    public void testCueEpisodicMemories() {
        // TODO review test
        WorkspaceImpl instance = new WorkspaceImpl();
        
        // Add a CueListener to list
        CueListener l = new mockCueListenerImpl();
        instance.addCueListener(l);
        
        // Set a NodeStructure (with nodeId = 2) to list of cueListener
        NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns.addDefaultNode(n1);
		instance.cueEpisodicMemories(ns);
    }

    /**
     * Test of receiveBroadcast method, of class WorkspaceImpl.
     */
    @Test
    public void testReceiveBroadcast() {
        BroadcastContent bc = null;
        WorkspaceImpl workspace = new WorkspaceImpl();
        
        MockQueue bq = new MockQueue();
        bq.setModuleName(ModuleName.BroadcastQueue);
        workspace.addSubModule(bq);
        
        workspace.receiveBroadcast(bc);
        assertEquals(bc, bq.checkContent());
        
        NodeStructure foo = new NodeStructureImpl();
        foo.addDefaultNode(factory.getNode());
        
        workspace.receiveBroadcast((BroadcastContent) foo);
        assertEquals(foo, bq.checkContent());
    }

    /**
     * Test of receiveLocalAssociation method, of class WorkspaceImpl.
     */
    @Test
    public void testReceiveLocalAssociation() {
        // TODO review test
    	// Create a sub module of EpisodicBuffer
        WorkspaceImpl instance = new WorkspaceImpl();
        WorkspaceBufferImpl buffer = new WorkspaceBufferImpl();
        buffer.setModuleName(ModuleName.EpisodicBuffer);
        instance.addSubModule(buffer);
    	    	
        // Add a WorkspaceListener to list
        WorkspaceListener w = new mockWorkListenerImpl();
        instance.addWorkspaceListener(w);
        
        // Set a NodeStructure (with nodeId = 5) to list of WorkspaceListener
        NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(5);
		ns.addDefaultNode(n1);
        instance.receiveLocalAssociation(ns);
    }

    /**
     * Test of receivePercept method, of class WorkspaceImpl.
     */
    @Test
    public void testReceivePercept() {
        // TODO review test
    	// Create a sub module of PerceptualBuffer
        WorkspaceImpl instance = new WorkspaceImpl();
        WorkspaceBuffer buffer = new WorkspaceBufferImpl();
        buffer.setModuleName(ModuleName.PerceptualBuffer);
        instance.addSubModule(buffer);
    	
        // Set a NodeStructure (with nodeId = 9) to PerceptualBuffer
        NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(9);
		ns.addDefaultNode(n1);
        instance.receivePercept(ns);
        
        //Check the action above has been done successfully
		buffer = (WorkspaceBuffer) instance.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns2 = (NodeStructure)buffer.getBufferContent(null);
	    assertTrue("Problem with class WorkspaceImpl for receivePercept()",
				ns2.containsNode(9));
				
    }

    /**
     * Test of getModuleContent method, of class WorkspaceImpl.
     */
    @Test
    public void testGetModuleContent() {
        // TODO review test
        Object[] params = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        Object expResult = null;
        Object result = instance.getModuleContent(params);
        assertEquals(expResult, result);
    }

    /**
     * Test of learn method, of class WorkspaceImpl.
     */
    @Test
    public void testLearn() {
        // TODO review test
        //N/A because of "Not applicable"
    }

    /**
     * Test of init method, of class WorkspaceImpl.
     */
    @Test
    public void testInit() {
        // TODO review test
        //N/A because of "Not applicable"
    }
}

class mockWorkListenerImpl implements WorkspaceListener {

	@Override
	public void receiveWorkspaceContent(ModuleName originatingBuffer,
			WorkspaceContent content) {
		//Check for receiving a NodeStructure which has a node with Id = 5
		assertTrue("Problem with class WorkspaceImpl for receiveLocalAssociation()",
				content.containsNode(5));
		
	}
	
}

class mockCueListenerImpl implements CueListener {

	@Override
	public void receiveCue(NodeStructure cue) {
		//Check for receiving a NodeStructure which has a node with Id = 2
		assertTrue("Problem with class WorkspaceImpl for cueEpisodicMemories()",
				cue.containsNode(2));
	}
	
}

class mockModuleListenerImpl implements ModuleListener{
	
}