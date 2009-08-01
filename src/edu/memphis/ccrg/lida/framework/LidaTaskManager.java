package edu.memphis.ccrg.lida.framework;

public class LidaTaskManager extends TaskSpawnerImpl {

	private static long nextTaskID = 0L;
	private static boolean ticksMode = false;
	/**
	 * Threads calling the member function getSleepTime() will sleep for this
	 * many ms.
	 */
	private int timeScale = 150;
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
	public static void setTicksModeEnabled(boolean mode) {
		ticksMode = mode;
	}

	public static boolean isTicksModeEnabled() {
		return ticksMode;
	} 


	public LidaTaskManager(boolean startPaused, int timeScale) {
		if (startPaused){
			pauseSpawnedTasks();
		}else{
			resumeSpawnedTasks();
		}
		this.timeScale = timeScale;
	}

	public synchronized void resumeSpawnedTasks() {
		super.resumeSpawnedTasks();
		this.notifyAll();
	}

	/**
	 * Threads should call this in every iteration of their cycle so that the
	 * system is pausable.
	 */
	public synchronized void checkForStartPause() {
		if (tasksArePaused()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				stopRunning();
			}
		}
	}// method

	/**
	 * Threads call this to get the standard sleep time. This way the system
	 * operates roughly at the same speed across threads.
	 * 
	 * @return how long to sleep
	 */
	public int getTimeScale() {
		return timeScale;
	}

	public synchronized void setTimeScale(int newTimeScale) {
		timeScale = newTimeScale;
	}//

	/**
	 * Not used.
	 */
	public void run() {
		// Not used
	}

}// class FrameworkTimer