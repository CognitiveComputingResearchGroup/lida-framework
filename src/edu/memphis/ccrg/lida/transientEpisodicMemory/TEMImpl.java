/*
 * @(#)TEMImpl.java  1.0  May 1, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class TEMImpl implements TransientEpisodicMemory{

    /**
     * 
     * @param cue
     */
	public void receiveCue(WorkspaceContent cue) {
		// TODO Auto-generated method stub
    }
    
    /**
     * 
     * @param cue
     * @return
     */
    public LocalAssociation cue(MemoryCue cue){
        return new LocalAssociationImpl();
    }

}
