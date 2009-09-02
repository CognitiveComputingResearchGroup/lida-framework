package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class PerceptualBufferDriver extends ModuleDriverImpl {

	private PerceptualBufferImpl pb;

	public PerceptualBufferDriver(PerceptualBufferImpl pb, int ticksPerCycle) {
		super(ticksPerCycle);
		this.pb = pb;
	}// constructor


	public void runThisLidaTask() {
		//pb.activateCodelets();
		pb.sendEvent();
	}


	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class