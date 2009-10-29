/*
 * @(#)TranslatorImpl.java  1.0  June 30, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

import java.util.Collection;
import java.util.HashMap;

/**
 * This is the class that translates from nodes to boolean vectors and vice
 * versa.
 * @author Rodrigo Silva L.
 */
public class TranslatorImpl implements Translator {

    private HashMap<Node, Integer> indexMap;
    private HashMap<Integer, Node> nodeMap;
    private byte[] vector;
    private NodeStructure nodeStructure;
    private Collection<Node> nodes;

    /**
     * The constructor for the class.
     * @param nodeStructure the node structure used for translation, this
     * structure contains the primitive feature detectors in PAM
     */
    public TranslatorImpl(NodeStructure nodeStructure) {
        indexMap = new HashMap<Node, Integer>();
        nodeMap = new HashMap<Integer, Node>();
        this.nodeStructure = nodeStructure;
        nodes = nodeStructure.getNodes();
        int index = 0;
        for (Node n : nodes) {
            indexMap.put(n, index);
            nodeMap.put(index, n);
            index++;
        }
        vector = new byte[indexMap.size()];
    }

    /* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.Translator#translate(byte[])
	 */
    public NodeStructure translate(byte[] data) {
        NodeStructureImpl ns = new NodeStructureImpl();
        for (int i = 0; i != vector.length; i++) {
            if (data[i] != 0) {
                ns.addNode(nodeStructure.getNode(nodeMap.get(i).getId()));
            }
        }
        return ns;
    }

    /* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.Translator#translate(edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
    public byte[] translate(NodeStructure structure) {
        byte[] v = new byte[vector.length];
        for (Node n : structure.getNodes()) {
            v[indexMap.get(n)] = 1;
        }
        return v;
    }
}