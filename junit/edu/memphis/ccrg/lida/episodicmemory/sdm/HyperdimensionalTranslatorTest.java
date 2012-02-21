/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rodrigo Silva-Lugo
 */
public class HyperdimensionalTranslatorTest {

    private HyperdimensionalTranslator tr;
    private static int SIZE = 100;
    private PerceptualAssociativeMemory pam;
    private NodeStructure ns;
    private BitVector vector;
    private Object factory;

    /**
     * 
     */
    public HyperdimensionalTranslatorTest() {
    }

    /**
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * 
     */
    @Before
    public void setUp() {
        pam = new MockPAM();
        tr = new HyperdimensionalTranslator(SIZE, pam);
        ns = new NodeStructureImpl();
        Node n = new NodeImpl();
        n.setId(10);
	//PamNode pn = (PamNode) factory.getNode("PamNodeImpl");

        pam.addDefaultNode(n);
        ns.addDefaultNode(n);
        ns.addNode(n, null);
        n = new NodeImpl();
        n.setId(20);
        ns.addDefaultNode(n);
        vector = new BitVector(SIZE);
        vector.set(10);
        vector.set(20);
    }

    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of translate method, of class HyperdimensionalTranslator.
     */
    @Test
    public void testTranslate_BitVector() {
//        System.out.println("translate");
//        BitVector data = null;
//        tr = new HyperdimensionalTranslator(SIZE, pam);
//        System.out.println(tr.getSize());
//        NodeStructure expResult = null;
//        NodeStructure result = instance.translate(data);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of translate method, of class HyperdimensionalTranslator.
     */
    @Test
    public void testTranslate_NodeStructure() {
//        System.out.println("translate");
//        NodeStructure structure = null;
//        HyperdimensionalTranslator instance = new HyperdimensionalTranslator();
//        BitVector expResult = null;
//        BitVector result = tr.translate(ns);
//        assertEquals(vector, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
