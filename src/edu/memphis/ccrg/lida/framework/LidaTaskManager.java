package edu.memphis.ccrg.lida.framework;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.pam.ExcitationTask;
import edu.memphis.ccrg.lida.pam.PamNode;

public class LidaTaskManager extends TaskSpawnerImpl {
	
	private static Logger logger = Logger.getLogger("lida.framework.LidaTaskManager");
	
	/**
	 * The length of time that 1 tick equals in milliseconds.
	 */
	private int tickDuration = 1;
	
	/**
	 * All tasks in the Lida system are created, executed, and managed by this class.  
	 * This variable is to be used to get unique ids for each task.
	 */
	private static long nextTaskID = 0L;
	
	/**
	 * A boolean to track whether or not the system is in ticks mode.
	 */
	private static boolean inTicksMode = false;
	
	/**
	 * 
	 * @param tasksStartOutRunning
	 * @param tickDuration
	 */
	public LidaTaskManager(int tickDuration) {
		//O ticks per step - Task manager should not be run
		//Null, LIDA_TASK_MANAGER should not have a LIDA_TASK_MANAGER
		super(0, null);	
		this.tickDuration = tickDuration;
	}
	
	/**
	 * Convenience method to obtain the next ID for LidaTasks
	 * 
	 * @return nextThreadID
	 */
	public static long getNextTaskID() {
		long currentID = nextTaskID;
		nextTaskID++;
		return currentID;
	}

	/**
	 * The ticksMode permits to run the framework in a step by step fashion.
	 * Modules have cycles rates between them. For example, sensory memory could
	 * work ten times faster than PAM so one tick means one cycle for PAM and
	 * ten for Sensory Memory. In order to have the framework running accurately
	 * the relative speed of each part of the framework must be set.
	 */
	public synchronized void setInTicksMode(boolean mode) {
		inTicksMode = mode;
	}
	public static boolean isInTicksMode() {
		return inTicksMode;
	} 

	public boolean isSystemPaused(){
		return super.isTasksPaused();
	}

	/**
	 * Threads should call this in every iteration of their cycle so that the
	 * system is pausable.
	 */
	public synchronized void checkForStartPause() {
		if(isTasksPaused()){
			try{
				this.wait();
			}catch(InterruptedException e){
				stopRunning();
			}
		}//if
	}// method

	/**
	 * Threads call this to get the standard sleep time. This way the system
	 * operates roughly at the same speed across threads.
	 * 
	 * @return how long to sleep
	 */
	public int getTickDuration() {
		return tickDuration;
	}
	public synchronized void setTickDuration(int newTickDuration) {
		tickDuration = newTickDuration;
	}
	
	/**
	 * 
	 */
	@Override
	public void stopRunning(){
		super.stopRunning();
		logger.info("All threads and tasks told to stop\n");
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Exiting\n");
		System.exit(0);
	}

	/**
	 * Since it is a LidaTask, this class inherits, 
	 * but should not use or implement, this method.
	 */
	protected void runThisLidaTask() {
		// Not applicable
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void processResults(LidaTask task) {
		try {
			List<Object> results = (List<Object>) ((Future) task).get();
			Set<PamNode> nodes = (Set<PamNode>) results.get(ExcitationTask.nodesIndex);
			Double amount = (Double) results.get(ExcitationTask.amountIndex);
	//		pam.receiveActivationBurst(nodes, amount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}//method

	@Override
	public String toString() {
		return "LidaTaskManager";
	}

}// class LIDA_TASK_MANAGER