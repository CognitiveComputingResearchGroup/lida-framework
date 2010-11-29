/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.dao.Saveable;

public interface SparseDistributedMemory extends Saveable {

	/**
	 * Stores a word in the given address in this sparse distributed memory.
	 * 
	 * @param wrd
	 *            the word to be stored
	 * @param addr
	 *            the address where the word is to be stored
	 */
	public abstract void store(BitVector wrd, BitVector addr);

	/**
	 * Stores a word in this sparse distributed memory using the word as address.
	 * 
	 * @param wrd
	 *            the word to be stored
	 */
	public abstract void store(BitVector wrd);

	/**
	 * Stores a word in this sparse distributed memory using the word as address.
	 * The word is mapped (xor) with the mapping address.
	 * 
	 * @param wrd
	 *            the word to be stored.
	 * @param mapping
	 *            the mapping address.
	 */
	public abstract void mappedStore(BitVector wrd, BitVector mapping);

	/**
	 * Stores a word in this sparse distributed memory using the word as address.
	 * The word is mapped (xor) with the mapping address.
	 * 
	 * @param wrd
	 *            the word to be stored.
	 * @param addr
	 *            the address.
	 * @param mapping
	 *            the mapping address.
	 */
	public abstract void mappedStore(BitVector wrd,BitVector addr, BitVector mapping);

	/**
	 * Retrieves the contents of this sparse distributed memory at the given
	 * address.
	 * 
	 * @param addr
	 *            the address of the contents to be retrieved
	 * @return the contents of this sparse distributed memory associated with
	 *         the given address
	 */
	public abstract BitVector retrieve(BitVector addr);

	/**
	 * Retrieves the contents of this sparse distributed memory at the given
	 * address. Iterates 
	 * 
	 * @param addr
	 *            the address of the contents to be retrieved
	 * @return the contents of this sparse distributed memory associated with
	 *         the given address
	 */
	public abstract BitVector retrieveIterating(BitVector addr);

	public abstract BitVector retrieveIterating(BitVector addr,
			BitVector mapping);

}