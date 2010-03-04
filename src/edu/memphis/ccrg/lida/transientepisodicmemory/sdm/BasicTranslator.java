/*
 * @(#)BasicTranslator.java  1.0  January 29, 2010
 *
 * Copyright 2006-2010 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * 
 * @author Javier Snaider
 */
public class BasicTranslator implements Translator {

	private int size;
	private PerceptualAssociativeMemory pam;

        /**
         * 
         * @param data
         * @return
         * @throws Exception
         */
	public NodeStructure translate(BitVector data) throws Exception {
		NodeStructure ns = new NodeStructureImpl();
		for (int i = 0;i<size;i++){
			if(data.getQuick(i)){
				Node n= pam.getNode(i);
				ns.addNode(NodeFactory.getInstance().getNode(n));
			}
		}
		return ns;
	}

        /**
         *
         * @param structure
         * @return
         * @throws Exception
         */
	public BitVector translate(NodeStructure structure) throws Exception {
		BitVector v = new BitVector (size);
		for (Node n : structure.getNodes()){
			v.put((int)n.getId(), true);
		}
		return v;
	}

        /**
         *
         * @param size
         * @param pam
         */
	public BasicTranslator(int size, PerceptualAssociativeMemory pam) {
		super();
		this.size = size;
		this.pam=pam;
	}

}
