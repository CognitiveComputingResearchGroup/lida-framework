package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class EpisodicBufferDriver extends GenericModuleDriver {

	private EpisodicBufferImpl eBuffer;

	public EpisodicBufferDriver(EpisodicBufferImpl eb, LidaTaskManager timer) {
		super(timer);
		this.eBuffer = eb;
	}

	public void cycleStep() {
		//eBuffer.activateCodelets();
		eBuffer.sendEvent();

	}

}// class