package edu.memphis.ccrg.lida.framework;

public class LidaTaskManager extends TaskSpawnerImpl {
	
	/**
	 * The length of time that 1 tick equals in milliseconds.
	 */
	private static int tickDuration = 1;
	
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
	public LidaTaskManager(boolean tasksStartOutRunning, int tickDuration) {
		super(0);//Task manager should not be run
		if (tasksStartOutRunning)
			pauseSpawnedTasks();
		else
			resumeSpawnedTasks();
	
		LidaTaskManager.tickDuration = tickDuration;
	}
	
	/**
	 * Convenience method to obtain the next ID for LidaTasks
	 * 
	 * @return nextThreadID
	 */
	public static long getNextTaskID() {
		return nextTaskID++;
	}

	/**
	 * The ticksMode permits to run the framework in a step by step fashion.
	 * Modules have cycles rates between them. For example, sensory memory could
	 * work ten times faster than PAM so one tick means one cycle for PAM and
	 * ten for Sensory Memory. In order to have the framework running accurately
	 * the relative speed of each part of the framework must be set.
	 */
	public static void setInTicksMode(boolean mode) {
		inTicksMode = mode;
	}
	public static boolean isInTicksMode() {
		return inTicksMode;
	} 

	/**
	 * 
	 */
	public synchronized void resumeSpawnedTasks() {
		super.resumeSpawnedTasks();
		this.notifyAll();
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
	public static int getTickDuration() {
		return tickDuration;
	}
	public synchronized void setTickDuration(int newTickDuration) {
		tickDuration = newTickDuration;
	}

	/**
	 * Since it is a LidaTask, this class implements runnable but run is not used for the task manager.
	 */
	public void run(){}
	
	public void stopRunning(){
		super.stopRunning();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

}// class FrameworkTimer