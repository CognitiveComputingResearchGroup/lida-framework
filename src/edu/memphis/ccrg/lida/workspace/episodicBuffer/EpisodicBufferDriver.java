package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class EpisodicBufferDriver extends GenericModuleDriver {

	private EpisodicBufferImpl eBuffer;

	public EpisodicBufferDriver(EpisodicBufferImpl eb, FrameworkTimer timer) {
		super(timer);
		this.eBuffer = eb;
	}

	@Override
	public void cycleStep() {
		eBuffer.activateCodelets();
		eBuffer.sendGuiContent();

	}

}// class