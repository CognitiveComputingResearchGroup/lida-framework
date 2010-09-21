/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

/**
 * Test for the TranslatorImpl class.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 * @see TranslatorImpl
 */
public class TranslatorTest {
//
//    /** Number of nodes in the node structure to be translated, this is also the
//     * number of positions in the boolean vector.
//     */
//    private static final int NUM_NODES = 20;
//
//    /** The number of tests to be performed. */
//    private static final int NUM_TESTS = 50;
//
//    /**
//     * The main method of the class. This test takes a randomly generated input
//     * boolean vector, translates it to a node structure, and then translates
//     * this structure again to a boolean vector. The resulting vector should be
//     * identical to the original input vector.
//     * @param args command line arguments, not used
//     */
//    public static void main(String[] args) {
//        NodeStructure allNodes = new NodeStructureImpl();
//        NodeStructure outputNodes;
//        byte[] inputVector = new byte[NUM_NODES];
//        byte[] outputVector;
//        for (int i = 0; i != NUM_NODES; i++) {
//            NodeImpl node = new NodeImpl();
//            node.setId(i);
//            allNodes.addNode(node);
//        }
//        for (int j = 1; j <= NUM_TESTS; j++) {
//            for (int i = 0; i != NUM_NODES; i++) {
//                inputVector[i] = (byte) Math.round(Math.random());
//            }
//            Translator translator = new TranslatorImpl(allNodes);
//            try {
//                outputNodes = translator.translate(inputVector);
//                outputVector = translator.translate(outputNodes);
//                System.out.println("Test " + j);
//                System.out.print("Input vector:  ");
//                for (int i = 0; i != NUM_NODES; i++) {
//                    System.out.print(inputVector[i] + ",");
//                }
//                System.out.println();
//                System.out.print("Output vector: ");
//                for (int i = 0; i != NUM_NODES; i++) {
//                    if (outputVector[i] == inputVector[i]) {
//                        System.out.print(outputVector[i] + ",");
//                    } else {
//                        System.out.println("ERROR!");
//                        return;
//                    }
//                }
//                System.out.println();
//                System.out.print("Output nodes:  ");
//                for (Node n : outputNodes.getNodes()) {
//                    System.out.print(n.getId() + ",");
//                }
//                System.out.println();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Tests finished successfully!");
//    }
}
