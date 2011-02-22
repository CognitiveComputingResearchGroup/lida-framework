/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.episodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * This is the class that translates from nodes to boolean vectors and vice-
 * versa. The translation works by assigning a unique index to every node.
 * 
 * @author Javier Snaider
 */
public class BasicTranslator implements Translator {

	private int size;
	private PerceptualAssociativeMemory pam;
	private LidaElementFactory factory;
	

	/**
	 * Constructor of the class.
	 * 
	 * @param size
	 *            the number of positions of the bit vector
	 * @param pam
	 *            the PAM associated with this translator
	 */
	public BasicTranslator(int size, PerceptualAssociativeMemory pam) {
		super();
		this.size = size;
		this.pam = pam;
		this.factory=LidaElementFactory.getInstance();
	}

	/**
	 * Translates a bit vector into a node structure. Since the getQuick method
	 * in the BitVector class is used, no preconditions are checked.
	 * 
	 * @param data
	 *            the boolean vector to be translated
	 * @return a node structure representing the positions in the bit vector,
	 *         each node has a unique ID
	 * @see BitVector
	 */
	@Override
	public NodeStructure translate(BitVector data) {
		NodeStructure ns = new NodeStructureImpl();
		for (int i = 0; i < size; i++) {
			if (data.getQuick(i)) {
				Node n = pam.getPamNode(i);
				ns.addDefaultNode(factory.getNode(n));
			}
		}
		return ns;
	}

	/**
	 * Translates a node structure into a bit vector. At this point only nodes
	 * are being translated, but links, and maybe activations must be also
	 * handled.
	 * 
	 * @param structure
	 *            the node structure to be translated
	 * @return a bit vector representing the nodes in the node structure
	 */
	@Override
	public BitVector translate(NodeStructure structure) {
		BitVector v = new BitVector(size);
		for (Node n : structure.getNodes()) {
			v.put(n.getId(), true);
		}
		return v;
	}

}
