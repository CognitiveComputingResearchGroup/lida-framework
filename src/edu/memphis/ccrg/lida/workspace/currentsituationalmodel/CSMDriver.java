package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class CSMDriver extends GenericModuleDriver {

	private CurrentSituationalModelImpl csm;

	public CSMDriver(CurrentSituationalModelImpl csm, FrameworkTaskManager timer) {
		super(timer);
		this.csm = csm;
	}

	@Override
	public void cycleStep() {
		csm.sendCSMContent();
		csm.sendEvent();

	}

}// class
