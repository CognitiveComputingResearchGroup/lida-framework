package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

/**
 * A task to add a node to the percept.
 * @author ryanjmccall
 *
 */
public class AddToPerceptTask extends LidaTaskImpl {
	
	private PamNode pamNode;
	private PerceptualAssociativeMemory pam;

	public AddToPerceptTask(PamNode pamNode, PerceptualAssociativeMemory pam, LidaTaskManager tm) {
		super(tm);
		this.pamNode = pamNode;
		this.pam = pam;
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	public void runThisLidaTask() {
		pam.addNodeToPercept(pamNode);
		this.setTaskStatus(LidaTask.FINISHED);
	}
	public String toString(){
		return "Threshold " + getTaskId();
	}

}//class
