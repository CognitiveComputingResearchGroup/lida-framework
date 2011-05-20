package edu.memphis.ccrg.lida.episodicmemory;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.episodicmemory.sdm.BitVectorUtils;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

public class MockTranslator implements Translator {

	public NodeStructure ns;
	public BitVector v;
	@Override
	public NodeStructure translate(BitVector data) {
		return ns;
	}

	@Override
	public BitVector translate(NodeStructure structure) {
		ns=structure;
		v=BitVectorUtils.getRandomVector(1000);
		return v;
	}


}
