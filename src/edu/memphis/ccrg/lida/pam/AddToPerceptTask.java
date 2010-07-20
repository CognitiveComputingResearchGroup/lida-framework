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
 * @see ExcitationTask - AddToPerceptTask is spawned by ExcitationTask
 */
public class AddToPerceptTask extends LidaTaskImpl {
	
	private NodeStructure nodeStructure;
	private PerceptualAssociativeMemory pam;

	public AddToPerceptTask(PamNode node, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		nodeStructure = new NodeStructureImpl();
		nodeStructure.addNode(node);
	}
	
	public AddToPerceptTask(PamLink link, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		nodeStructure = new NodeStructureImpl();
		nodeStructure.addLink(link);
	//	System.out.println("constructing add to percept for link " + link.getLabel());
	}

	public AddToPerceptTask(NodeStructure ns, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		nodeStructure = ns;
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
		return "AddToPerceptTask " + getTaskId();
	}

}//class
