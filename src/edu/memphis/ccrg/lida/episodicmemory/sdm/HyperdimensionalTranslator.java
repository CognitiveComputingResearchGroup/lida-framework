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
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.Translatable;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Translates between {@link NodeStructure} objects and {@link BitVector} 
 * objects. The methods used are those described by Kanerva in his 2009
 * hyperdimensional computing paper.
 * @author Rodrigo Silva-Lugo
 */
public class HyperdimensionalTranslator extends BasicTranslator {
	//TODO Javadoc
//    private static final ElementFactory factory = ElementFactory.getInstance();
//    private int size;
//    private PerceptualAssociativeMemory pam;
        
    /** Vector used to map into the sets region of the space. */
    @SuppressWarnings("unused")
    private BitVector setVector;
    private SparseDistributedMemory sdm;
    private static final Logger LOG
            = Logger.getLogger(HyperdimensionalTranslator.class.getName());    
    private Map<BitVector, Translatable> bvLookUpMap
            = new ConcurrentHashMap<BitVector, Translatable>();
    
    /* TODO: generalize this implementation to translate between representations.
    Extract interface which encapsulates non-specific respresentations functionality.*/

    /**
     * Constructor of the class.
     * 
     * @param size the number of positions in the bit vector
     * @param pam  the PAM associated with this translator
     */
    public HyperdimensionalTranslator(int size, PerceptualAssociativeMemory pam) {
        super(size, pam);
        setVector = BitVectorUtils.getRandomVector(size);
    }
    
    /**
     * TODO
     * @param size      the number of positions in the bit vector
     * @param pam       the PAM associated with this translator
     * @param setVector the vector used to map sets into different regions of
     *                  the space
     */
    public HyperdimensionalTranslator(int size, PerceptualAssociativeMemory pam,
            BitVector setVector) {
        super(size, pam);
        this.setVector = setVector;
    }

    /**
     * Translates a bit vector into a node structure.
     * @param data  the bit vector to be translated
     * @return      the resulting node structure
     */
    @Override
    public NodeStructure translate(BitVector data) {

        //BitVector dataVector = data;
        NodeStructure structure = new NodeStructureImpl();
        ArrayList<BitVector> vectors = new ArrayList<BitVector>();
        // 1. Apply inverse mapping
        BitVector unmappedSum = BitVectorUtils.multiplyVectors(data, setVector);
        // 2. Cue memory with unmapped sum
        BitVector probedVector = sdm.retrieve(unmappedSum);
        while (!probedVector.equals(unmappedSum)) {
            vectors.add(probedVector);
            int[] accum = new int[getSize()];
            BitVectorUtils.getVectorSum(accum, unmappedSum);
            BitVectorUtils.getVectorSum(
                    accum, BitVectorUtils.getComplement(probedVector));
            unmappedSum = BitVectorUtils.getNormalizedVector(accum);
            probedVector = sdm.retrieve(unmappedSum);
        }
        for (BitVector v : vectors) {
            structure.addDefaultNode((Node) bvLookUpMap.get(v));
        }
        // 3. Subtract returned vector from sum
        // 4. Cue memory with resulting vector
        // 5. GOTO 2 
        return structure;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Translates a node structure into a bit vector.
     * @param structure the node structure to be translated
     * @return          the resulting bit vector
     */
    @Override
    public BitVector translate(NodeStructure structure) {
        Collection<Node> nodes = structure.getNodes();
        int[] sum = new int[getSize()];
        BitVector result;
        for (Node n : nodes) {
            PamNode p = n.getGroundingPamNode();
            BitVector v = p.getSdmId();
            if (v == null) {
                v = BitVectorUtils.getRandomVector(getSize());
                p.setSdmId(v);
                bvLookUpMap.put(v, p);
            }
            BitVectorUtils.getVectorSum(sum, p.getSdmId());
        }
        result = BitVectorUtils.getNormalizedVector(sum);
        return result;
    }
}