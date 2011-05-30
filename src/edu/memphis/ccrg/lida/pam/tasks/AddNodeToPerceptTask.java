package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

public class AddNodeToPerceptTask extends FrameworkTaskImpl {
	
	private PamNode node;
	private PerceptualAssociativeMemory pam;

	public AddNodeToPerceptTask(PamNode pamNode, PerceptualAssociativeMemory pam) {
		node = pamNode;
		this.pam = pam;
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	@Override
	protected void runThisFrameworkTask() {		
		pam.addNodeToPercept(node);
		setTaskStatus(TaskStatus.FINISHED);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return AddNodeToPerceptTask.class.getSimpleName() + " " + getTaskId();
	}

}
