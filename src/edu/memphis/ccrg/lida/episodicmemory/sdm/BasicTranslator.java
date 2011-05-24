/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.episodicmemory.sdm;

import java.util.Collection;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
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
	private static ElementFactory factory = ElementFactory.getInstance();;
	

	/**
	 * Constructor of the class.
	 * 
	 * @param size
	 *            the number of positions of the bit vector
	 * @param pam
	 *            the PAM associated with this translator
	 */
	public BasicTranslator(int size, PerceptualAssociativeMemory pam) {
		this.size = size;
		this.pam = pam;
	}

	/**
	 * Default constructor. The PAM and vectors' size must be set before to use this {@link Translator}.
	 */
	public BasicTranslator(){		
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
				Node n = pam.getNode(i);
				if(n == null){
					continue;
				}
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
		Collection<Node> nodes = structure.getNodes();
		if(nodes != null){
			for (Node n : nodes) {
				v.put(n.getId(), true);
			}
		}
		return v;
	}

	/**
	 * @return the vectors' size 
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the vectors' size in the {@link SparseDistributedMemory}
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the pam
	 */
	public PerceptualAssociativeMemory getPam() {
		return pam;
	}

	/**
	 * @param pam the pam to set
	 */
	public void setPam(PerceptualAssociativeMemory pam) {
		this.pam = pam;
	}

	
}
