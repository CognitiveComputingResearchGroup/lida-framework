package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class PerceptualBufferDriver extends GenericModuleDriver {

	private PerceptualBufferImpl pb;

	public PerceptualBufferDriver(PerceptualBufferImpl pb, LidaTaskManager timer) {
		super(timer);
		this.pb = pb;
	}// constructor


	public void cycleStep() {
		//pb.activateCodelets();
		pb.sendEvent();

	}

}// class