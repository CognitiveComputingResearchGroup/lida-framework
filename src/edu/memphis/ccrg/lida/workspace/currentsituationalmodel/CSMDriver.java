package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class CSMDriver extends ModuleDriverImpl {

	private CurrentSituationalModelImpl csm;

	public CSMDriver(CurrentSituationalModelImpl csm, LidaTaskManager timer, int ticksPerCycle) {
		super(timer, ticksPerCycle);
		this.csm = csm;
	}


	public void runSingleProcessingStep() {
		csm.sendCSMContent();
		csm.sendEvent();

	}

}// class
