/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.proceduralmemory.ns.SchemeImpl;

public class SchemeImplTest {

	private SchemeImpl scheme;
	private Action action;
	private Node node1, node2, node3;

	private static ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		action = new ActionImpl();

		scheme = new SchemeImpl();
		scheme.setLabel("1");
		scheme.setAction(action);
		node1 = factory.getNode();
		node2 = factory.getNode();
		node3 = factory.getNode();
	}

	@Test
	public void test1() {
		assertEquals("1", scheme.getLabel());
		assertEquals(action, scheme.getAction());

		scheme = new SchemeImpl();

		scheme.setAction(action);
		assertEquals(action, scheme.getAction());
	}

	@Test
	public void test2() {
		// Action foo = new ActionImpl();
		// scheme.setAction(foo);
		// assertEquals(foo, scheme.getAction());
		//		
		// scheme.addToAddingList(node1);
		// assertTrue(scheme.containsAddingItem(node1));
		//		
		// scheme.addContextCondition(node2);
		// assertTrue(scheme.containsContextCondition(node2));
		//		
		// scheme.addToDeletingList(node3);
		// assertTrue(scheme.containsDeletingItem(node3));
		//		
		// assertFalse(scheme.isInnate());
		// scheme.setInnate(true);
		// assertTrue(scheme.isInnate());
	}

	@Test
	public void test3() {
		assertTrue(0 == scheme.getNumExecutions());
		assertTrue(0.0 == scheme.getReliability());
		assertTrue(scheme.isReliable());

		scheme.actionSuccessful();

		assertTrue(0 == scheme.getNumExecutions());
		assertTrue(0.0 == scheme.getReliability());
		assertTrue(scheme.isReliable());

		scheme.actionExecuted();
		scheme.actionExecuted();
		scheme.actionExecuted();
		scheme.actionExecuted();

		assertTrue(4 == scheme.getNumExecutions());
		assertTrue(0.25 == scheme.getReliability());
		SchemeImpl.setReliabilityThreshold(0.3);
		assertFalse(scheme.isReliable());

		scheme.actionSuccessful();
		scheme.actionSuccessful();
		scheme.actionSuccessful();

		assertTrue(scheme.isReliable());
	}

	@Test
	public void testEquals() {
		Scheme scheme2 = new SchemeImpl();
		scheme2.setId(2);
		Scheme scheme3 = new SchemeImpl();
		scheme3.setId(2);

		assertEquals(scheme2, scheme3);
		assertEquals(scheme2.hashCode(), scheme3.hashCode());

		assertFalse(scheme.equals(scheme2));
	}

}
