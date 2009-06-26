/*
 * @(#)MemoryCueImpl.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 *
 * @author Rodrigo Silva L.
 */
public class MemoryCueImpl implements MemoryCue {

    private NodeStructureImpl structure;
    
    /**
     * 
     */
    public MemoryCueImpl() {
        structure = new NodeStructureImpl();
    }
    
    /**
     * 
     * @param structure
     */
    public MemoryCueImpl(NodeStructureImpl structure) {
        this.structure = structure;
    }
    
    /**
     *
     * @return
     */
    public NodeStructure getNodeStructure() {
        return structure;
    }
}
