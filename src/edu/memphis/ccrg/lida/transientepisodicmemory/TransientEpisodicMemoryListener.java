/*
 * @(#)MemoryCueListener.java  1.0  February 12, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.ModuleListener;

/**
 *
 * @author Rodrigo Silva L.
 */
public interface TransientEpisodicMemoryListener extends ModuleListener{

    /**
     * 
     * @param association 
     */
    public void receiveTEMContent(TransientEpisodicMemoryContent association);
}
