package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class PerceptualBufferDriver extends ModuleDriverImpl {

	private PerceptualBufferImpl pb;

	public PerceptualBufferDriver(PerceptualBufferImpl pb, LidaTaskManager timer) {
		super(timer);
		this.pb = pb;
	}// constructor


	public void runDriverOneProcessingStep() {
		//pb.activateCodelets();
		pb.sendEvent();

	}

}// class