package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;

public class EpisodicBufferDriver extends ModuleDriverImpl {

	private EpisodicBufferImpl eBuffer;

	public EpisodicBufferDriver(EpisodicBufferImpl eb, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm, LidaTaskNames.episodicBufferDriver);
		this.eBuffer = eb;
	}

	public void runThisDriver() {
		//eBuffer.activateCodelets();
		eBuffer.sendEvent();
	}

	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class