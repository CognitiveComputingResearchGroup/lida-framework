/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ConcurrentHashSetTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test1() {
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
