package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task which adds a {@link NodeStructure} to the percept.
 * @author Ryan J McCall
 */
public class AddNodeStructureToPerceptTask extends FrameworkTaskImpl {

	private NodeStructure ns;
	private PerceptualAssociativeMemory pam;

	/**
	 * Default constructor
	 * @param ns {@link NodeStructure}
	 * @param pam {@link PerceptualAssociativeMemory}
	 */
	public AddNodeStructureToPerceptTask(NodeStructure ns, PerceptualAssociativeMemory pam) {
		this.ns = ns;
		this.pam = pam;
	}

	/**
	 * Adds {@link NodeStructure} to the percept then finishes.
	 */
	@Override
	protected void runThisFrameworkTask() {		
		pam.addNodeStructureToPercept(ns);
		setTaskStatus(TaskStatus.FINISHED);
	}
}
