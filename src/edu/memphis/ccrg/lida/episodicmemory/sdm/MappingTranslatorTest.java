/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.episodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * This is a test for the MappingTranslator class.
 * @author Rodrigo Silva-Lugo
 */
public class MappingTranslatorTest {

    /**
     * The main method of the class.
     * @param args command line arguments, not used
     */
    public static void main(String[] args) {

        // the size of the bit vectors
        final int NUM_NODES = 5;
        PerceptualAssociativeMemoryImpl pam = new PerceptualAssociativeMemoryImpl();
        NodeStructureImpl ns = new NodeStructureImpl();
        BitVector v = new BitVector(NUM_NODES);
        for (int i = 0; i < NUM_NODES; i++) {
            NodeImpl n = new NodeImpl();
            n.setId(i);
            ns.addNode(n);
        }
        MappingTranslator t = new MappingTranslator(NUM_NODES, pam);
        try {
            v = t.translate(ns);
            v.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
