/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.episodicmemory.sdm;

import java.util.Collection;
import java.util.HashMap;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

/**
 * This is the class that translates from nodes to boolean vectors and vice-
 * versa. The translation works by assigning a unique index to every node. For
 * example, if there are four nodes in total, namely n1, n2, n3 and n4, the
 * boolean vector will have four positions. If the structure to be translated
 * has nodes n1 and n2, the output vector will be 1100. If the input vector to be
 * translated is 0101, the output structure will have two nodes i.e. n2 and n4.
 * In this case the dictionaries will be:
 * indexToNodeDictionary = {(0,n1),(1,n2),(2,n3),(3,n4)}
 * nodeToIndexDictionary = {(n1,0),(n2,1),(n3,2),(n4,3)}
 * Both dictionaries are always the same size, which corresponds to the total
 * number of nodes used to create the translator.
 * @author Rodrigo Silva-Lugo <rsilval@acm.org>
 */
public class TranslatorImpl {

    /**
     * The dictionary used to translate from nodes to vectors. Each node, has
     * a unique corresponding index in the vector
     */
    private HashMap<Node, Integer> nodeToIndexDictionary;
    
    /**
     * The dictionary used to translate from vextors to indices. Each position
     * in the vector corresponds to a unique node.
     */
    private HashMap<Integer, Node> indexToNodeDictionary;

    /** The boolean vector used for the translation. */
    private byte[] vector;

    /** The node structure used for the translation. */
    private NodeStructure nodeStructure;

    /** The nodes in the node structure, this collection is used in the for
     * loops since it has an enumerator. */
    private Collection<Node> nodes;

    /**
     * The constructor for the class. the dictionaries are created and populated.
     * The nodes from the input structure are stored, and the boolean vector is
     * initialized.
     * @param nodeStructure the node structure that contains all the nodes used
     * for translation
     */
    public TranslatorImpl(NodeStructure nodeStructure) {
        nodeToIndexDictionary = new HashMap<Node, Integer>();
        indexToNodeDictionary = new HashMap<Integer, Node>();
        this.nodeStructure = nodeStructure;
        nodes = nodeStructure.getNodes();
        int index = 0;
        for (Node n : nodes) {
            nodeToIndexDictionary.put(n, index);
            indexToNodeDictionary.put(index, n);
            index++;
        }
        vector = new byte[nodeToIndexDictionary.size()];
    }

    /**
     * Translates a boolean vector into a node structure.
     * @param data the boolean vector to be translated, must be the same size as
     * the dictionaries
     * @return the node structure with the nodes corresponding to the input
     * vector
     * @throws Exception when the input vector is not the same size as the
     * dictionaries
     */
    public NodeStructure translate(byte[] data) throws Exception {
        NodeStructureImpl ns = new NodeStructureImpl();
        if (data.length == vector.length) {
            vector = data;
        } else {
            throw new Exception("The data vector size is incorrect.");
        }
        for (int i = 0; i != vector.length; i++) {
            if (vector[i] != 0) {
                ns.addNode(nodeStructure.getNode(indexToNodeDictionary
                        .get(i).getId()));
            }
        }
        return ns;
    }

    /**
     * Translates a node structure into a boolean vector.
     * @param structure the node structure to be translated, must have at most
     * as many nodes as any of the dictionaries
     * @return a boolean vector representing the node structure
     * @throws Exception when the input node structure size is larger than the
     * dictionaries, or a node in the structure is not in the dictionary
     */
    public byte[] translate(NodeStructure structure) throws Exception {
        byte[] v = new byte[vector.length];
        if (structure.getNodes().size() <= nodes.size()) {
            for (Node n : structure.getNodes()) {
                Integer i = nodeToIndexDictionary.get(n);
                if (!i.equals(null)) {
                    v[nodeToIndexDictionary.get(n)] = 1;
                } else {
                    throw new Exception("The node " + n.toString()
                            + " is not in the dictionary");
                }
            }
            return v;
        } else {
            throw new Exception("The structure size is incorrect, should be "
                    + "less or equal than " + nodes.size());
        }
    }
}