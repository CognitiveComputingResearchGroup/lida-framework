/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

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
