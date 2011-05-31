package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task which adds a {@link PamNode} to the percept.
 * @author Ryan J McCall
 */
public class AddNodeToPerceptTask extends FrameworkTaskImpl {
	
	private PamNode node;
	private PerceptualAssociativeMemory pam;

	/**
	 * Default constructor
	 * @param pamNode {@link PamNode}
	 * @param pam {@link PerceptualAssociativeMemory}
	 */
	public AddNodeToPerceptTask(PamNode pamNode, PerceptualAssociativeMemory pam) {
		node = pamNode;
		this.pam = pam;
	}

	/**
	 * Adds {@link PamNode} to the percept then finishes.
	 */
	@Override
	protected void runThisFrameworkTask() {		
		pam.addNodeToPercept(node);
		setTaskStatus(TaskStatus.FINISHED);
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName() + " " + getTaskId();
	}

}
