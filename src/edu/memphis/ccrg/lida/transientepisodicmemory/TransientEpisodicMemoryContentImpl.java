/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

/**
 *
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TransientEpisodicMemoryContentImpl implements TransientEpisodicMemoryContent {

    private NodeStructure structure;
    
    /**
     * 
     */
    public TransientEpisodicMemoryContentImpl() {
        structure = new NodeStructureImpl();
    }
    
    /**
     * 
     */
    public NodeStructure getContent() {
        return structure;
    }

}
