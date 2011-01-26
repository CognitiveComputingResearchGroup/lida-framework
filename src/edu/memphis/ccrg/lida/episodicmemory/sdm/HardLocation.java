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
	 * @return the address
	 */
	public BitVector getAddress();

	/**
	 * @param address the address to set
	 */
	public void setAddress(BitVector address);

	/**
	 * @return the counters
	 */
	public byte[] getCounters();

	public void setCounters(byte[] newCounters);

	/**
	 * @return the writes
	 */
	public int getWrites();

	public void write(BitVector word);

	public int[] read(int[] buff);
	
	public int hamming(BitVector vector);

}