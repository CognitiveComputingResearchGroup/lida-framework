package edu.memphis.ccrg.lida.framework;

public class LidaTaskManager extends TaskSpawnerImpl {

	private static long nextTaskID = 0L;
	private static boolean ticksMode = false;

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
	public static void setTicksMode(boolean mode) {
		ticksMode = mode;
	}

	public static boolean isTicksMode() {
		return ticksMode;
	}

	/**
	 * true -> Start out paused false -> Start out running
	 */
	/**
	 * When paused, this is how long this thread will sleep before checking to
	 * see if pause was clicked again to start things back up.
	 */
	private int msUntilICheckForUnpause = 10;
	/**
	 * Threads calling the member function getSleepTime() will sleep for this
	 * many ms.
	 */
	private int threadSleepTime = 150;

	public LidaTaskManager(boolean startPaused, int threadSleepTime) {
		if (startPaused){
			pauseSpawnedTasks();
		}else{
			resumeSpawnedTasks();
		}
		this.threadSleepTime = threadSleepTime;
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
		if (isTasksArePaused()) {
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
	public int getSleepTime() {
		return threadSleepTime;
	}

	public synchronized void setSleepTime(int newTime) {
		threadSleepTime = newTime;
	}//

	/**
	 * Not used.
	 */
	public void run() {
		// Not used
	}

}// class FrameworkTimer