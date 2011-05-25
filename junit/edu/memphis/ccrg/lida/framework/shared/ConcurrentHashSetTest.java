package edu.memphis.ccrg.lida.framework.shared;


import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ConcurrentHashSetTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test1(){
		Set<String> hashSet = new ConcurrentHashSet<String>();
		assertNotNull(hashSet);
		assertEquals(0, hashSet.size());
		
		hashSet = new ConcurrentHashSet<String>(hashSet);
		assertNotNull(hashSet);
		assertEquals(0, hashSet.size());
		
		hashSet = new ConcurrentHashSet<String>(34);
		assertNotNull(hashSet);
		assertEquals(0, hashSet.size());
		
		hashSet = new ConcurrentHashSet<String>(4, 0.99F);
		assertNotNull(hashSet);
		assertEquals(0, hashSet.size());
	}

}
