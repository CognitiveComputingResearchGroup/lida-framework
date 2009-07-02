/*
 * @(#)MemoryCue.java  1.0  April 24, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.shared.NodeStructure;

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
