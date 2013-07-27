/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.episodicmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.episodicmemory.sdm.SparseDistributedMemory;
import edu.memphis.ccrg.lida.framework.mockclasses.MockLocalAssocListener;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTranslator;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;

/**
 * @author Javier Snaider
 */
public class EpisodicMemoryImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();

	private EpisodicMemoryImpl em;
	private MockTranslator translator;
	private NodeStructureImpl content;
	private Node node1;
	private MockLocalAssocListener listener;

	/**
	 * @throws java.lang.Exception
	 *             e
	 */
	@Before
	public void setUp() throws Exception {
		em = new EpisodicMemoryImpl();
		em.init();
		translator = new MockTranslator();
		content = new NodeStructureImpl();
		node1 = factory.getNode();
		listener = new MockLocalAssocListener();
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#init()}.
	 */
	@Test
	public void testInit() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em.numOfHardLoc", 20);
		params.put("em.addressLength", 100);
		params.put("em.activationRadius", 451);
		params.put("em.wordLength", 150);
		em.init(params);
		SparseDistributedMemory sdm = em.getSdm();
		BitVector addr = new BitVector(100);

		assertNotNull(sdm);
		assertEquals(150, (sdm.retrieve(addr).size()));
	}

	@Test
	public void testReceiveBroadcast() {
		em.setTranslator(translator);
		Coalition c = new CoalitionImpl(content, null);
		em.receiveBroadcast(c);

		assertTrue(NodeStructureImpl.compareNodeStructures(content,
				translator.ns));
		BitVector v = em.getSdm().retrieve(translator.v);
		assertEquals(translator.v, v);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#receiveCue(edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure)}
	 * .
	 */
	@Test
	public void testReceiveCue() {
		NodeStructure ns2 = new NodeStructureImpl();
		ns2.addDefaultNode(node1);
		translator.ns2 = ns2;
		em.getSdm().store(translator.v);
		em.setTranslator(translator);
		em.addListener(listener);

		em.receiveCue(content);
		assertEquals(content, translator.ns);
		assertEquals(translator.v, translator.data);
		assertEquals(ns2, listener.ns);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)}
	 * .
	 */
	@Test
	public void testAddListener() {
		NodeStructure ns2 = new NodeStructureImpl();
		ns2.addDefaultNode(node1);
		MockLocalAssocListener listener2 = new MockLocalAssocListener();

		translator.ns2 = ns2;
		em.getSdm().store(translator.v);
		em.setTranslator(translator);
		em.addListener(listener);
		em.addListener(listener2);

		em.receiveCue(content);

		assertEquals(ns2, listener.ns);
		assertEquals(ns2, listener2.ns);
	}

	@Test
	public void testsetTranslator() {
		em.setTranslator(translator);
		assertEquals(translator, em.getTranslator());

	}

}
