package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public class MockDetectionAlgorithm extends BasicDetectionAlgorithm {

	public MockDetectionAlgorithm(PamLinkable linkable, SensoryMemory sm,
			PerceptualAssociativeMemory pam) {
		super(linkable, sm, pam);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double detect() {
		// TODO Auto-generated method stub
		return 0;
	}

}
