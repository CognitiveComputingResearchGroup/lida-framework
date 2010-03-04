/*
 * @(#)MappingTranslator.java  1.0  February 23, 2010
 *
 * Copyright 2006-2010 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

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
     *
     * @param size
     * @param pam
     */
    public MappingTranslator(int size, PerceptualAssociativeMemory pam) {
        this.size = size;
        this.pam = pam;
    }

    /**
     * 
     * @param data
     * @return
     * @throws Exception
     */
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
    public BitVector translate(NodeStructure structure) throws Exception {

        //throw new UnsupportedOperationException("Not supported yet.");
        int[] sum = new int[size];
        BitVector v = new BitVector(size);
        for (Node n : structure.getNodes()) {
            v = new BitVector(size);
            v.put((int)n.getId(), true);
            sum = BitVectorUtils.sumVectors(sum, v);
        }

        //TODO: add links in the node structure to the set. Will links need an ID?
        v = BitVectorUtils.normalizeVector(sum);
        return v;

    }

}
