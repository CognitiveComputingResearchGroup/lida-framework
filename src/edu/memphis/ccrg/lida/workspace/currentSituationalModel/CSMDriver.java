package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class CSMDriver extends GenericModuleDriver {

	private CurrentSituationalModelImpl csm;

	public CSMDriver(CurrentSituationalModelImpl csm, FrameworkTimer timer) {
		super(timer);
		this.csm = csm;
	}

	@Override
	public void cycleStep() {
		csm.sendCSMContent();
		csm.sendEvent();

	}

}// class
