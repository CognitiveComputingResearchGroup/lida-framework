/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.bitvector.BitVector;

/**
 * Implementation of Kanerva's sparse distributed memory. This implementation is
 * based on the model described in P. Kanerva, "Sparse Distributed Memory and
 * Related Models" in <i>Associative Neural Memories: Theory and Implementation
 * </i>, pp. 50-76, Oxford University Press, 1993.
 * 
 * @author Javier Snaider
 */
public class SparseDistributedMemoryImpl implements SparseDistributedMemory {

	private static Logger logger = Logger.getLogger(SparseDistributedMemoryImpl.class.getCanonicalName());
	private static final int MAX_ITERATIONS = 20;
	private HardLocation[] hardlocations;
	private int wordLength;
	private int addrLength;
	private int memorySize;
	private int activationRadius;

	/**
	 * Constructor of the class that receives all the parameters necessary for
	 * this sparse distributed memory.
	 * 
	 * @param memorySize
	 *            the size of the memory
	 * @param radious
	 *            the activation radius
	 * @param wordLength
	 *            the word size
	 */
	public SparseDistributedMemoryImpl(int memorySize, int radious, int wordLength) {
		// Memory's internal parameters
		this.memorySize = memorySize;
		activationRadius = radious;

		this.wordLength = wordLength;
		hardlocations = new HardLocation[memorySize];
		for (int i =0; i<memorySize;i++){
			hardlocations[i]=new HardLocationImpl(BitVectorUtils.getRandomVector(wordLength));
		}
	}
	/**
	 * Constructor of the class that receives all the parameters necessary for
	 * this sparse distributed memory.
	 * 
	 * @param memorySize
	 *            the size of the memory
	 * @param radious
	 *            the activation radius
	 * @param wordLength
	 *            the word size
	 * @param addrLength
	 *            the address size
	 */
	public SparseDistributedMemoryImpl(int memorySize, int radious, int wordLength, int addrLength) {
		this.activationRadius = radious;

		this.addrLength = addrLength;
		this.memorySize = memorySize;
		this.wordLength = wordLength;
		hardlocations = new HardLocation[memorySize];
		for (int i =0; i<memorySize;i++){
			hardlocations[i]=new HardLocationImpl(BitVectorUtils.getRandomVector(addrLength),wordLength);
		}
	}


	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#store(cern.colt.bitvector.BitVector, cern.colt.bitvector.BitVector)
	 */
	@Override
	public void store(BitVector wrd, BitVector addr) {

		for (int i = 0; i < memorySize; i++) {
			if (hardlocations[i].hamming(addr) <= activationRadius) {
				hardlocations[i].write(wrd);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#store(cern.colt.bitvector.BitVector)
	 */
	@Override
	public void store(BitVector wrd) {
		store(wrd,wrd);
			}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#mappedStore(cern.colt.bitvector.BitVector, cern.colt.bitvector.BitVector)
	 */
	@Override
	public void mappedStore(BitVector wrd, BitVector mapping) {
		if(wrd.size()==addrLength){
		BitVector mapped = wrd.copy();
		mapped.xor(mapping);
		store(mapped);
		}else{
			BitVector mapped = wrd.partFromTo(0, addrLength-1);
			mapped.xor(mapping);
			BitVector aux=wrd.copy();
			aux.replaceFromToWith(0, addrLength-1, mapped, 0);
			store(aux,mapped);
		}
			}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#mappedStore(cern.colt.bitvector.BitVector,cern.colt.bitvector.BitVector, cern.colt.bitvector.BitVector)
	 */
	@Override
	public void mappedStore(BitVector wrd, BitVector addr, BitVector mapping) {
		BitVector mapped = addr.copy();
		mapped.xor(mapping);
		store(wrd,mapped);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#retrieve(cern.colt.bitvector.BitVector)
	 */
	@Override
	public BitVector retrieve(BitVector addr) {
		int[] buff = new int[wordLength];
		for (int i = 0; i < memorySize; i++) {
			if (hardlocations[i].hamming(addr) <= activationRadius) {
				hardlocations[i].read(buff);
			}
		}
		BitVector res = new BitVector(wordLength);
		for (int i = 0; i < wordLength; i++) {
			res.putQuick(i, buff[i] > 0);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#retrieveIterating(cern.colt.bitvector.BitVector)
	 */
	@Override
	public BitVector retrieveIterating(BitVector addr) {

		BitVector res=null;
			for (int i = 1; i < MAX_ITERATIONS; i++) {
				res = retrieve(addr);
				BitVector aux =res.partFromTo(0, addr.size()-1);
				if (aux.equals(addr)) {
					logger.log(Level.FINER,"number of iterations: "+i);
					return res;
				}
				addr = aux;
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory#retrieveIterating(cern.colt.bitvector.BitVector, cern.colt.bitvector.BitVector)
	 */
	@Override
	public BitVector retrieveIterating(BitVector addr,BitVector mapping) {
		BitVector mapped = addr.copy();
		mapped.xor(mapping);

		BitVector res= retrieveIterating(mapped);
		if (res.size()==addrLength){
			res.xor(mapping);			
		}else{
		BitVector aux = res.partFromTo(0, addrLength-1);
		aux.xor(mapping);
		res.replaceFromToWith(0, addrLength-1, aux, 0);
		}
		return res;
	}

        @Override
		public Object getState() {
            BitVector[] addresses = new BitVector[memorySize];
            byte[][] counters = new byte[memorySize][];

            for (int i = 0; i < memorySize; i++) {
                addresses[i] = hardlocations[i].getAddress();
                counters[i] = hardlocations[i].getCounters();
            }

            Object[] state = new Object[2];
            state[0] = addresses;
            state[1] = counters;

            return state;
        }
        @Override
		public boolean setState(Object content) {
            if (content instanceof Object[]) {
                try {
                    Object[] state = (Object[])content;
                    BitVector[] addresses = (BitVector[])state[0];
                    byte[][] counters = (byte[][])state[1];
//                    BitVector bv;
                    for (int i = 0; i < addresses.length; i++) {
                        hardlocations[i].setAddress(addresses[i]);
                        hardlocations[i].setCounters(counters[i]);
                    }
                    return true;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        }
}