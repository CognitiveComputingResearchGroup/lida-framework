/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Implementation of Kanerva's sparse distributed memory. This implementation is
 * based on the model described in P. Kanerva, "Sparse Distributed Memory and
 * Related Models" in <i>Associative Neural Memories: Theory and Implementation
 * </i>, pp. 50-76, Oxford University Press, 1993.
 * 
 * TODO Logging
 * 
 * @author Javier Snaider
 */
public class SparseDistributedMemoryImpl implements SparseDistributedMemory {

	private static final Logger logger = Logger
			.getLogger(SparseDistributedMemoryImpl.class.getCanonicalName());
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
	 * @param radius
	 *            the activation radius
	 * @param wordLength
	 *            the word size
	 */
	public SparseDistributedMemoryImpl(int memorySize, int radius,
			int wordLength) {
		// Memory's internal parameters
		this.memorySize = memorySize;
		activationRadius = radius;

		this.wordLength = wordLength;
		this.addrLength = wordLength;
		hardlocations = new HardLocation[memorySize];
		for (int i = 0; i < memorySize; i++) {
			hardlocations[i] = new HardLocationImpl(
					BitVectorUtils.getRandomVector(wordLength));
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
	public SparseDistributedMemoryImpl(int memorySize, int radious,
			int wordLength, int addrLength) {
		this.activationRadius = radious;

		this.addrLength = addrLength;
		this.memorySize = memorySize;
		this.wordLength = wordLength;
		hardlocations = new HardLocation[memorySize];
		for (int i = 0; i < memorySize; i++) {
			hardlocations[i] = new HardLocationImpl(
					BitVectorUtils.getRandomVector(addrLength), wordLength);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory
	 * #store(cern.colt.bitvector.BitVector, cern.colt.bitvector.BitVector)
	 */
	@Override
	public void store(BitVector wrd, BitVector addr) {
		for (int i = 0; i < memorySize; i++) {
			if (hardlocations[i].hammingDistance(addr) <= activationRadius) {
				hardlocations[i].write(wrd);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory
	 * #store(cern.colt.bitvector.BitVector)
	 */
	@Override
	public void store(BitVector wrd) {
		store(wrd, wrd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory
	 * #mappedStore(cern.colt.bitvector.BitVector,
	 * cern.colt.bitvector.BitVector)
	 */
	@Override
	public void mappedStore(BitVector wrd, BitVector mapping) {
		if (wrd.size() == addrLength) {
			BitVector mapped = wrd.copy();
			mapped.xor(mapping);
			store(mapped);
		} else {
			BitVector mapped = wrd.partFromTo(0, addrLength - 1);
			mapped.xor(mapping);
			BitVector aux = wrd.copy();
			aux.replaceFromToWith(0, addrLength - 1, mapped, 0);
			store(aux, mapped);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory
	 * #retrieve(cern.colt.bitvector.BitVector)
	 */
	@Override
	public BitVector retrieve(BitVector addr) {
		// TODO case where address is null

		int[] buff = new int[wordLength];
		for (int i = 0; i < memorySize; i++) {
			if (hardlocations[i].hammingDistance(addr) <= activationRadius) {
				hardlocations[i].read(buff);
			}
		}
		BitVector res = new BitVector(wordLength);
		for (int i = 0; i < wordLength; i++) {
			boolean aux;
			if (buff[i]==0){
				aux = (Math.random()>.5);
			}else{
				aux = (buff[i] > 0);
			}
			res.putQuick(i, aux);
//			res.putQuick(i, (buff[i] > 0));
		}
		return res;
	}

	@Override
	public BitVector retrieve(BitVector addr, BitVector mapping) {
		BitVector mapped = addr.copy();
		mapped.xor(mapping);
		BitVector res = retrieve(mapped);
		if (res != null) {
			if (res.size() == addrLength) {
				res.xor(mapping);
			} else {
				BitVector aux = res.partFromTo(0, addrLength - 1);
				aux.xor(mapping);
				res.replaceFromToWith(0, addrLength - 1, aux, 0);
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory
	 * #retrieveIterating(cern.colt.bitvector.BitVector)
	 */
	@Override
	public BitVector retrieveIterating(BitVector addr) {
		BitVector res = null;
		for (int i = 1; i < MAX_ITERATIONS; i++) {
			res = retrieve(addr);
			BitVector aux = res.partFromTo(0, addr.size() - 1);
			// TODO hamming distance tolerance instead of strict equality
			if (aux.equals(addr)) {
				logger.log(Level.FINER, "number of iterations: {1}", 
						new Object[]{TaskManager.getCurrentTick(), i});
				return res;
			}
			addr = aux;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory
	 * #retrieveIterating(cern.colt.bitvector.BitVector,
	 * cern.colt.bitvector.BitVector)
	 */
	@Override
	public BitVector retrieveIterating(BitVector addr, BitVector mapping) {
		BitVector mapped = addr.copy();
		mapped.xor(mapping);

		BitVector res = retrieveIterating(mapped);
		if (res != null) {
			if (res.size() == addrLength) {
				res.xor(mapping);
			} else {
				BitVector aux = res.partFromTo(0, addrLength - 1);
				aux.xor(mapping);
				res.replaceFromToWith(0, addrLength - 1, aux, 0);
			}
		}
		return res;
	}

}