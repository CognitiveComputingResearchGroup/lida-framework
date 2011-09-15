/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import java.util.Collection;

/**
 * This class translates between node structures and bit vectors. The methods
 * used are the ones described by Kanerva in his 2009 hyperdimensional computing
 * paper.
 * @author Rodrigo Silva-Lugo
 */
public class HyperdimensionalTranslator implements Translator {

    /**
     * 
     * @param data
     * @return 
     */
    @Override
    public NodeStructure translate(BitVector data) {
        
        // TODO: implement this method.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param structure
     * @return 
     */
    @Override
    public BitVector translate(NodeStructure structure) {
        
        // TODO: verify that all nodes in structure are PamNodeImpl?
        
        // FIXME: add dictionary field, where extra bit vectors are kept.
        
        // Get nodes from NodeStructure.
        Collection<Node> nodes = structure.getNodes();
        
        // FIXME: set the array length to tem.wordlength
        int [] sum = new int[PamNodeImpl.DEFAULT_BITVECTOR_LENGTH];
        BitVector result = new BitVector(PamNodeImpl.DEFAULT_BITVECTOR_LENGTH);
        for (Node n : nodes) {
            PamNode p = n.getGroundingPamNode();
            if (!p.hasSdmId()) {
                p.setSdmId();
            }
            BitVectorUtils.sumVectors(sum, p.getSdmId());
        }
        result = BitVectorUtils.normalizeVector(sum);
        
        // TODO: Map the sum using multiplication. Initially zero vector?
        return result;
    }
    
}
