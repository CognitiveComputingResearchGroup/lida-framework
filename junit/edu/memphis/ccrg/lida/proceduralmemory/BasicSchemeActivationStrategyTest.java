/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class BasicSchemeActivationStrategyTest{
	
	private static ElementFactory factory = ElementFactory.getInstance();
	private Node node1, node2, node3;
	private NodeStructure broadcast;
	private Map<ExtendedId, Set<Scheme>>  map;
	private MockProceduralMemory pm;
	private Scheme a, b, c;
	
	@Before
	public void setUp() throws Exception {	
		pm = new MockProceduralMemory();
		
		node1 = factory.getNode();
		node1.setActivation(1.0);
		node2 = factory.getNode();
		node2.setActivation(1.0);
		node3 = factory.getNode();
		node3.setActivation(1.0);

		map = new HashMap<ExtendedId, Set<Scheme>>();
		
		a = new SchemeImpl("", null);
		b = new SchemeImpl("", null);
		c = new SchemeImpl("", null);
		
		Set<Scheme> node1Set = new HashSet<Scheme>();
		Set<Scheme> node2Set = new HashSet<Scheme>();
		Set<Scheme> node3Set = new HashSet<Scheme>();
		
		NodeStructure context = new NodeStructureImpl();
		context.addDefaultNode(node1);
		a.setContext(context);
		node1Set.add(a);
		
		context = new NodeStructureImpl();
		context.addDefaultNode(node1);
		context.addDefaultNode(node2);
		b.setContext(context);
		node1Set.add(b);
		node2Set.add(b);
		
		context = new NodeStructureImpl();
		context.addDefaultNode(node1);
		context.addDefaultNode(node2);
		context.addDefaultNode(node3);
		c.setContext(context);
		node1Set.add(c);
		node2Set.add(c);
		node3Set.add(c);
		
		map.put(node1.getExtendedId(), node1Set);
		map.put(node2.getExtendedId(), node2Set);
		map.put(node3.getExtendedId(), node3Set);
	}	
	@Test
	public void test1(){
		SchemeActivationStrategy behavior = new BasicSchemeActivationStrategy();
		behavior.setProceduralMemory(pm);
		
		broadcast = new NodeStructureImpl();
		broadcast.addDefaultNode(node1);
		broadcast.addDefaultNode(node2);
		broadcast.addDefaultNode(node3);	
		
		behavior.setSchemeSelectionThreshold(1.0);
		
		behavior.activateSchemesWithBroadcast(broadcast, map);
		
		Collection<Scheme> instantiated = pm.getTestInstantiated();
		assertTrue(instantiated.size()+"",instantiated.size() == 3);
		assertTrue(instantiated.contains(a));
		assertTrue(instantiated.contains(b));
		assertTrue(instantiated.contains(c));
	}
	@Test
	public void test2(){
		SchemeActivationStrategy behavior = new BasicSchemeActivationStrategy();
		behavior.setProceduralMemory(pm);
		
		broadcast = new NodeStructureImpl();
		broadcast.addDefaultNode(node1);
		broadcast.addDefaultNode(node2);
		
		behavior.setSchemeSelectionThreshold(1.0);
		behavior.activateSchemesWithBroadcast(broadcast, map);
		
		Collection<Scheme> instantiated = pm.getTestInstantiated();
		assertTrue(instantiated.size()+"",instantiated.size() == 2);
		assertTrue(instantiated.contains(a));
		assertTrue(instantiated.contains(b));
		assertFalse(instantiated.contains(c));
	}
	@Test
	public void test3(){
		SchemeActivationStrategy behavior = new BasicSchemeActivationStrategy();
		behavior.setProceduralMemory(pm);
		
		broadcast = new NodeStructureImpl();
		broadcast.addDefaultNode(node1);
		broadcast.addDefaultNode(node2);
		
		behavior.setSchemeSelectionThreshold(0.5);
		behavior.activateSchemesWithBroadcast(broadcast, map);
		
		Collection<Scheme> instantiated = pm.getTestInstantiated();
		assertTrue(instantiated.size()+"",instantiated.size() == 3);
		assertTrue(instantiated.contains(a));
		assertTrue(instantiated.contains(b));
		assertTrue(instantiated.contains(c));
	}
	@Test
	public void test4(){
		SchemeActivationStrategy behavior = new BasicSchemeActivationStrategy();
		behavior.setProceduralMemory(pm);
		
		broadcast = new NodeStructureImpl();
		broadcast.addDefaultNode(node2);
		
		behavior.setSchemeSelectionThreshold(0.4);
		behavior.activateSchemesWithBroadcast(broadcast, map);
		
		Collection<Scheme> instantiated = pm.getTestInstantiated();
		assertTrue(instantiated.size()+"",instantiated.size() == 1);
		assertFalse(instantiated.contains(a));
		assertTrue(instantiated.contains(b));
		assertFalse(instantiated.contains(c));
	}
}
