package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

public class ThresholdTask extends LidaTaskImpl {
	
	private PamNode pamNode;
	private PerceptualAssociativeMemory pam;

	public ThresholdTask(PamNode pamNode, PerceptualAssociativeMemory pam, LidaTaskManager tm) {
		super(tm);
		this.pamNode = pamNode;
		this.pam = pam;
	}

	public void runThisLidaTask() {
		//System.out.println(pamNode.getLabel() + " " + pamNode.getActivation());
		if(pamNode.isOverThreshold())
			pam.addNodeToPercept(pamNode);
		this.setTaskStatus(LidaTask.FINISHED);
	}
	public String toString(){
		return "ThresholdTask-"+ getTaskId();
	}

}//class
