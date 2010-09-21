/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;

public class HardLocationImpl implements HardLocation {
private static final byte DEFAULT_COUNTER_MAX = 40;
private BitVector address;
private int wordLength;
private byte[] counters;
private int writes;
private final byte counterMax = DEFAULT_COUNTER_MAX;


public HardLocationImpl (BitVector address,int wordLength){
	this.address=address;
	this.wordLength=wordLength;
	counters = new byte[wordLength];
}

public HardLocationImpl (BitVector address){
	this (address,address.size());
}
/* (non-Javadoc)
 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.HardLocation#getAddress()
 */
public BitVector getAddress() {
	return address;
}
/* (non-Javadoc)
 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.HardLocation#setAddress(cern.colt.bitvector.BitVector)
 */
public void setAddress(BitVector address) {
	this.address = address;
}
/* (non-Javadoc)
 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.HardLocation#getCounters()
 */
public byte[] getCounters() {
	return counters;
}
/* (non-Javadoc)
 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.HardLocation#getWrites()
 */
public int getWrites() {
	return writes;
}

/* (non-Javadoc)
 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.HardLocation#write(cern.colt.bitvector.BitVector)
 */
public void write(BitVector word) {
	writes++;
	int size=word.size();
	
//	if (size>wordLength){
//		throw new IllegalArgumentException();
//	}
	for (int j = 0; j < size; j++) {
		if (word.getQuick(j)) {
			if (counters[j] < counterMax) {
				counters[j] += 1;
			}
		} else {
			if (counters[j] > -counterMax) {
				counters[j] += -1;
			}
		}
	}
}

public void setCounters(byte[] newCounters) {
    for (int i = 0; i < this.wordLength; i++) {
        counters[i] = newCounters[i];
    }
}

/* (non-Javadoc)
 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.HardLocation#read(int[])
 */
public int[] read(int[] buff) {

//	if (buff.length<wordLength){
//		throw new IllegalArgumentException();
//	}

	for (int i = 0; i < wordLength; i++) {
		buff[i] += Integer.signum(counters[i]);
	}
	return buff;
}

/**
 * Calculates the Hamming distances between the vector and the hardlocation address.
 * 
 * @return the Hamming distances
 */
public int hamming(BitVector vector) {
	BitVector aux = vector.copy();
	aux.xor(address);

	return aux.cardinality();
}

}
