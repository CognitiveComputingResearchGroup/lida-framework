/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.Map;

import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * @author Sean Kugele
 *
 */
public interface BehaviorFactory {
    
    /**
     * Returns a new Behavior based on specified {@link Scheme} of default
     * behavior type.
     * 
     * @param s
     *            a {@link Scheme}
     * @return a new {@link Behavior}
     */
    public Behavior getBehavior(Scheme s);

    /**
     * @param s
     * @param type
     * @return
     */
    public Behavior getBehavior(Scheme s, String type);
    

    /**
     * @param s
     * @param type
     * @param params
     * @return
     */
    public Behavior getBehavior(Scheme s, String type,
            Map<String, ? extends Object> params);
}
