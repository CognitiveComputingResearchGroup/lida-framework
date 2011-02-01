/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import cern.colt.bitvector.BitVector;

public interface HardLocation {

	/**
	 * @return the address of this HardLocation
	 */
	public BitVector getAddress();

	/**
	 * Sets the address of this HardLocation in the vector Space
	 * @param address the address to set
	 */
	public void setAddress(BitVector address);

	/**
	 * Returns the counters of this HardLocation 
	 * Each HardLocation has wordSize counters. Each counter is byte size.
	 * Couters are incremented or decremented when the memory is written.
	 * @return the counters of this HardLocation 
	 */
	public byte[] getCounters();

	/**
	 * Sets the counters of this HardLocation 
	 * Each HardLocation has wordSize counters. Each counter is byte size.
	 * Counters are incremented or decremented when the memory is written.
	 * @param newCounters new counters
	 */
	public void setCounters(byte[] newCounters);

	/**
	 * @return the number of times that this HardLocation was written.
	 */
	public int getWriteCount();

	/**
	 * Writes BitVector word to this HardLocation.
	 * For each bit in word, the corresponding counter is incremented if the bit is 1 or decremented if
	 * the bit is 0.
	 *  
	 * @param word word to be written
	 */
	public void write(BitVector word);

	/**
	 * This method reads this HardLocation and sums the reading vector to int[] buffer.
	 * @param buffer buffer to be added to this hardlocation 
	 * @return summed vector
	 */
	public int[] read(int[] buffer);
	
	/**
	 * Returns the Hamming distance between vector and the address of this HardLocation
	 * @param vector vector to compare with this hardlocation
	 * @return the Hamming distance.
	 */
	public int hammingDistance(BitVector vector);

}