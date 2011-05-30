package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

public class AddNodeStructureToPerceptTask extends FrameworkTaskImpl {

	private NodeStructure ns;
	private PerceptualAssociativeMemory pam;

	public AddNodeStructureToPerceptTask(NodeStructure ns, PerceptualAssociativeMemory pam) {
		this.ns = ns;
		this.pam = pam;
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	@Override
	protected void runThisFrameworkTask() {		
		pam.addNodeStructureToPercept(ns);
		setTaskStatus(TaskStatus.FINISHED);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return AddNodeStructureToPerceptTask.class.getSimpleName() + " " + getTaskId();
	}

}
