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
	private PamLinkable pamLinkable;
	
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
	 * @param linkable
	 * @param excitation
	 * @param pam
	 * @param ts
	 */
	public ExcitationTask(PamLinkable linkable, double excitation,
			              PerceptualAssociativeMemory pam, 
			              TaskSpawner ts) {
		super();
		pamLinkable = linkable;
		excitationAmount = excitation;
		this.pam = pam;
		taskSpawner = ts;
	}

	/**
	 * 
	 */
	protected void runThisLidaTask() {
		//System.out.println("Exciting " + pamLinkable.getLabel() + " amount " + excitationAmount);
		pamLinkable.excite(excitationAmount); 
		
		if(pamLinkable.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			AddToPerceptTask task;
			
			if(pamLinkable instanceof PamNode){
				PamNode pn = (PamNode) pamLinkable;
				task = new AddToPerceptTask(pn, pam);
				taskSpawner.addTask(task);
				//Tell PAM to propagate the activation of pamNode to its parents
				pam.sendActivationToParentsOf(pn);
			}else if(pamLinkable instanceof PamLink){
				task = new AddToPerceptTask((PamLink) pamLinkable, pam);
				taskSpawner.addTask(task);
			}else{
				//log an error
			}
		}else if(pamLinkable instanceof PamNode){
			pam.sendActivationToParentsOf((PamNode) pamLinkable);
		}
		
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}//method

	/**
	 * 
	 */
	public String toString(){
		return "Excitation " + getTaskId();
	}

}//class