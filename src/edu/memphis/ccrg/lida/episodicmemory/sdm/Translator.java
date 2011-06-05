/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.episodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * Interface for the translator between boolean vectors in sparse distributed
 * memory, and node structures in the LIDA cognitive architecture.
 * @author Javier Snaider
 */
public interface Translator {

	/**
	 * Translates a boolean vector into a node structure.
	 * @param data a byte vector with the boolean vector to be translated
	 * @return the node structure associated with the address
 	 */
	public NodeStructure translate(BitVector data);

	/**
	 * Translates a node structure into a boolean vector.
	 * @param structure the node structure to be translated
	 * @return a byte vector with the boolean address associated with
     * the structure
	 */
	public BitVector translate(NodeStructure structure);

}