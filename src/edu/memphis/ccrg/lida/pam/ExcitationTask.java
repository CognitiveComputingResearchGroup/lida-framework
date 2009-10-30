package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.TaskSpawner;

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
	 * 
	 */
	private PerceptualAssociativeMemory pam;
	
	/**
	 * For Task control
	 */
	private LidaTaskManager taskManager;
	
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
	 * @param tm
	 */
	public ExcitationTask(PamNode node, double excitation,
			              PerceptualAssociativeMemory pam, 
			              TaskSpawner ts, 
			              LidaTaskManager tm) {
		super(tm);
		pamNode = node;
		excitationAmount = excitation;
		this.pam = pam;
		taskSpawner = ts;
		taskManager = tm;
	}

	/**
	 * 
	 */
	protected void runThisLidaTask() {
		pamNode.excite(excitationAmount);  
		if(pamNode.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			AddToPerceptTask task = new AddToPerceptTask(pamNode, pam, taskManager);
			taskSpawner.addTask(task);
		}
		//Tell PAM to propagate the activation of pamNode to its parents
		pam.sendActivationToParentsOf(pamNode);
		this.setTaskStatus(LidaTask.FINISHED);
	}//method

	/**
	 * 
	 */
	public String toString(){
		return "Excitation " + getTaskId();
	}

}//class