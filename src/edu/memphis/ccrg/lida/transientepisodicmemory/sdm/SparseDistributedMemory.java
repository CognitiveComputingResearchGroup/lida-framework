package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;

public interface SparseDistributedMemory {

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