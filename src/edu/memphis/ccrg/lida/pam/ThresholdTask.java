package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;

public class ThresholdTask extends LidaTaskImpl {
	
	private PamNode pamNode;
	private PerceptualAssociativeMemory pam;

	public ThresholdTask(PamNode pamNode, PerceptualAssociativeMemory pam) {
		this.pamNode = pamNode;
		this.pam = pam;
	}

	public void run() {
		if(pamNode.isOverThreshold())
			pam.addNodeToPercept(pamNode);
		this.setTaskStatus(LidaTask.FINISHED);
	}

}//class
