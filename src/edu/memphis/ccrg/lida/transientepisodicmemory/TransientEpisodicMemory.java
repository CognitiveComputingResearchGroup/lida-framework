/*
 * TransientEpisodicMemory.java
 *
 * Part of the Lida Framework, a computational implementation
 * of the Lida Model of cognition.
 * 
 * Copyright 2006-2010 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * http://ccrg.cs.memphis.edu/
 * 
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.LidaModule;


/**
 * The interface for LIDA's episodic memory. Episodic memory in LIDA
 * communicates with the workspace, receiving memory cues, and returning local
 * associations. 
 * <p>
 * Specific implementations of episodic memories must implement
 * this interface. Every implementation of this interface must also implement
 * {@link edu.memphis.ccrg.lida.globalworkspace.BroadcastListener}.
 * @see MemoryCue
 * @see LocalAssociation
 * @author Rodrigo Silva L.
 */
public interface TransientEpisodicMemory extends LidaModule{
    
    /**
     * Cues this episodic memory. Typically the cue would be a set of nodes, and
     * in the future links.
     * @param cue the cue used to read this TEM
     */
    public void cue(MemoryCue cue);
}