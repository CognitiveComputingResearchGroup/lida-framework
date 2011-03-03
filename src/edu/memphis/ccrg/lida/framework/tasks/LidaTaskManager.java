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

/**
 * All tasks in the Lida system are created, executed, and managed by this
 * class.  Controls the decay of all the LidaModules in Lida.  Keeps track of the current
 * tick, the unit of time in the application.  Maintains a task queue where
 * each position represents the time (in ticks) when a task will be run.  Multiple tasks
 * can be scheduled for the same tick.  Uses an ExecutorService to obtain the
 * threads to run all the tasks scheduled in one tick concurrently.
 * 
 * @author Javier Snaider
 */
public class LidaTaskManager {

	private static final Logger logger = Logger.getLogger(LidaTaskManager.class.getCanonicalName());

	/**
	 * Determines whether or not spawned tasks should run
	 */
	private volatile boolean tasksPaused = true;
	
	/**
	 * Whether or not this task manager is shutting down its tasks
	 */
	private volatile boolean shuttingDown = false;
	
	private volatile long endOfNextInterval = 0L;
	private volatile static long currentTick = 0L;
	private volatile Long maxTick = 0L;
	private volatile long lastDecayTick = 0L;
	private volatile boolean inIntervalMode = false;
	private volatile Object lock = new Object();

	private ConcurrentMap<Long, Queue<LidaTask>> taskQueue;
	/**
	 * Length of time of 1 tick in milliseconds.  The actual time thats the tick unit represents.
	 * In practice tickDuration affects the speed of tasks in the simulation.
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

	/**
	 * Main thread of the system.
	 */
	private Thread taskManagerThread;

	/**
	 * List of the LidaModules managed by this class
	 */
	private Collection<LidaModule> modules = new HashSet<LidaModule>();

	/**
	 * 
	 * @param tickDuration - length of time of 1 tick in milliseconds
	 * @param maxPoolSize - max number of threads used by the ExecutorService
	 */
	public LidaTaskManager(int tickDuration, int maxPoolSize) {
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
	 * Current tick in the system.  Tasks scheduled for this tick have been executed or they are being
	 * executed.
	 * @return current tick
	 */
	public static long getCurrentTick() {
		return currentTick;
	}
	
	/**
	 * Returns max tick
	 * @return the farthest tick in the future that has a scheduled task. in other words, the highest
	 * tick position in the task queue that has scheduled task(s).
	 */
	public Long getMaxTick() {
		return maxTick;
	}
	
	/**
	 * Returns endOfNextInterval
	 * @return the absolute tick when the current execution interval ends
	 */
	public long getEndOfNextInterval() {
		return endOfNextInterval;
	}

	/**
	 * Returns the next unique ID for LidaTasks.
	 * @return unique id
	 */
	public static long getNextTaskID() {
		long currentID = nextTaskID;
		nextTaskID++;
		return currentID;
	}

	/**
	 * Sets tickDuration
	 * @param newTickDuration set a new tick duration, the length of time of 1 tick in milliseconds.
	 * The actual time that the tick unit represents.
	 * In practice tickDuration affects the speed of tasks in the simulation.
	 */
	public synchronized void setTickDuration(int newTickDuration) {
		tickDuration = newTickDuration;
	}
	
	public int getTickDuration(){
		return tickDuration;
	}

	/**
	 * @return true if system is in interval mode
	 */
	public boolean isInIntervalMode() {
		return inIntervalMode;
	}

	/**
	 * Sets inIntervalMode
	 * @param inIntervalMode true to set the system to interval mode, false to exit.
	 */
	public void setInIntervalMode(boolean inIntervalMode) {
		this.inIntervalMode = inIntervalMode;
	}
	
	/**
	 * @return UnmodifiableMap of the task queue 
	 */
	public Map<Long, Queue<LidaTask>> getTaskQueue() {
		return Collections.unmodifiableMap(taskQueue);
	}

	/**
	 * Method shuts down all tasks, the executor service, waits, and exits.
	 */
	public void stopRunning() {
		shuttingDown = true;
		taskManagerThread.interrupt();
		// Now that we can be sure that active tasks will no longer be executed
		// the executor service can be shutdown.
		executorService.shutdown();
		logger.log(Level.INFO, "All threads and tasks told to stop",
				getCurrentTick());
		try {
			executorService.awaitTermination(800, TimeUnit.MILLISECONDS);			
			executorService.shutdownNow();
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.log(Level.INFO, "Exiting", getCurrentTick());
		System.exit(0);
	}

	@Override
	public String toString() {
		return "LidaTaskManager";
	}

	/**
	 * @return true if tasks are paused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}

	public void pauseTasks() {
		logger.log(Level.INFO, "All Tasks paused.", getCurrentTick());
		tasksPaused = true;
	}

	public void resumeTasks() {
		logger.log(Level.FINE,
				"resume tasks called actualTime: {0} maxTick: {1}",
				new Object[] { currentTick, maxTick });
		if (shuttingDown)
			return;

		tasksPaused = false;

		synchronized (lock) {
			lock.notify();
		}
	}

	public void addTicksToExecute(long ticks) {
		endOfNextInterval = ticks + currentTick;
		synchronized (lock) {
			lock.notify();
		}
	}

	public boolean scheduleTask(LidaTask task, long inXTicks) {
		if (inXTicks <= 0) 
			return false;
		
		Long time = currentTick + inXTicks;
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

	private long goNextTick() {
		//TODO optimize this method to skip ticks until the next tick with scheduled tasks is found
		Queue<LidaTask> queue = taskQueue.get(++currentTick);
		taskQueue.remove(currentTick);
		logger.log(Level.FINER, "Tick {0} executed", currentTick);
		if (queue != null) {
			try {
				decayModules();
				executorService.invokeAll(queue); // Execute all tasks scheduled for this tick
			} catch (InterruptedException e) {
				if(!shuttingDown){
					logger.log(Level.WARNING, 
						    "Current tick " + currentTick + " was interrupted because of " + e.toString(), 
						    currentTick);
				}else{
					logger.log(Level.INFO, 
							"Current tick " + currentTick + " interrupted for application shutdown.", 
							currentTick);
				}
			}
		}
		return currentTick;
	}

	private void decayModules() {
		List<DecaybleWrapper> decaybles = new ArrayList<DecaybleWrapper>();
		long ticks=currentTick - lastDecayTick;
		for (LidaModule lm : modules) {
			decaybles.add(new DecaybleWrapper(lm,ticks));
		}
		
		try {
			executorService.invokeAll(decaybles);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, "Decaying interrupted. Message: " + e.getMessage(), currentTick);
		}
		lastDecayTick = currentTick;
		logger.log(Level.FINEST, "Modules decayed", currentTick);
	}

	/**
	 * This inner class implements the main loop of the system.
	 */
	private class TaskManagerMainLoop implements Runnable {

		@Override
		public void run() {
			while (!shuttingDown) {
				synchronized (lock) {
					if ((currentTick >= maxTick)
							|| (inIntervalMode && (currentTick >= endOfNextInterval))
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
		}
		
	}//class

	/**
	 * Cancels the task from the Task Queue. This is only possible if the tick
	 * for which the task is scheduled has not been reached.
	 * 
	 * @param task
	 *            The task to cancel.
	 * @return true if it was , false otherwise.
	 */
	public boolean cancelTask(LidaTask task) {
		long time = task.getScheduledTick();
		Queue<LidaTask> queue = null;
		if (time > currentTick) {
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

}