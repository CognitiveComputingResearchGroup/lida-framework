/*
 * @(#)TranslatorTest.java  1.0  June 30, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import edu.memphis.ccrg.lida.shared.NodeImpl;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 * Test for the Translator class.
 * @author Rodrigo Silva L.
 * @also Translator
 */
public class TranslatorTest {

    public static final int NUM_NODES = 5;

    /**
     * The main method of the class.
     * @param args command line arguments, not used
     */
    public static void main(String[] args) {
        NodeStructure structure = new NodeStructureImpl();
        NodeStructure resultStructure;
        byte[] vector = new byte[NUM_NODES];
        byte[] resultVector;
        for (int i = 0; i != NUM_NODES; i++) {
            NodeImpl node = new NodeImpl();
            node.setId(i);
            structure.addNode(node);
            vector[i] = 1;
        }
        Translator translator = new Translator(structure);
        resultStructure = translator.translate(vector);
        resultVector = translator.translate(structure);
        System.out.println(resultStructure.getNodeCount() + resultVector.length);
    }
}
