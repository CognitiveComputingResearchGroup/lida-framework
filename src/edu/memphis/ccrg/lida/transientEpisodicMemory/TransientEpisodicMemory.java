/*
 * @(#)TransientEpisodicMemory.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import java.util.concurrent.Future;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

/**
 * The interface for LIDA's episodic memory. Episodic memory in LIDA
 * communicates with the workspace, receiving memory cues, and returning local
 * associations. 
 * <p>
 * Specific implementations of episodic memories must implement
 * this interface. Every implementation of this interface must also implement
 * {@link edu.memphis.ccrg.lida.globalworkspace.BroadcastListener}.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public interface TransientEpisodicMemory extends BroadcastListener {
    
    /**
     * 
     * @param cue
     * @return
     */
    //public LocalAssociation cue(MemoryCue cue);
    public Future cue(MemoryCue cue);
}