package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

public class CsmDriver extends ModuleDriverImpl {

	private CurrentSituationalModelImpl csm;

	public CsmDriver(CurrentSituationalModelImpl csm, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		this.csm = csm;
	}

	public void runThisDriver() {
		csm.sendCSMContent();
	}

	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}

}// class
