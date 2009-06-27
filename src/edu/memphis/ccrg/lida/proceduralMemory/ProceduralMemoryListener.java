/*
 * @(#)SchemeListener.java  1.0  February 14, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.List;

/**
 *
 * @author Rodrigo Silva L.
 */
public interface ProceduralMemoryListener {

    /**
     * 
     * @param scheme
     */
    public void receiveSchemes(List<Scheme> schemes);
}
