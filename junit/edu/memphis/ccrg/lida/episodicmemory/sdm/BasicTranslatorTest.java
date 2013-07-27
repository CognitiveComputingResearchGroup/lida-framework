/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNS;

public class BasicTranslatorTest {

	private Translator tr;
	private static int SIZE = 100;
	private PerceptualAssociativeMemoryNS pam;
	private NodeStructure ns;
	private BitVector vector;

	@Before
	public void setUp() throws Exception {
		pam = new MockPAM();
		tr = new BasicTranslator(SIZE, pam);
		ns = new NodeStructureImpl();
		Node n = new NodeImpl();
		n.setId(10);
		ns.addDefaultNode(n);
		n = new NodeImpl();
		n.setId(20);
		ns.addDefaultNode(n);
		vector = new BitVector(SIZE);
		vector.set(10);
		vector.set(20);

	}

	@Test
	public void testTranslateBitVector() {
		BitVector v = tr.translate(ns);
		assertEquals(vector, v);
	}

	@Test
	public void testTranslateNodeStructure() {
		NodeStructure nss = tr.translate(vector);
		assert (nss.getNodes().equals(ns.getNodes()));
	}

}
