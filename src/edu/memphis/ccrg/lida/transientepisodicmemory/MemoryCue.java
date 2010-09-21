/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * This interface represents the memory cue for LIDA's transient episodic memory.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public interface MemoryCue {

    /**
     * Gets the node structure representing this cue.
     * @return a node structure with the nodes and links associated with this cue.
     */
    public NodeStructure getNodeStructure();

}
