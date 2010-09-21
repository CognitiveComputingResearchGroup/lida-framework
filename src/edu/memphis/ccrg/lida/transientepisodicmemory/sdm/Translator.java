/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * Interface for the translator between boolean vectors in sparse distributed
 * memory, and node structures in the LIDA cognitive architecture.
 * @author Rodrigo Silva-Lugo <rsilval@acm.org>
 */
public interface Translator {

	/**
	 * Translates a boolean vector into a node structure.
	 * @param data a byte vector with the boolean vector to be translated
	 * @return the node structure associated with the address
         * @throws Excaeption when the boolean vector is the wrong size
	 */
	public abstract NodeStructure translate(BitVector data) throws Exception;

	/**
	 * Translates a node structure into a boolean vector.
	 * @param structure the node structure to be translated
	 * @return a byte vector with the boolean address associated with
         * the structure
         * @throws Exception when the node structure is the wrong size
	 */
	public abstract BitVector translate(NodeStructure structure)
                throws Exception;

}