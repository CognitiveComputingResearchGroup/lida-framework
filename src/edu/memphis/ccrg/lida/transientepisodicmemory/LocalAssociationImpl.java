/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

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
     */
    public NodeStructure getNodeStructure() {
        return nodeStructure;
    }
}
