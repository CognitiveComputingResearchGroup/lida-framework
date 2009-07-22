/*
 * @(#)LocalAssociation.java  1.0  April 24, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.shared.NodeStructure;

/**
 * This interface represents the local association for LIDA's transient episodic
 * memory.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public interface LocalAssociation {

    /**
     * Gets the node structure representing this association.
     * @return a node structure with the nodes and links associated with this
     * association.
     */
    public NodeStructure getNodeStructure();
}