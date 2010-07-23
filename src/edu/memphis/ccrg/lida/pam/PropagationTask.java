package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.LinkType;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * A propagation task excites a node and a link.  
 * The link connects the source of the activation (not accessible) to the node.
 * @author ryanjmccall
 *
 */
public class PropagationTask extends LidaTaskImpl {
	
	private PamLink link;
	private PamNode source, sink;
	private double excitationAmount;
	
	/**
	 * Used to make another excitation call
	 */
	private PerceptualAssociativeMemory pam;
	
	/**
	 * For threshold task creation
	 */
	private TaskSpawner taskSpawner;

	public PropagationTask(PamNode source, PamLink link, PamNode sink, double amount,
						   PerceptualAssociativeMemory pam, TaskSpawner taskSpawner) {
		super();
		this.source = source;
		this.link = link;
		this.sink = sink;
		this.excitationAmount = amount;
		this.pam = pam;
		this.taskSpawner = taskSpawner;		
	}

	@Override
	protected void runThisLidaTask() {
		link.excite(excitationAmount);
		sink.excite(excitationAmount);
		//
		pam.sendActivationToParentsOf(sink);
		if(link.isOverThreshold() && sink.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			NodeStructure ns = new NodeStructureImpl();
			ns.addNode(source);
			ns.addNode(sink);
			ns.addLink(link);
			AddToPerceptTask task = new AddToPerceptTask(ns, pam);
			taskSpawner.addTask(task);
		}
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}
	
	/**
	 * 
	 */
	public String toString(){
		return "Propagation " + getTaskId();
	}

}//class
