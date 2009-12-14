/*
 * @(#)TransientEpisodicMemory.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
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
 * @also MemoryCue
 * @also LocalAssociation
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public interface TransientEpisodicMemory extends LidaModule{
    
    /**
     * Cues this episodic memory. Typically the cue would be a set of nodes, and
     * in the future links.
     * @param cue the cue used to read this TEM
     * @return a Future object with the local association
     */
    public void cue(MemoryCue cue);
}