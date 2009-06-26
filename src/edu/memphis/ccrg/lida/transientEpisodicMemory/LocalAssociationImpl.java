/*
 * @(#)LocalAssociationImpl.java  1.0  April 24, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.shared.NodeImpl;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 *
 * @author Rodrigo Silva L.
 */
public class LocalAssociationImpl implements LocalAssociation {

    private NodeStructureImpl nodeStructure;

    /**
     *
     */
    public LocalAssociationImpl() {
        nodeStructure = new NodeStructureImpl();
    }

    /**
     * 
     * @param structure
     */
    public LocalAssociationImpl(NodeStructureImpl structure) {
        nodeStructure = structure;
    }
    
    /**
     *
     * @param node
     */
    void addNode(NodeImpl node) {
        nodeStructure.addNode(node);
    }

    /**
     * 
     * @param link
     */
    void addLink(LinkImpl link) {
        nodeStructure.addLink(link);
    }

    /**
     * 
     * @return
     */
    public NodeStructure getNodeStructure() {
        return nodeStructure;
    }
}
