package edu.memphis.ccrg.lida.transientepisodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

public class BasicTranslator implements Translator {

	private int size;
	private PerceptualAssociativeMemory pam;

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

	public BitVector translate(NodeStructure structure) throws Exception {
		BitVector v = new BitVector (size);
		for (Node n : structure.getNodes()){
			v.put((int)n.getId(), true);
		}
		return v;
	}

	/**
	 * @param size
	 */
	public BasicTranslator(int size, PerceptualAssociativeMemory pam) {
		super();
		this.size = size;
		this.pam=pam;
	}

}
