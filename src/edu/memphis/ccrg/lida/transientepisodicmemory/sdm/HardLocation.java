package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;

public interface HardLocation {

	/**
	 * @return the address
	 */
	public abstract BitVector getAddress();

	/**
	 * @param address the address to set
	 */
	public abstract void setAddress(BitVector address);

	/**
	 * @return the counters
	 */
	public abstract byte[] getCounters();

	public abstract void setCounters(byte[] newCounters);

	/**
	 * @return the writes
	 */
	public abstract int getWrites();

	public abstract void write(BitVector word);

	public abstract int[] read(int[] buff);
	
	public int hamming(BitVector vector);

}