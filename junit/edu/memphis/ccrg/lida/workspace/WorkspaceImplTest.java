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
    	MockWorkspaceImpl instance = new MockWorkspaceImpl();
        
    	// Type of listener is neither WorkspaceListener nor CueListener 
        // Warning should appear
        instance.workListenerFlag = false;
        instance.cueListenerFlag = false;
        ModuleListener listener = new mockModuleListenerImpl();
        instance.addListener(listener);
	    assertTrue("Problem with class WorkspaceImpl for addListener()",
				(instance.workListenerFlag == false) && (instance.cueListenerFlag == false));
        
        // Type of listener is WorkspaceListener (mockWorkListenerImpl)
        instance.workListenerFlag = false;
        WorkspaceListener wListener2 = new mockWorkListenerImpl();
        instance.addListener(wListener2);
	    assertTrue("Problem with class WorkspaceImpl for addListener()",
				instance.workListenerFlag == true);
        
        // Type of listener is CueListener (mockCueListenerImpl)
        instance.cueListenerFlag = false;
        CueListener cListener = new mockCueListenerImpl();
        instance.addListener(cListener);
	    assertTrue("Problem with class WorkspaceImpl for addListener()",
				instance.cueListenerFlag == true);
        
    }

    /**
     * Test of addCueListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddCueListener() {
        // TODO review test
        CueListener l = new mockCueListenerImpl();
        MockWorkspaceImpl instance = new MockWorkspaceImpl();
        instance.cueListenerFlag = false;
        instance.addCueListener(l);
	    assertTrue("Problem with class WorkspaceImpl for addCueListener()",
				instance.cueListenerFlag == true);
    }

    /**
     * Test of addWorkspaceListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddWorkspaceListener() {
        // TODO review test
        WorkspaceListener listener = new mockWorkListenerImpl();
        MockWorkspaceImpl instance = new MockWorkspaceImpl();
        instance.workListenerFlag = false;
        instance.addWorkspaceListener(listener);
	    assertTrue("Problem with class WorkspaceImpl for addWorkspaceListener()",
				instance.workListenerFlag == true);
    }

    /**
     * Test of cueEpisodicMemories method, of class WorkspaceImpl.
     */
    @Test
    public void testCueEpisodicMemories() {
        // TODO review test
        WorkspaceImpl instance = new WorkspaceImpl();
        
        // Add a CueListener to list
        mockCueListenerImpl l = new mockCueListenerImpl();
        l.ns = null;
        instance.addCueListener(l);
        
        // Set a NodeStructure (with nodeId = 2) to list of cueListener
        NodeStructure ns = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns.addDefaultNode(n1);
		instance.cueEpisodicMemories(ns);
		
		assertTrue("Problem with class WorkspaceImpl for CueEpisodicMemories()",
				l.ns.toString().equals(ns.toString()));
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
        
        NodeStructure ns1 = new NodeStructureImpl();
		Node n1 = new NodeImpl();
		n1.setId(2);
		ns1.addDefaultNode(n1);
		buffer.addBufferContent((WorkspaceContent) ns1);

        instance.addSubModule(buffer);
    	    	
        // Add a WorkspaceListener to list
        mockWorkListenerImpl w = new mockWorkListenerImpl();
        w.ns = null;
        instance.addWorkspaceListener(w);
        
        // Set a NodeStructure (with nodeId = 5) to list of WorkspaceListener
        NodeStructure ns2 = new NodeStructureImpl();
		Node n2 = new NodeImpl();
		n2.setId(5);
		ns2.addDefaultNode(n2);
        instance.receiveLocalAssociation(ns2);
        
        ns1.mergeWith(ns2);
        
		assertTrue("Problem with class WorkspaceImpl for receiveLocalAssociation()",
				w.ns.toString().equals(ns1.toString()) );
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
				ns.toString().equals(ns2.toString()));				
    }

    /**
     * Test of getModuleContent method, of class WorkspaceImpl.
     */
    @Test
    public void testGetModuleContent() {
        // TODO review test
        Object[] params = null;
        Object expResult = null;
        MockWorkspaceImpl instance = new MockWorkspaceImpl();
        instance.moduleContentflag = false;
        Object result = instance.getModuleContent(params);
	    assertTrue("Problem with class WorkspaceImpl for getModuleContent()",
				(instance.moduleContentflag == true) && ( result == expResult));	
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

	public NodeStructure ns = null;
	@Override
	public void receiveWorkspaceContent(ModuleName originatingBuffer,
			WorkspaceContent content) {
            this.ns = (NodeStructure)content;
		
	}
	
}

class mockCueListenerImpl implements CueListener {

	public NodeStructure ns = null;
	@Override
	public void receiveCue(NodeStructure cue) {
		this.ns = cue;
	}
	
}

class mockModuleListenerImpl implements ModuleListener{
	
}

class MockWorkspaceImpl extends WorkspaceImpl{
	public boolean workListenerFlag = false;
	public boolean cueListenerFlag = false;
	public boolean moduleContentflag = false;
	
	@Override
	public void addCueListener(CueListener l){
		cueListenerFlag =true;
	}
	@Override
	public void addWorkspaceListener(WorkspaceListener listener){
		workListenerFlag =true;
	}
	@Override
	public Object getModuleContent(Object... params) {
		moduleContentflag = true;
		return null;
	}
}