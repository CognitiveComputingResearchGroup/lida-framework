/*
 * @(#)ProceduralMemory.java  1.0  February 14, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.proceduralMemory;

/**
 * The interface for LIDA's procedural memory. Procedural memory in LIDA
 * receives a conscious broadcast from the global workspace, and sends behavior
 * schemes to the action selection module. procedural memory also receives
 * the cosncious broadcast from the global workspace.
 * 
 * <p>Specific implementations of procedural memories must implement this
 * interface. Every implementation of this interface must also implement
 * {@link edu.memphis.ccrg.lida.globalworkspace.BroadcastListener}.
 * @author Rodrigo Silva L.
 */
public interface ProceduralMemory {

    /**
     * 
     * @return
     */
    public Scheme getScheme();
    
    /**
     * 
     * @param listener
     */
    public void AddSchemeListener(SchemeListener listener);
}
