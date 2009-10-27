package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.AllModuleDriver;

public class LidaTaskManager{
	
	private static Logger logger = Logger.getLogger("lida.framework.LidaTaskManager");

	/**
	 * Determines whether or not spawned task should run
	 */
	private boolean tasksPaused = true;
	private boolean shuttingDown = false;
	private long totalTicks=0L;

	
	/**
	 * @return the totalTicks
	 */
	public long getTotalTicks() {
		return totalTicks;
	}

	/**
	 * The length of time that 1 tick equals in milliseconds.
	 */
	private int tickDuration = 1;
	private ExecutorService executorService;
	/**
	 * All tasks in the Lida system are created, executed, and managed by this class.  
	 * This variable is to be used to get unique ids for each task.
	 */
	private static long nextTaskID = 0L;
	
	/**
	 * A boolean to track whether or not the system is in ticks mode.
	 */
	private static boolean inTicksMode = false;
	
	private TaskSpawner mainTaskSpawner;
	
	/**
	 * @return the mainTaskSpawner
	 */
	public TaskSpawner getMainTaskSpawner() {
		return mainTaskSpawner;
	}


	/**
	 * 
	 * @param tasksStartOutRunning
	 * @param tickDuration
	 */
	public LidaTaskManager(int tickDuration, int maxPoolSize) {
		//O ticks per step - Task manager should not be run
		//Null, LIDA_TASK_MANAGER should not have a LIDA_TASK_MANAGER
		int corePoolSize = 5;
		long keepAliveTime = 10;
		this.tickDuration = tickDuration;
		executorService = new LidaExecutorService( corePoolSize,
				maxPoolSize, keepAliveTime, TimeUnit.SECONDS);
		
		mainTaskSpawner= new AllModuleDriver(this);
	}
	
	
	/**
	 * @return the executorService
	 */
	public ExecutorService getExecutorService() {
		return executorService;
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

//	public boolean isSystemPaused(){
//		return super.isTasksPaused();
//	}

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
	public void stopRunning(){
		shuttingDown=true;
		mainTaskSpawner.stopRunning();
		
		// Now that we can be sure that active tasks will no longer be executed
		// the executor service can be shutdown.
		executorService.shutdown();

		logger.info("All threads and tasks told to stop\n");
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Exiting\n");
		System.exit(0);
	}

	@Override
	public String toString() {
		return "LidaTaskManager";
	}
	/**
	 * @return the tasksPaused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}
	public void pauseSpawnedTasks() {
		logger.log(Level.FINE, "All Tasks paused.");

		tasksPaused = true;
		mainTaskSpawner.pauseSpawnedTasks();
	}
	
	public void resumeSpawnedTasks() {
		logger.log(Level.FINE, "resume spawned tasks called");
		if (shuttingDown)
			return;
		
		tasksPaused = false;

		mainTaskSpawner.resumeSpawnedTasks();
	}// method

	public void addTicks(int ticks){
		totalTicks=totalTicks + ticks;
		mainTaskSpawner.addTicks(ticks);
	}

	public Collection<LidaTask> getSpawnedTasks() {
		return mainTaskSpawner.getSpawnedTasks();
	}
}// class LIDA_TASK_MANAGER