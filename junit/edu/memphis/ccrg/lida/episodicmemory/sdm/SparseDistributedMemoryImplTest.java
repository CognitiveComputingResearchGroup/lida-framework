package edu.memphis.ccrg.lida.episodicmemory.sdm;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cern.colt.bitvector.BitVector;

public class SparseDistributedMemoryImplTest {
	
	private SparseDistributedMemory sdm;
	private static int MSIZE = 10000;
	private static int WSIZE = 1000;
	private static int RADIUS = 451;
	private BitVector v1;
	private BitVector v2;
	private BitVector v3;
	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sdm = new SparseDistributedMemoryImpl(MSIZE, RADIUS, WSIZE);
		v1 = BitVectorUtils.getRandomVector(WSIZE);
		v2 = BitVectorUtils.getRandomVector(WSIZE);
		v3 = BitVectorUtils.getRandomVector(WSIZE);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStoreBitVectorBitVector() {
		sdm.store(v1);
		BitVector ret = sdm.retrieve(v1);
		assertEquals(v1, ret);
	}

	@Test
	public void testStoreBitVector() {
		sdm.store(v1,v2);
		BitVector ret = sdm.retrieve(v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testMappedStoreBitVectorBitVector() {
		sdm.mappedStore(v1,v2);
		BitVector ret = sdm.retrieve(v1, v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testMappedStoreBitVectorBitVectorBitVector() {
		sdm.mappedStore(v1,v2,v3);
		BitVector ret = sdm.retrieve(v2, v3);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieve() {
		sdm.store(v1,v2);
		BitVector ret = sdm.retrieve(v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVector() {
		sdm.mappedStore(v1,v2);
		BitVector addr = BitVectorUtils.noisyVector(v1, 50);
		BitVector ret = sdm.retrieveIterating(addr,v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVectorBitVector() {
		sdm.store(v1,v2);
		BitVector addr = BitVectorUtils.noisyVector(v2, 50);
		BitVector ret = sdm.retrieve(addr);
		assertEquals(v1, ret);
	}
}
