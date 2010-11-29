/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;


/**
 * This is the translator class based in the mapping concept described in
 * Kanerva's 2009 paper "Hyperdimensional Computing: An Introduction to
 * Computing in Distributed Representation with High-Dimensional Random Vectors"
 *
 * @author Rodrigo Silva-Lugo
 */
public class MappingTranslator implements Translator {

    private int size;
    private PerceptualAssociativeMemory pam;

    /**
     * @param size TODO
     */
    public MappingTranslator(int size, PerceptualAssociativeMemory pam) {
        this.size = size;
        this.pam = pam;
    }

    /**
     * @throws Exception UnsupportedOperationException
     */
    @Override
	public NodeStructure translate(BitVector data) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * Translates a node structure into a bit vector. Here the elements of
     * the node structure are encoded into a set using vector sum, then this set
     * is mapped using multiplication (XOR) with a random vector.
     * @param structure the node structure to be translated
     * @return a byte vector with the boolean address associated with
     * the structure
     * @throws Exception when the node structure is the wrong size
     */
    @Override
	public BitVector translate(NodeStructure structure) throws Exception {

        //throw new UnsupportedOperationException("Not supported yet.");
        int[] sum = new int[size];
        BitVector v = new BitVector(size);
        for (Node n : structure.getNodes()) {
            v = new BitVector(size);
            v.put(n.getId(), true);
            sum = BitVectorUtils.sumVectors(sum, v);
        }

        //TODO: add links in the node structure to the set. Will links need an ID?
        v = BitVectorUtils.normalizeVector(sum);
        return v;
    }
}
