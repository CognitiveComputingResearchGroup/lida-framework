/*
 * @(#)SchemeListener.java  1.0  February 14, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.ModuleListener;

/**
 *
 * @author Rodrigo Silva L.
 */
public interface ProceduralMemoryListener extends ModuleListener{

    /**
     * @param scheme
     */
    public void receiveBehavior(Behavior b);
   
}
