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

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.FrameworkModule;

/**
 * All tasks in the {@link Agent} system are executed by this class. Controls the decay
 * of all the {@link FrameworkModule}s in {@link Agent}. Keeps track of the current
 * tick, the unit of time in the application. Maintains a task queue where each
 * position represents the time (in ticks) when a task will be executed.
 * Multiple tasks can be scheduled for the same tick. Uses an
 * {@link ExecutorService} to obtain the threads to run all the tasks scheduled
 * in one tick concurrently.
 * 
 * @author Javier Snaider
 */
public class TaskManager {

	private static final Logger logger = Logger.getLogger(TaskManager.class
			.getCanonicalName());

	/*
	 * Determines whether or not spawned tasks should run
	 */
	private volatile boolean tasksPaused = true;

	/*
	 * Whether or not this task manager is shutting down its tasks
	 */
	private volatile boolean shuttingDown = false;

	private volatile long endOfNextInterval = 0L;
	private volatile static long currentTick = 0L;
	private volatile Long maxTick = 0L;
	private volatile boolean inIntervalMode = false;
	private volatile Object lock = new Object();

	private ConcurrentMap<Long, Queue<FrameworkTask>> taskQueue;
	/**
	 * Length of time of 1 tick in milliseconds. The actual time thats the tick
	 * unit represents. In practice tickDuration affects the speed of tasks in
	 * the simulation.
	 */
	private int tickDuration = 1;

	/**
	 * Service used to execute the tasks
	 */
	private ExecutorService executorService;

	/**
	 * Main thread of the system.
	 */
	private Thread taskManagerThread;

	/**
	 * List of the FrameworkModules managed by this class
	 */
	private List<DecayableWrapper> decaybles = new ArrayList<DecayableWrapper>();

	/**
	 * 
	 * @param tickDuration
	 *            - length of time of 1 tick in milliseconds
	 * @param maxPoolSize
	 *            - max number of threads used by the ExecutorService
	 */
	public TaskManager(int tickDuration, int maxPoolSize) {
		int corePoolSize = 50;
		long keepAliveTime = 10;
		this.tickDuration = tickDuration;
		taskQueue = new ConcurrentHashMap<Long, Queue<FrameworkTask>>();
		executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());

		taskManagerThread = new Thread(new TaskManagerMainLoop());
		taskManagerThread.start();
	}

	/**
	 * Current tick in the system. Tasks scheduled for this tick have been
	 * executed or they are being executed.
	 * 
	 * @return current tick
	 */
	public static long getCurrentTick() {
		return currentTick;
	}

	/**
	 * Returns max tick
	 * 
	 * @return the farthest tick in the future that has a scheduled task. in
	 *         other words, the highest tick position in the task queue that has
	 *         scheduled task(s).
	 */
	public long getMaxTick() {
		return maxTick;
	}

	/**
	 * This attribute is used for interval execution mode. Returns
	 * endOfNextInterval
	 * 
	 * @return the absolute tick when the current execution interval ends
	 */
	public long getEndOfNextInterval() {
		return endOfNextInterval;
	}

	/**
	 * Sets tickDuration
	 * 
	 * @param newTickDuration
	 *            set a new tick duration, the length of time of 1 tick in
	 *            milliseconds. The actual time that the tick unit represents.
	 *            In practice tickDuration affects the speed of tasks in the
	 *            simulation.
	 */
	public synchronized void setTickDuration(int newTickDuration) {
		tickDuration = newTickDuration;
	}

	public int getTickDuration() {
		return tickDuration;
	}

	/**
	 * @return true if system is in interval execution mode
	 */
	public boolean isInIntervalMode() {
		return inIntervalMode;
	}

	/**
	 * Sets inIntervalMode
	 * 
	 * @param inIntervalMode
	 *            true to set the system to interval execution mode, false to
	 *            exit.
	 */
	public void setInIntervalMode(boolean inIntervalMode) {
		this.inIntervalMode = inIntervalMode;
	}

	/**
	 * @return UnmodifiableMap of the task queue
	 */
	public Map<Long, Queue<FrameworkTask>> getTaskQueue() {
		return Collections.unmodifiableMap(taskQueue);
	}

	/**
	 * @return true if tasks are paused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}

	/**
	 * Finish the executions of all tasks scheduled for the currentTick and
	 * pauses all further tasks executions.
	 */
	public void pauseTasks() {
		logger.log(Level.INFO, "All Tasks paused.", getCurrentTick());
		tasksPaused = true;
	}

	/**
	 * Resumes the execution of tasks in the queue.
	 */
	public void resumeTasks() {
		if (shuttingDown){
			return;
		}
		
		logger.log(Level.INFO,
				"resume tasks called actualTime: {0} maxTick: {1}",
				new Object[] { currentTick, maxTick });

		tasksPaused = false;

		synchronized (lock) {
			lock.notify();
		}
	}

	/**
	 * Cancels the task from the Task Queue. This is only possible if the tick
	 * for which the task is scheduled has not been reached.
	 * 
	 * @param task
	 *            The task to cancel.
	 * @return true if it was , false otherwise.
	 */
	public boolean cancelTask(FrameworkTask task) {
		if (task != null) {
			long time = task.getScheduledTick();
			if (time > currentTick) {
				Queue<FrameworkTask> queue = taskQueue.get(time);
				if (queue != null) {
					return queue.remove(task);
				}
			}
		}
		return false;
	}

	/**
	 * Sets a number of ticks to execute when the system is in interval
	 * execution mode. The system will execute all tasks scheduled in the queue
	 * until currentTick + ticks.
	 * 
	 * @param ticks
	 *            the number of ticks to use as an interval.
	 */
	public void addTicksToExecute(long ticks) {
		if (inIntervalMode) {
			endOfNextInterval = ticks + currentTick;
			synchronized (lock) {
				lock.notify();
			}
		}
	}

	/**
	 * Schedules the task for execution in currentTick + inXTicks If inXTicks is
	 * negative or 0, the task is not scheduled.
	 * 
	 * @param task
	 *            the task to schedule
	 * @param inXTicks
	 *            the number of ticks in the future that the task will be
	 *            scheduled for execution.
	 * @return true if the task was scheduled.
	 */
	public boolean scheduleTask(FrameworkTask task, long inXTicks) {
		if (inXTicks <= 0){
			return false;
		}

		Long time = currentTick + inXTicks;
		Queue<FrameworkTask> queue = taskQueue.get(time);
		if (queue == null) {
			Queue<FrameworkTask> queue2 = new ConcurrentLinkedQueue<FrameworkTask>();
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
		// TODO optimize this method to skip ticks until the next tick with
		// scheduled tasks is found
		Queue<FrameworkTask> queue = taskQueue.get(++currentTick);
		taskQueue.remove(currentTick);
		logger.log(Level.FINER, "Tick {0} executed", currentTick);
		if (queue != null) {
			try {
				decayModules();
				executorService.invokeAll(queue); // Execute all tasks scheduled
				// for this tick
			} catch (InterruptedException e) {
				if (!shuttingDown) {
					logger.log(Level.WARNING, "Current tick " + currentTick
							+ " was interrupted because of " + e.toString(),
							currentTick);
				} else {
					logger.log(Level.INFO, "Current tick " + currentTick
							+ " interrupted for application shutdown.",
							currentTick);
				}
			}
		}
		return currentTick;
	}

	private void decayModules() {
		DecayableWrapper.setDecayInterval(currentTick);
		try {
			executorService.invokeAll(decaybles);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, "Decaying interrupted. Message: "
					+ e.getMessage(), currentTick);
		}
		DecayableWrapper.setLastDecayTick(currentTick);
		logger.log(Level.FINEST, "Modules decayed", currentTick);
	}

	/**
	 * Set the Collection of modules for decaying
	 * 
	 * @param modules
	 *            a Collection with the FrameworkModules
	 */
	public void setDecayingModules(Collection<FrameworkModule> modules) {
		for (FrameworkModule lm : modules) {
			decaybles.add(new DecayableWrapper(lm));
		}
	}	
	
	/**
	 * This inner class implements the main loop of the system.
	 * The main loop waits on the lock if the tasks are paused,
	 * if in interval mode and have reached endOfNextInterval, 
	 * or if no tasks are scheduled beyond current tick.
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
							logger.log(Level.INFO, "Main loop interrupted.",
									currentTick);
							return;
						}
					}
				}

				long initTime = System.currentTimeMillis(); // For real time

				goNextTick(); // Execute one step of the whole system

				long duration = System.currentTimeMillis() - initTime;
				if (duration < tickDuration) {// TODO change this if multiticks
												// are executed in goNextTick()
					try {
						Thread.sleep(tickDuration - duration);
					} catch (InterruptedException e) {
						return;
					}
				}
			}// while
		}
	}// class

	/**
	 * This is an auxiliary class to perform the decaying of the modules in parallel.
	 * 
	 * @author Javier Snaider
	 *
	 */
	private static class DecayableWrapper implements Callable<Void> {
		private FrameworkModule module;
		private static long ticksToDecay;
		private static long lastDecayTick = 0L;

		/**
		 * Updates the interval that all decayables should decay.
		 * Must be setup before executing the run() method.
		 * @param currentTick current tick of the task manager
		 */
		public static void setDecayInterval(long currentTick) {
			ticksToDecay = currentTick - lastDecayTick;
		}

		/**
		 * Sets the last time that the decayables were decayed.
		 * @param lastDecayTick last tick modules were decayed
		 */
		public static void setLastDecayTick(long lastDecayTick) {
			DecayableWrapper.lastDecayTick = lastDecayTick;
		}

		public DecayableWrapper(FrameworkModule module) {
			this.module = module;
		}

		@Override
		public Void call() throws Exception {
			if (module != null) {
				module.decayModule(ticksToDecay);
			}
			return null;
		}
	}// private class
	
	/**
	 * This method stops all tasks executing and prevents further tasks from
	 * being executed. It is used to shutdown the entire system. Method shuts
	 * down all tasks, the executor service, waits, and exits.
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
		logger.log(Level.INFO,
				"TaskManager shutting down. System exiting.",
				getCurrentTick());
		System.exit(0);
	}
	
	@Override
	public String toString() {
		return "TaskManager";
	}

}