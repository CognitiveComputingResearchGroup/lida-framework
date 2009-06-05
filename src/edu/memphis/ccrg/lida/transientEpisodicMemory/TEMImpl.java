/*
 * @(#)TEMImpl.java  1.0  May 1, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeImpl;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientEpisodicMemory.sdm.SparseDistributedMemory;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Rodrigo Silva L.
 */
public class TEMImpl implements TransientEpisodicMemory {

    private SparseDistributedMemory sdm;
    private HashMap<Long, Integer> indexMap;
    private HashMap<Integer, Long> nodeMap;

    /**
     * The constructor of the class.
     * @param pam a node structure containing the nodes of the PAM
     */
    public TEMImpl(NodeStructure pamNodes) {
        Collection<Node> nodes = pamNodes.getNodes();
        indexMap = new HashMap<Long, Integer>();
        nodeMap = new HashMap<Integer, Long>();
        int index = 0;
        for (Node n : nodes) {
            long nodeID = n.getId();
            indexMap.put(nodeID, index);
            nodeMap.put(index, nodeID);
            index++;
        }
    }

    /**
     * 
     * @param bc the content of the conscious broadcast
     */
    public void receiveBroadcast(BroadcastContent bc) {
        //TODO: logic for episodic learning goes here...
    }

    /**
     * Cues this episodic memory.
     * @param cue a set of nodes used to cue this episodic memory
     * @return the local association related to the cue
     */
    public LocalAssociation cue(MemoryCue cue) {
        Collection<Node> nodes = cue.getNodeStructure().getNodes();
        LocalAssociationImpl association = new LocalAssociationImpl();
        for (Node n : nodes) {
            byte[] address = new byte[nodeMap.size()];
            address[indexMap.get(n.getId())] = 1;
            byte[] out = sdm.retrieve(address);
            for (int j = 0; j != out.length; j++) {
                if (out[j] == 1) {
                    association.addNode((NodeImpl) cue.getNodeStructure()
                            .getNode(nodeMap.get(j)));
                }
            }
        }
        return association;
    }
}