package edu.memphis.ccrg.lida.pam;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
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
	
	private static Logger logger = Logger.getLogger("lida.pam.ExcitationTask");
	
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
	 * @param excitationTaskTicksPerRun 
	 * @param pam
	 * @param ts
	 */
	public ExcitationTask(PamLinkable linkable, double excitation,
			              int excitationTaskTicksPerRun, 
			              PerceptualAssociativeMemory pam, 
			              TaskSpawner ts) {
		super();
		pamLinkable = linkable;
		excitationAmount = excitation;
		setNumberOfTicksPerRun(excitationTaskTicksPerRun);
		this.pam = pam;
		taskSpawner = ts;
	}

	/**
	 * 
	 */
	protected void runThisLidaTask() {
		pamLinkable.excite(excitationAmount); 
		//TODO create an ExcitationTask for both PamLink and PamNode?
		if(pamLinkable.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			AddToPerceptTask task;
			
			if(pamLinkable instanceof PamNode){
				PamNode pamNode = (PamNode) pamLinkable;
				task = new AddToPerceptTask(pamNode, pam);
				taskSpawner.addTask(task);
				//Tell PAM to propagate the activation of pamNode to its parents
				pam.sendActivationToParents(pamNode);
			}else if(pamLinkable instanceof PamLink){
				task = new AddToPerceptTask((PamLink) pamLinkable, pam);
				taskSpawner.addTask(task);
			}else{
				logger.log(Level.WARNING, "pam linkable is not a PamNode or PamLink", LidaTaskManager.getActualTick());
			}
		}else if(pamLinkable instanceof PamNode){
			//TODO what if its an instanceof PamLink?
			pam.sendActivationToParents((PamNode) pamLinkable);
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