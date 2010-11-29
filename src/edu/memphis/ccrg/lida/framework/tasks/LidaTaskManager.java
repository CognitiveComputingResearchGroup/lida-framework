/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;

//TODO: Comment!!!
/**
 * All tasks in the Lida system are created, executed, and managed by this
 * class. 
 * @author Javier Snaider
 * 
 */
public class LidaTaskManager {

	private static Logger logger = Logger.getLogger(LidaTaskManager.class.getCanonicalName());

	/**
	 * Determines whether or not spawned tasks should run
	 */
	private volatile boolean tasksPaused = true;
	
	/**
	 * Whether or not this task manager is shutting down its tasks
	 */
	private volatile boolean shuttingDown = false;
	
	private volatile long lapTicks = 0L;
	private volatile static long actualTick = 0L;
	private volatile Long maxTick = 0L;
	private volatile long lastDecayTick = 0L;
	private volatile boolean lapMode = false;
	private volatile Object lock = new Object();

	private ConcurrentMap<Long, Queue<LidaTask>> taskQueue;
	/**
	 * length of time for 1 tick in milliseconds.  The actual time which the tick unit is pegged to
	 */
	private int tickDuration = 1;

	/**
	 * Service used to execute the tasks
	 */
	private ExecutorService executorService;
	
	/**
	 * All tasks in the Lida system are created, executed, and managed by this
	 * class. This variable is to be used to get unique ids for each task.
	 */
	private static long nextTaskID = 0L;

	private TaskSpawner mainTaskSpawner;

	private Thread taskManagerThread;

	/**
	 * List of the Lida Modules managed by this class
	 */
	private Collection<LidaModule> modules = new HashSet<LidaModule>();

	/**
	 * 
	 * @param tickDuration - length of time of 1 tick in milliseconds
	 * @param maxPoolSize - max number of threads used by the ExecutorService
	 */
	public LidaTaskManager(int tickDuration, int maxPoolSize) {
		//TODO is this important below?
		// O ticks per step - Task manager should not be run
		// Null, LIDA_TASK_MANAGER should not have a LIDA_TASK_MANAGER
		int corePoolSize = 50;
		long keepAliveTime = 10;
		this.tickDuration = tickDuration;
		taskQueue = new ConcurrentHashMap<Long, Queue<LidaTask>>();
		executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, 
												  TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

		taskManagerThread = new Thread(new TaskManagerMainLoop());
		taskManagerThread.start();
	}
	

	/**
	 * @return the actualTick
	 */
	public static long getActualTick() {
		return actualTick;
	}
	
	/**
	 * @return the maxTick
	 */
	public Long getMaxTick() {
		return maxTick;
	}
	
	/**
	 * @return the totalTicks
	 */
	public long getLapTicks() {
		return lapTicks;
	}
	
	/**
	 * @return the mainTaskSpawner
	 */
	public TaskSpawner getMainTaskSpawner() {
		return mainTaskSpawner;
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
	
	public Map<Long, Queue<LidaTask>> getTaskQueue() {
		return Collections.unmodifiableMap(taskQueue);
	}

	public Collection<LidaTask> getSpawnedTasks() {
		return mainTaskSpawner.getSpawnedTasks();
	}

	/**
	 * Method shuts down all tasks, the executor service, waits and exits
	 */
	public void stopRunning() {
		shuttingDown = true;
		taskManagerThread.interrupt();
		// Now that we can be sure that active tasks will no longer be executed
		// the executor service can be shutdown.
		executorService.shutdown();
		logger.log(Level.INFO, "All threads and tasks told to stop",
				getActualTick());
		try {
			Thread.sleep(400);
			executorService.awaitTermination(400, TimeUnit.MILLISECONDS);
			executorService.shutdownNow();
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.log(Level.INFO, "Exiting", getActualTick());
		System.exit(0);
	}

	@Override
	public String toString() {
		return "LidaTaskManager";
	}

	public boolean isSystemPaused() {
		return tasksPaused;
	}
	/**
	 * @return the tasksPaused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}

	public void pauseSpawnedTasks() {
		logger.log(Level.INFO, "All Tasks paused.", getActualTick());
		tasksPaused = true;
	}

	public void resumeSpawnedTasks() {
		logger.log(Level.FINE,
				"resume spawned tasks called actualTime: {0} maxTick: {1}",
				new Object[] { actualTick, maxTick });
		if (shuttingDown)
			return;

		tasksPaused = false;

		synchronized (lock) {
			lock.notify();
		}
	}// method

	public void setLapTicks(long l) {
		lapTicks = l;
		synchronized (lock) {
			lock.notify();
		}
	}

	public boolean scheduleTask(LidaTask task, long delayTicks) {
		if (delayTicks <= 0) 
			return false;
		
		Long time = actualTick + delayTicks;
		Queue<LidaTask> queue = taskQueue.get(time);
		if (queue == null) {
			Queue<LidaTask> queue2 = new ConcurrentLinkedQueue<LidaTask>();
			queue = taskQueue.putIfAbsent(time, queue2);
			if (queue == null) {// there wasn't a Queue already at key 'time'
				queue = queue2;
				synchronized (maxTick) {
					if (time > maxTick) {
						maxTick = time;
						synchronized (lock) {
							lock.notify();
						}
					}
				}
			}
		}
		task.setScheduledTick(time);
		queue.add(task);
		return true;
	}

	public long goNextTick() {
		Queue<LidaTask> queue = taskQueue.get(++actualTick);
		taskQueue.remove(actualTick);
		logger.log(Level.FINER, "Tick {0} executed", actualTick);
		if (queue != null) {
			try {
				decayModules();
				executorService.invokeAll(queue); // Execute all tasks scheduled for this tick
			} catch (InterruptedException e) {
				logger.log(Level.WARNING, e.getMessage(), actualTick);
			}
		}
		return actualTick;
	}

	private void decayModules() {
		List<DecaybleWrapper> decaybles = new ArrayList<DecaybleWrapper>();
		long ticks=actualTick - lastDecayTick;
		for (LidaModule lm : modules) {
			decaybles.add(new DecaybleWrapper(lm,ticks));
		}
		
		try {
			executorService.invokeAll(decaybles);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, e.getMessage(), actualTick);
		}
		lastDecayTick = actualTick;
		logger.log(Level.FINEST, "Modules decayed", actualTick);
	}

	/**
	 * This inner class implements the main loop of the system.
	 * 
	 */
	private class TaskManagerMainLoop implements Runnable {

		@Override
		public void run() {
			while (!shuttingDown) {
				synchronized (lock) {
					if ((actualTick >= maxTick)
							|| (lapMode && (actualTick >= lapTicks))
							|| tasksPaused) {
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

			}//while
		}//method
		
	}//private sclass

	/**
	 * Cancels the task from the Task Queue. This is only possible if the tick
	 * for witch the task is scheduled has not been reached.
	 * 
	 * @param task
	 *            The task to cancel.
	 * @return true if it was , false otherwise.
	 */
	public boolean cancelTask(LidaTask task) {
		long time = task.getScheduledTick();
		Queue<LidaTask> queue = null;
		if (time > actualTick) {
			queue = taskQueue.get(time);
		}
		if (queue != null) {
			queue.remove(task);
			return true;
		}
		return false;
	}

	private class DecaybleWrapper implements Callable<Void> {
		private LidaModule module;
		private long ticks;

		public DecaybleWrapper(LidaModule module,long ticks) {
			this.module = module;
			this.ticks=ticks;
		}

		@SuppressWarnings("unused")
		public LidaModule getModule() {
			return module;
		}
		
		@SuppressWarnings("unused")
		public void setTicks(long ticks){
			this.ticks=ticks;
		}

		@Override
		public Void call() throws Exception {
			if (module!=null){
				module.decayModule(ticks);
			}
			return null;
		}
	}//private class
	
	/**
	 * Set the Collection of modules for decaying
	 * 
	 * @param modules
	 *            a Collection with the LidaModules
	 */
	public void setDecayingModules(Collection<LidaModule> modules) {
		this.modules.addAll(modules);
	}

}// class LIDA_TASK_MANAGER