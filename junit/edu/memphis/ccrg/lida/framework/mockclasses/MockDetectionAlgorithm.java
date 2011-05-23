package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public class MockDetectionAlgorithm extends BasicDetectionAlgorithm {

	public MockDetectionAlgorithm(PamLinkable linkable, SensoryMemory sm,
			PerceptualAssociativeMemory pam) {
		super(linkable, sm, pam);
	}

	@Override
	public double detect() {
		
		return 0;
	}

}
