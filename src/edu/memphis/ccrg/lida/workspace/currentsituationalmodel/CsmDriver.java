package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

/**
 * This class is not current being used. 10/22/09
 * @author ryanjmccall
 *
 */
public class CsmDriver extends ModuleDriverImpl {

	private CurrentSituationalModelImpl csm;

	public CsmDriver(CurrentSituationalModelImpl csm, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		this.csm = csm;
	}

	public void runThisDriver() {
		csm.sendCSMContent();
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}// class
