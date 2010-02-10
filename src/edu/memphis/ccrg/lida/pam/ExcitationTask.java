package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * A task that allows PAM nodes to be excited asynchronously.
 * Created in PAM method 'receiveActivationBurst'
 * 
 * @author Ryan J McCall
 *
 */
public class ExcitationTask extends LidaTaskImpl{
	
	/**
	 * PamNode to be excited
	 */
	private PamNode pamNode;
	
	/**
	 * Amount to excite
	 */
	private double excitationAmount;
	
	/**
	 * Used to make another excitation call
	 */
	private PerceptualAssociativeMemory pam;
	
	/**
	 * For threshold task creation
	 */
	private TaskSpawner taskSpawner;

	/**
	 * 
	 * @param node
	 * @param excitation
	 * @param pam
	 * @param ts
	 */
	public ExcitationTask(PamNode node, double excitation,
			              PerceptualAssociativeMemory pam, 
			              TaskSpawner ts) {
		super();
		pamNode = node;
		excitationAmount = excitation;
		this.pam = pam;
		taskSpawner = ts;
	}

	/**
	 * 
	 */
	protected void runThisLidaTask() {
		pamNode.excite(excitationAmount);  
		if(pamNode.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			AddToPerceptTask task = new AddToPerceptTask(pamNode, pam);
			taskSpawner.addTask(task);
		}
		//Tell PAM to propagate the activation of pamNode to its parents
		pam.sendActivationToParentsOf(pamNode);
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}//method

	/**
	 * 
	 */
	public String toString(){
		return "Excitation " + getTaskId();
	}

}//class