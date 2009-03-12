/*
 * @(#)EpisodicMemory.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.episodic;

/**
 * The interface for LIDA's episodic memory. Episodic memory in LIDA
 * communicates with the workspace, receiving memory cues, and returning local
 * associations. 
 * <p>
 * Specific implementations of episodic memories must implement
 * this interface. Every implementation of this interface must also implement
 * {@link edu.memphis.ccrg.lida.globalworkspace.BroadcastListener}, and
 * {@link MemoryCueListener}.
 * @author Rodrigo Silva L.
 */
public interface EpisodicMemory {
    
    /**
     * 
     * @return
     */
    public MemoryAssociation getAssociation();
    
    /**
     * 
     * @param cue
     */
    public void cueMemory(MemoryCue cue);
    
    /**
     * 
     * @param listener
     */
    public void addMemoryAssociationListener(MemoryAssociationListener listener);

}