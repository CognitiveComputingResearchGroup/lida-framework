package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.framework.FrameworkThreadManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class EpisodicBufferDriver extends GenericModuleDriver {

	private EpisodicBufferImpl eBuffer;

	public EpisodicBufferDriver(EpisodicBufferImpl eb, FrameworkThreadManager timer) {
		super(timer);
		this.eBuffer = eb;
	}

	@Override
	public void cycleStep() {
		//eBuffer.activateCodelets();
		eBuffer.sendEvent();

	}

}// class