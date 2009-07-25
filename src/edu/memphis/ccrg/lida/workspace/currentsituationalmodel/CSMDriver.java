package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class CSMDriver extends GenericModuleDriver {

	private CurrentSituationalModelImpl csm;

	public CSMDriver(CurrentSituationalModelImpl csm, LidaTaskManager timer) {
		super(timer);
		this.csm = csm;
	}


	public void cycleStep() {
		csm.sendCSMContent();
		csm.sendEvent();

	}

}// class
