/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;

public class ProceduralMemoryImplTest{

	private ProceduralMemory pm;

	@Before
	public void setUp() throws Exception {
		pm = new ProceduralMemoryImpl();
	}
	@Test
	public void testAddScheme() {
		Scheme s = new SchemeImpl();
		pm.addScheme(s);

		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 1);

		pm.addScheme(s);
		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 1);

		s = new SchemeImpl();
		pm.addScheme(s);
		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 2);
	}
	@Test
	public void testSendInstantiatedScheme() {
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		Action a = new ActionImpl();
		Scheme s = new SchemeImpl("foo", a);
		
		pm.createInstantiation(s);
		
		assertEquals(listener.behavior.getAction(), a);
		assertEquals(listener.behavior.getLabel(), "foo");
	}

}