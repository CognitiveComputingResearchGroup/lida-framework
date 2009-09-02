package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class CsmDriver extends ModuleDriverImpl {

	private CurrentSituationalModelImpl csm;

	public CsmDriver(CurrentSituationalModelImpl csm, int ticksPerCycle) {
		super(ticksPerCycle);
		this.csm = csm;
	}


	public void runThisDriver() {
		csm.sendCSMContent();
		csm.sendEvent();
	}


	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class
