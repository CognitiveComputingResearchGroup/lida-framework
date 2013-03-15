/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.colt.bitvector.BitVector;

public class HardLocationImplTest {

	private static int ADDSS_SIZE = 1000;
	private static int WRD_SIZE = 2000;

	private HardLocation hl;
	private BitVector address;

	@Before
	public void setUp() throws Exception {
		address = BitVectorUtils.getRandomVector(ADDSS_SIZE);
		hl = new HardLocationImpl(address, WRD_SIZE);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHardLocationImplBitVectorInt() {
		hl = new HardLocationImpl(address, WRD_SIZE);
		assertEquals(address, hl.getAddress());
		assertEquals(WRD_SIZE, hl.getCounters().length);
	}

	@Test
	public void testHardLocationImplBitVector() {
	}

	@Test
	public void testGetAddress() {
		assertEquals(address, hl.getAddress());
	}

	@Test
	public void testSetAddress() {
		BitVector v = BitVectorUtils.getRandomVector(ADDSS_SIZE);
		hl.setAddress(v);
		assert (!v.equals(hl.getAddress()));
		assertEquals(v, hl.getAddress());
	}

	@Test
	public void testGetCounters() {
		byte[] counters = hl.getCounters();
		assertEquals(WRD_SIZE, counters.length);
	}

	@Test
	public void testGetWriteCount() {
		for (int i = 0; i < 10; i++) {
			BitVector v = BitVectorUtils.getRandomVector(WRD_SIZE);
			hl.write(v);
		}
		assertEquals(10, hl.getWriteCount());
	}

	@Test
	public void testWrite() {
		BitVector v = BitVectorUtils.getRandomVector(WRD_SIZE);
		hl.write(v);
		byte[] counters = hl.getCounters();
		for (int i = 0; i < WRD_SIZE; i++) {
			if (v.get(i) && counters[i] == 1) {
				continue;
			} else {
				if (!v.get(i) && counters[i] == -1) {
					continue;
				}
			}
			assert (false);
		}
	}

	@Test
	public void testSetCounters() {
		byte[] counters = new byte[WRD_SIZE];
		hl.setCounters(counters);
		assert (Arrays.equals(counters, hl.getCounters()));
	}

	@Test
	public void testRead() {
		for (int i = 0; i < 10; i++) {
			BitVector v = BitVectorUtils.getRandomVector(WRD_SIZE);
			hl.write(v);
		}
		int[] buffer = new int[WRD_SIZE];
		buffer = hl.read(buffer);

		byte[] counters = hl.getCounters();
		for (int i = 0; i < WRD_SIZE; i++) {
			if ((counters[i] > 0) && buffer[i] == 1) {
				continue;
			} else {
				if ((counters[i] < 0) && buffer[i] == -1) {
					continue;
				}
			}
			assert (false);
		}

	}

	@Test
	public void testHammingDistance() {
		BitVector nv = BitVectorUtils.getRandomVector(ADDSS_SIZE);
		int hamm = 0;
		BitVector addr = hl.getAddress();
		for (int i = 0; i < ADDSS_SIZE; i++) {
			if (nv.get(i) != addr.get(i)) {
				hamm++;
			}
		}
		assertEquals(hamm, hl.hammingDistance(nv));
	}

}
