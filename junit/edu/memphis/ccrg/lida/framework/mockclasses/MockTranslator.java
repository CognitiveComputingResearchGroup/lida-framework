/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.episodicmemory.sdm.BitVectorUtils;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;

public class MockTranslator implements Translator {

	public NodeStructure ns;
	public NodeStructure ns2;
	public BitVector v = BitVectorUtils.getRandomVector(1000);
	public BitVector data;

	@Override
	public NodeStructure translate(BitVector data) {
		this.data = data;
		return ns2;
	}

	@Override
	public BitVector translate(NodeStructure structure) {
		ns = structure;
		return v;
	}

}
