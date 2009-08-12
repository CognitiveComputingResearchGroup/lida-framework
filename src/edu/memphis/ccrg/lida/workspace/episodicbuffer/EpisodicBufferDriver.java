package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class EpisodicBufferDriver extends ModuleDriverImpl {

	private EpisodicBufferImpl eBuffer;

	public EpisodicBufferDriver(EpisodicBufferImpl eb, LidaTaskManager timer, int ticksPerCycle) {
		super(timer, ticksPerCycle);
		this.eBuffer = eb;
	}

	public void runDriverOneProcessingStep() {
		//eBuffer.activateCodelets();
		eBuffer.sendEvent();

	}

}// class