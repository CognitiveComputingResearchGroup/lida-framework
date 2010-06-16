package edu.memphis.ccrg.lida.pam;

import java.util.List;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;

/**
 * A task to add a node to the percept.
 * @author ryanjmccall
 *
 */
public class AddToPerceptTask extends LidaTaskImpl {
	
	private NodeStructure nodeStructure;
	private PerceptualAssociativeMemory pam;

	public AddToPerceptTask(PamNode pamNode, PerceptualAssociativeMemory pam) {
		super();
		nodeStructure = new NodeStructureImpl();
		nodeStructure.addNode(pamNode);
		this.pam = pam;
	}

	public AddToPerceptTask(NodeStructure ns, PerceptualAssociativeMemory pam) {
		super();
		nodeStructure = ns;
		this.pam = pam;
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	public void runThisLidaTask() {
		pam.addNodeStructureToPercept(nodeStructure);
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}
	public String toString(){
		return "Threshold " + getTaskId();
	}

}//class
