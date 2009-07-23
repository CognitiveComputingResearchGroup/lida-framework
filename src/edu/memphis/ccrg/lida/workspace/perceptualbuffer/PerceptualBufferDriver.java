package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.framework.FrameworkThreadManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class PerceptualBufferDriver extends GenericModuleDriver {

	private PerceptualBufferImpl pb;

	public PerceptualBufferDriver(PerceptualBufferImpl pb, FrameworkThreadManager timer) {
		super(timer);
		this.pb = pb;
	}// constructor

	@Override
	public void cycleStep() {
		//pb.activateCodelets();
		pb.sendEvent();

	}

}// class