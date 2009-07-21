/*
 * @(#)MemoryCueImpl.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 * This class is the cannonical implementation of the memory cue for tansient
 * episodic memory.
 * @author Rodrigo Silva L.
 */
public class MemoryCueImpl implements MemoryCue {

    private NodeStructureImpl structure;
    
    /**
     * Constructor of the class.
     */
    public MemoryCueImpl() {
        structure = new NodeStructureImpl();
    }
    
    /**
     * Constructs an object using an existing node structure.
     * @param structure the node structure used to create a new cue
     */
    public MemoryCueImpl(NodeStructureImpl structure) {
        this.structure = structure;
    }
    
    /**
     * gets the node structure associated with this cue.
     * @return the node structure representing this cue
     */
    public NodeStructure getNodeStructure() {
        return structure;
    }
}
