/*
 * @(#)TEMImpl.java  1.0  May 1, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;

/**
 *
 * @author Rodrigo Silva L.
 */
public class TEMImpl implements TransientEpisodicMemory{

    /**
     * 
     * @param bc
     */
    public void receiveBroadcast(BroadcastContent bc) {
        //TODO: logic for episodic learning goes here...
    }

    /**
     * 
     * @param cue
     * @return
     */
    public LocalAssociation cue(MemoryCue cue){
        //TODO: logic for cueing SDM goes here...
        return new LocalAssociationImpl();
    }

}
