package edu.memphis.ccrg.lida.pam;

//import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;

/**
 * A task to add a node to the percept.
 * @author Ryan J McCall
 * @see ExcitationTask - AddToPerceptTask is spawned by ExcitationTask
 */
public class AddToPerceptTask extends LidaTaskImpl {
	
	private NodeStructure pamNodeStructure;
	private PerceptualAssociativeMemory pam;

	public AddToPerceptTask(PamNode node, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		pamNodeStructure = new NodeStructureImpl();
		pamNodeStructure.addNode(node);
	}
	
	public AddToPerceptTask(PamLink link, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		pamNodeStructure = new NodeStructureImpl();
		pamNodeStructure.addLink(link);
	}

	public AddToPerceptTask(NodeStructure ns, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		pamNodeStructure = new NodeStructureImpl(ns);
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	public void runThisLidaTask() {	
//		for(Node n: pamNodeStructure.getNodes()){
//			System.out.println(n.getLabel());
//		}
//		System.out.println("");
			
		pam.addNodeStructureToPercept(pamNodeStructure);
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}
	public String toString(){
		return "AddToPerceptTask " + getTaskId();
	}

}//class

