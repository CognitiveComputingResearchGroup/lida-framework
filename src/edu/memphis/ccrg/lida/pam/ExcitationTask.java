package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.TaskSpawner;

public class ExcitationTask extends LidaTaskImpl{
	
	private PamNode pamNode;
	private double excitationAmount;
	private PerceptualAssociativeMemory pam;
	private LidaTaskManager taskManager;
	private TaskSpawner taskSpawner;

	public ExcitationTask(PamNode node, double excitation,
			              PerceptualAssociativeMemory pam, 
			              TaskSpawner ts, 
			              LidaTaskManager tm) {
		super(tm);
		pamNode = node;
		excitationAmount = excitation;
		this.pam = pam;
		taskSpawner = ts;
		taskManager = tm;
	}

	protected void runThisLidaTask() {
		pamNode.excite(excitationAmount);
		if(pamNode.isOverThreshold()){
			ThresholdTask task = new ThresholdTask(pamNode, pam, taskManager);
			taskSpawner.addTask(task);
		}
		pam.sendActivationToParentsOf(pamNode);
		this.setTaskStatus(LidaTask.FINISHED);
	}//method

	public String toString(){
		return "Excitation " + getTaskId();
	}

}//class