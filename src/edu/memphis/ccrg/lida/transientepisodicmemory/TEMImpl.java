/*
 * @(#)TEMImpl.java  1.0  May 1, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.Translator;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.FutureTask;

/**
 * This is the cannonical implementation of TEM. It uses a sparse distributed
 * memory to store the information.
 * @author Rodrigo Silva L.
 */
public class TEMImpl implements TransientEpisodicMemory, BroadcastListener, CueListener {

    private SparseDistributedMemory sdm;
    private HashMap<Long, Integer> indexMap;
    private HashMap<Integer, Long> nodeMap;
    @SuppressWarnings("unused")
	private Translator translator;

    /**
     * The constructor of the class.
     * @param structure the structure with the nodes used for this TEM
     */
    public TEMImpl(NodeStructure structure) {
        translator = new Translator(structure);
        indexMap = new HashMap<Long, Integer>();
        nodeMap = new HashMap<Integer, Long>();
        Collection<Node> nodes = structure.getNodes();
        int index = 0;
        for (Node n : nodes) {
            long nodeID = n.getId();
            indexMap.put(nodeID, index);
            nodeMap.put(index, nodeID);
            index++;
        }
    }

    public TEMImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Receives the conscious broadcast and store its information in this TEM.
     * @param bc the content of the conscious broadcast
     */
    public void receiveBroadcast(BroadcastContent bc) {
        //TODO: logic for episodic learning goes here...
    }

    /**
     * Cues this episodic memory.
     * @param cue a set of nodes used to cue this episodic memory
     * @return a future task with the local association related to the cue
     */
    public FutureTask<LocalAssociation> cue(MemoryCue cue) {
        Collection<Node> nodes = cue.getNodeStructure().getNodes();
        LocalAssociationImpl association = new LocalAssociationImpl();
        FutureTask<LocalAssociation> future = null;
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
        return future;
    }

	public void receiveCue(NodeStructure cue) {
		
	}

	public void learn() {

	}
}