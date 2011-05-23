package edu.memphis.ccrg.lida.framework.mockclasses;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.episodicmemory.sdm.BitVectorUtils;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class MockTranslator implements Translator {

	public NodeStructure ns;
	public NodeStructure ns2;
	public BitVector v=BitVectorUtils.getRandomVector(1000);
	public BitVector data;
	@Override
	public NodeStructure translate(BitVector data) {
		this.data=data;
		return ns2;
	}

	@Override
	public BitVector translate(NodeStructure structure) {
		ns=structure;
		return v;
	}


}
