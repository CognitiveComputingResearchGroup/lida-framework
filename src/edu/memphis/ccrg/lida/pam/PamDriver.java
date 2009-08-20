package edu.memphis.ccrg.lida.pam;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class PamDriver extends ModuleDriverImpl{

	private PerceptualAssociativeMemory pam;
	//private Logger logger = Logger.getLogger("lida.pam.PamDriver");
	
	public PamDriver(PerceptualAssociativeMemory pam, LidaTaskManager taskManager, int ticksPerCycle){
		super(taskManager, ticksPerCycle);
		this.pam = pam;
	}//constructor
		
	public void runSingleProcessingStep() {				
		if (pam instanceof PerceptualAssociativeMemoryImpl){
			((PerceptualAssociativeMemoryImpl)pam).sendEvent();
		}
		pam.decayPam();  //Decay the activations			
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void receiveFinishedTask(LidaTask task, Throwable t) {
		super.receiveFinishedTask(task, t);
		if(task.getStatus() == LidaTask.FINISHED && task instanceof Future){
			try{
				List<Object> results = (List<Object>) ((Future) task).get();
				Set<PamNode> nodes = (Set<PamNode>) results.get(ExcitationTask.nodesIndex);
				Double amount = (Double) results.get(ExcitationTask.amountIndex);
				pam.receiveActivationBurst(nodes, amount);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}//if
	}//method
	
}//class 