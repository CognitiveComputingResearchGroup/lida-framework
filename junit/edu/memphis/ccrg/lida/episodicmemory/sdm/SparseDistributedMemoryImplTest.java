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
	
	

	@Before
	public void setUp() throws Exception {
		sdm = new SparseDistributedMemoryImpl(MSIZE, RADIUS, WSIZE);
		v1 = BitVectorUtils.getRandomVector(WSIZE);
		v2 = BitVectorUtils.getRandomVector(WSIZE);
		v3 = BitVectorUtils.getRandomVector(WSIZE);
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
		sdm.store(v1,v2);
		BitVector ret = sdm.retrieve(v2);
		assertEquals(v1, ret);
		assertNotSame(v2, ret);
	}

	@Test
	public void testMappedStoreBitVectorBitVector() {
		sdm.mappedStore(v1,v2);
		BitVector ret = sdm.retrieve(v1, v2);
		assertEquals(v1, ret);
		ret = sdm.retrieve(v1);
		assertNotSame(v1, ret);
	}

	@Test
	public void testRetrieve() {
		sdm.store(v1,v2);
		BitVector ret = sdm.retrieve(v2);
		assertEquals(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVector() {
		sdm.store(v1);
		BitVector addr = BitVectorUtils.noisyVector(v1, 50);
		
		for (int i=0;i<50;i++){
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.store(aux);			
		}
		
		BitVector ret = sdm.retrieveIterating(addr);
		assertEquals(v1, ret);
	}
	@Test
	public void testRetrieveIteratingBitVector2() {
		sdm.store(v1);
		BitVector addr = BitVectorUtils.noisyVector(v2, 50);
		
		for (int i=0;i<50;i++){
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.store(aux);			
		}
		
		BitVector ret = sdm.retrieveIterating(addr);
		assertNotSame(v1, ret);
	}

	@Test
	public void testRetrieveIteratingBitVectorBitVector() {
		sdm.mappedStore(v1,v2);
		BitVector addr = BitVectorUtils.noisyVector(v1, 50);
		
		for (int i=0;i<50;i++){
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.mappedStore(aux,v2);			
		}
		
		BitVector ret = sdm.retrieveIterating(addr,v2);
		assertEquals(v1, ret);
	}
	@Test
	public void testRetrieveIteratingBitVectorBitVector2() {
		sdm.mappedStore(v1,v2);
		BitVector addr = BitVectorUtils.getRandomVector(WSIZE);
		BitVector ret = sdm.retrieveIterating(addr,v2);
		
		for (int i=0;i<50;i++){
			BitVector aux = BitVectorUtils.getRandomVector(WSIZE);
			sdm.mappedStore(aux,v2);			
		}
		
		BitVector aux = v1.copy();
		aux.xor(v2);
		addr.xor(v2);
		System.out.println(BitVectorUtils.hamming(addr, aux));
		assertNotSame(v1, ret);
	}
}
