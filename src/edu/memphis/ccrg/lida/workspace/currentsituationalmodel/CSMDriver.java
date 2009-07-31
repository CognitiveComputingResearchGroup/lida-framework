package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class CSMDriver extends ModuleDriverImpl {

	private CurrentSituationalModelImpl csm;

	public CSMDriver(CurrentSituationalModelImpl csm, LidaTaskManager timer) {
		super(timer);
		this.csm = csm;
	}


	public void runDriverOneProcessingStep() {
		csm.sendCSMContent();
		csm.sendEvent();

	}

}// class
