/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import cern.colt.bitvector.BitVector;

public class SparseDistributedMemoryImplTest {

	private SparseDistributedMemory sdm;
	private static int MSIZE = 10000;
	private static int WSIZE = 1000;
	private static int RADIUS = 451;
	private BitVector v1;
	private BitVector v2;

	@Before
	public void setUp() throws Exception {
		sdm = new SparseDistributedMemoryImpl(MSIZE, RADIUS, WSIZE);
		v1 = BitVectorUtils.getRandomVector(WSIZE);
		v2 = BitVectorUtils.getRandomVector(WSIZE);
	}

	@Test
	public void testStoreBitVectorBitVector() {
		sdm.store(v1);
		BitVector ret = sdm.retrieve(v1);
		assertEquals(v1, ret);
		ret = sdm.retrieve(v2);
		assertNotSame(v2, ret);
	}

	@Test
	public void testStoreBitVector() {
		sdm.store(v1, v2);
		BitVector ret = sdm.retrieve(v2);
		assertEquals(v1, ret);
		assertNotSame(v2, ret);
	}

	@Test
	public void testMappedStoreBitVectorBitVector() {
		sdm.mappedStore(v1, v2);
		BitVector ret = sdm.retrieve(v1, v2);
		assertEquals(v1, ret);
		ret = sdm.retrieve(v1);
		assertNotSame(v1, ret);
	}

	@Test
	public void testRetrieve() {
		sdm.store(v1, v2);
		BitVector ret = sdm.retrieve(v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVector() {
		sdm.store(v1);
		BitVector addr = BitVectorUtils.getNoisyVector(v1, 50);

		for (int i = 0; i < 50; i++) {
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.store(aux);
		}

		BitVector ret = sdm.retrieveIterating(addr);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVector2() {
		sdm.store(v1);
		BitVector addr = BitVectorUtils.getNoisyVector(v2, 50);

		for (int i = 0; i < 50; i++) {
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.store(aux);
		}

		BitVector ret = sdm.retrieveIterating(addr);
		assertNotSame(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVectorBitVector() {
		sdm.mappedStore(v1, v2);
		BitVector addr = BitVectorUtils.getNoisyVector(v1, 50);

		for (int i = 0; i < 50; i++) {
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.mappedStore(aux, v2);
		}

		BitVector ret = sdm.retrieveIterating(addr, v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVectorBitVector2() {
		sdm.mappedStore(v1, v2);
		BitVector addr = BitVectorUtils.getRandomVector(WSIZE);
		BitVector ret = sdm.retrieveIterating(addr, v2);

		for (int i = 0; i < 50; i++) {
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.mappedStore(aux, v2);
		}

		BitVector aux = v1.copy();
		aux.xor(v2);
		addr.xor(v2);
		BitVectorUtils.hamming(addr, aux);
		assertNotSame(v1, ret);
	}
}
