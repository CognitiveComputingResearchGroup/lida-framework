package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.AllModuleDriver;
//TODO: Comment!!!
public class LidaTaskManager {

	private static Logger logger = Logger.getLogger("lida.framework.LidaTaskManager");

	/**
	 * Determines whether or not spawned task should run
	 */
	private volatile boolean tasksPaused = true;
	private volatile boolean shuttingDown = false;
	private volatile long lapTicks = 0L;
	private volatile long actualTick = 0L;
	private volatile Long maxTime = 0L;
	private volatile boolean lapMode = false;
	private Object lock = new Object();

	
	/**
	 * @return the actualTick
	 */
	public long getActualTick() {
		return actualTick;
	}


	/**
	 * @return the lapMode
	 */
	public boolean isLapMode() {
		return lapMode;
	}

	/**
	 * @param lapMode
	 *            the lapMode to set
	 */
	public void setLapMode(boolean lapMode) {
		this.lapMode = lapMode;
	}

	private ConcurrentMap<Long, Queue<LidaTask>> taskQueue;
	/**
	 * The length of time that 1 tick equals in milliseconds.
	 */
	private int tickDuration = 1;

	/**
	 * @return the totalTicks
	 */
	public long getLapTicks() {
		return lapTicks;
	}

	private ExecutorService executorService;
	/**
	 * All tasks in the Lida system are created, executed, and managed by this
	 * class. This variable is to be used to get unique ids for each task.
	 */
	private static long nextTaskID = 0L;

	private TaskSpawner mainTaskSpawner;

	private Thread taskManagerThread;

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
		// O ticks per step - Task manager should not be run
		// Null, LIDA_TASK_MANAGER should not have a LIDA_TASK_MANAGER
		int corePoolSize = 10;
		long keepAliveTime = 10;
		this.tickDuration = tickDuration;
		taskQueue = new ConcurrentHashMap<Long, Queue<LidaTask>>();
		executorService = new LidaExecutorService(corePoolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS, this);

		mainTaskSpawner = new AllModuleDriver(this);

		taskManagerThread = new Thread(new TaskManagerMainLoop());
		taskManagerThread.start();

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

	// /**
	// * The ticksMode permits to run the framework in a step by step fashion.
	// * Modules have cycles rates between them. For example, sensory memory
	// could
	// * work ten times faster than PAM so one tick means one cycle for PAM and
	// * ten for Sensory Memory. In order to have the framework running
	// accurately
	// * the relative speed of each part of the framework must be set.
	// */
	// public synchronized void setInTicksMode(boolean mode) {
	// inTicksMode = mode;
	// }
	// public static boolean isInTicksMode() {
	// return inTicksMode;
	// }

	public boolean isSystemPaused() {
		return tasksPaused;
	}

	// /**
	// * Threads should call this in every iteration of their cycle so that the
	// * system is pausable.
	// */
	// public synchronized void checkForStartPause() {
	// if(isTasksPaused()){
	// try{
	// this.wait();
	// }catch(InterruptedException e){
	// stopRunning();
	// }
	// }//if
	// }// method

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
	public void stopRunning() {
		shuttingDown = true;
		mainTaskSpawner.stopRunning();
		
		taskManagerThread.interrupt();
		// Now that we can be sure that active tasks will no longer be executed
		// the executor service can be shutdown.
		executorService.shutdown();

		logger.info("All threads and tasks told to stop\n");
		try {
			Thread.sleep(400);
			executorService.awaitTermination(400, TimeUnit.MILLISECONDS);
			executorService.shutdownNow();
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
	}

	public void resumeSpawnedTasks() {
		logger.log(Level.FINE, "resume spawned tasks called actualTime: "+actualTick+" maxTime: "+maxTime);
		if (shuttingDown)
			return;

		tasksPaused = false;

		synchronized(lock){
			lock.notify();
		}
	}// method

	public void setLapTicks(long l) {
		lapTicks = l;
		synchronized(lock){
			lock.notify();
		}
	}

	public Collection<LidaTask> getSpawnedTasks() {
		return mainTaskSpawner.getSpawnedTasks();
	}

	public boolean scheduleTask(LidaTask task, long delayTicks) {
		if (delayTicks <= 0) {
			return false;
		}
		Long time = actualTick + delayTicks;
		Queue<LidaTask> queue = taskQueue.get(time);
		if (queue == null) {
			queue = new ConcurrentLinkedQueue<LidaTask>();
			Queue<LidaTask> queue2 = taskQueue.putIfAbsent(time, queue);
			if (queue2 != null) {// there was a previous one
				queue = queue2;
			} else {
				synchronized (maxTime) {
					if (time > maxTime) {
						maxTime = time;
						synchronized(lock){
							lock.notify();
						}
					}
				}
			}
		}
		queue.add(task);
		task.setScheduledTick(time);
		return true;
	}

	public long goNextTick() {
		Queue<LidaTask> queue;

		queue = taskQueue.get(++actualTick);
		taskQueue.remove(actualTick);
		logger.log(Level.FINER, "Tick {0} executed", actualTick);
		if (queue != null) {
			try {
				executorService.invokeAll(queue);
			} catch (InterruptedException e) {
				logger.fine(e.getMessage());
			}
		}
		return actualTick;
	}

	public void receiveFinishedTask(LidaTask task, Throwable t) {
		
		TaskSpawner ts=task.getTaskSpawner();
		if (ts !=null){
			ts.receiveFinishedTask(task, t);
		}else{
			logger.log(Level.FINEST, "Taskmanager is deleting task {0}", task);
		}
	}

	public Map<Long, Queue<LidaTask>> getTaskQueue(){
		return Collections.unmodifiableMap(taskQueue);
	}
	private class TaskManagerMainLoop implements Runnable {

		public void run() {
			while (!shuttingDown) {
				synchronized (lock) {
					if ((actualTick >= maxTime)
							|| (lapMode && (actualTick >= lapTicks)) || tasksPaused) {
						try {
							lock.wait();
							continue;
						} catch (InterruptedException e) {
							return;
						}
					}
				}
				
				long initTime = System.currentTimeMillis(); // For real time 
				goNextTick();
				
				long duration = System.currentTimeMillis() - initTime;
				if (duration < tickDuration) {
					try {
						Thread.sleep(tickDuration - duration);
					} catch (InterruptedException e) {
						return;
					}
				}

			}
		}
	}
	
	/**
	 * Cancels the task from the Task Queue. This is only possible if the tick for witch the task 
	 * is scheduled has not been reached.
	 * 
	 * @param task The task to cancel.
	 * @return true if it was , false otherwise. 
	 */
	public boolean cancelTask(LidaTask task){
		long time = task.getScheduledTick();
		Queue<LidaTask> queue =null;
		if (time>actualTick){
			queue=taskQueue.get(time);			
		}
		if (queue!=null){
			queue.remove(task);
			return true;
		}
		return false;
	}
}// class LIDA_TASK_MANAGER