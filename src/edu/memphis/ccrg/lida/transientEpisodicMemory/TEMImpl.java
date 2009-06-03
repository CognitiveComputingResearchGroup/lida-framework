/*
 * @(#)TEMImpl.java  1.0  May 1, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 *
 * @author Rodrigo Silva L.
 */
public class TEMImpl implements TransientEpisodicMemory, CueListener{

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

	public void receiveCue(WorkspaceContent cue) {
		// TODO Auto-generated method stub
		
	}

}
