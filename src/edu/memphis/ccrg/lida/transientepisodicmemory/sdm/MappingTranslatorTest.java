/*
 * @(#)MappingTranslatorTest.java  1.0  February 23, 2010
 *
 * Copyright 2006-2010 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
