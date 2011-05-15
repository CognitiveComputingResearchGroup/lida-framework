/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * This class is the JUnit test for <code>WorkspaceImpl</code> class.
 * @author Rodrigo Silva-Lugo
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
        System.out.println("addListener");
        WorkspaceImpl instance = new WorkspaceImpl();
        
    	// Type of listener is neither WorkspaceListener nor CueListener 
        ModuleListener listener = null;
        instance.addListener(listener);
        
        // Type of listener is WorkspaceListener (PerceptualAssociativeMemoryImpl)
        // TODO: This test case should pass without warning, but warning happened.
        WorkspaceListener wListener = new PerceptualAssociativeMemoryImpl();
        instance.addListener(wListener);
        
        // Type of listener is WorkspaceListener (mockWorkListenerImpl)
        WorkspaceListener wListener2 = new mockWorkListenerImpl();
        instance.addListener(wListener2);
        
        // Type of listener is CueListener (mockCueListenerImpl)
        // TODO: BUG? Compiling error happened. It because that CueListener doesn't extend ModuleListener.
        CueListener cListener = new mockCueListenerImpl();
        instance.addListener(cListener);
        
    }

    /**
     * Test of addCueListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddCueListener() {
        // TODO review test
        System.out.println("addCueListener");
        CueListener l = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.addCueListener(l);
    }

    /**
     * Test of addWorkspaceListener method, of class WorkspaceImpl.
     */
    @Test
    public void testAddWorkspaceListener() {
        // TODO review test
        System.out.println("addWorkspaceListener");
        WorkspaceListener listener = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.addWorkspaceListener(listener);
    }

    /**
     * Test of cueEpisodicMemories method, of class WorkspaceImpl.
     */
    @Test
    public void testCueEpisodicMemories() {
        // TODO review test
        System.out.println("cueEpisodicMemories");
        NodeStructure content = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.cueEpisodicMemories(content);
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
        NodeStructure association = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.receiveLocalAssociation(association);
    }

    /**
     * Test of receivePercept method, of class WorkspaceImpl.
     */
    @Test
    public void testReceivePercept() {
        // TODO review test
        System.out.println("receivePercept");
        NodeStructure newPercept = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.receivePercept(newPercept);
    }

    /**
     * Test of getModuleContent method, of class WorkspaceImpl.
     */
    @Test
    public void testGetModuleContent() {
        // TODO review test
        System.out.println("getModuleContent");
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
        System.out.println("learn");
        BroadcastContent content = null;
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.learn(content);
    }

    /**
     * Test of init method, of class WorkspaceImpl.
     */
    @Test
    public void testInit() {
        // TODO review test
        System.out.println("init");
        WorkspaceImpl instance = new WorkspaceImpl();
        instance.init();
    }
}

class mockWorkListenerImpl implements WorkspaceListener {

	@Override
	public void receiveWorkspaceContent(ModuleName originatingBuffer,
			WorkspaceContent content) {
		
	}
	
}

class mockCueListenerImpl implements CueListener {

	@Override
	public void receiveCue(NodeStructure cue) {

	}


	
}