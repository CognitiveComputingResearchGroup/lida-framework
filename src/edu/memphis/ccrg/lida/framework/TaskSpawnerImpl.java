package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class TaskSpawnerImpl extends LidaTaskImpl implements
		TaskSpawner {

	private static Logger logger = Logger
			.getLogger("lida.framework.TaskSpawnerImpl");
	/**
	 * Used to execute the tasks
	 */
	private ThreadPoolExecutor executorService;
	/**
	 * The running tasks
	 */
	private Set<LidaTask> runningTasks = new HashSet<LidaTask>();

	/**
	 * Determines whether or not spawned task should run
	 */
	private boolean tasksPaused = true;

	/**
	 * Used to prevent resumeSpawnedTasks() from running once a shutdown has
	 * been started.
	 */
	private boolean shuttingDown = false;

	private LidaTaskManager taskManager;

	public TaskSpawnerImpl(int ticksForCycle, LidaTaskManager tm, LidaTaskNames name) {
		super(ticksForCycle, tm, name);
		taskManager = tm;
		if (taskManager != null)
			tasksPaused = taskManager.isSystemPaused();
		// thread pool executor
		int corePoolSize = 5;
		int maxPoolSize = 100;
		long keepAliveTime = 10;
		executorService = new LidaExecutorService(this, corePoolSize,
				maxPoolSize, keepAliveTime, TimeUnit.SECONDS);
	}// method

	public TaskSpawnerImpl(LidaTaskManager tm, LidaTaskNames name) {
		this(1, tm, name);
	}
	public void setInitialTasks(Collection<? extends LidaTask> initialTasks) {
		// System.out.println(this.getClass().toString() +
		// " setting initial tasks. system paused? " + tasksPaused);
		for (LidaTask r : initialTasks)
			addTask(r);
	}

	public void addTask(LidaTask task) {
		task.setTaskStatus(LidaTask.WAITING_TO_RUN);
		// System.out.println("adding task " + task.getClass().toString());
		synchronized (this) {
			runningTasks.add(task);
		}
		runTask(task);
	}

	protected void runTask(LidaTask task) {
		if (!tasksPaused) {
			if (shouldRun(task)) {
				logger.log(Level.FINEST, "Sending to executor task {0}", task);
				task.setTaskStatus(LidaTask.RUNNING);
				executorService.execute(task);
			}
		}
	}

	protected boolean shouldRun(LidaTask task) {
		// if not in ticks mode then should run task
		if (!LidaTaskManager.isInTicksMode())
			return true;
		// else in ticks mode, check for enough ticks
		else if (task.hasEnoughTicks())
			return true;
		return false;
	}// method

	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is overridden then is should still be called first using super.
	 */
	public void receiveFinishedTask(LidaTask task, Throwable t) {
		switch (task.getStatus()) {
		case LidaTask.FINISHED_WITH_RESULTS:
			processResults(task);
			removeTask(task);
			break;
		case LidaTask.FINISHED:
			removeTask(task);
			break;
		case LidaTask.CANCELED:
			logger.log(Level.FINEST, "cancelling task {0}", task);
			removeTask(task);
			break;
		case LidaTask.TO_RESET:
			logger.log(Level.FINEST, "reseting task {0}", task);
			task.reset();
		case LidaTask.WAITING_TO_RUN:
			// TODO:
		case LidaTask.RUNNING:
			logger.log(Level.FINEST, "Running task {0}", task);
			task.setTaskStatus(LidaTask.WAITING_TO_RUN);
			runTask(task);
			break;
		}
	}// method

	protected synchronized void removeTask(LidaTask t) {
		runningTasks.remove(t);
	}

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS
	 * This method is called to handle the results.
	 * 
	 * @param task
	 */
	protected abstract void processResults(LidaTask task);

	public Collection<LidaTask> getRunningTasks() {
		logger.log(Level.FINEST, "getting all tasks");
		return Collections.unmodifiableCollection(runningTasks);
	}

	public int getSpawnedTaskCount() {
		logger.log(Level.FINEST, "get spawned count");
		return runningTasks.size();
	}// method

	/**
	 * This method is override in this class in order to spawn the ticks to the
	 * sub tasks
	 */
	@Override
	public void addTicks(int ticks) {
		logger.log(Level.FINE, "Add ticks called");
		super.addTicks(ticks);
		synchronized (this) {
			for (LidaTask s : runningTasks) {
				s.addTicks(ticks);
				runTask(s);
			}// for
		}
	}// method

	public void pauseSpawnedTasks() {
		logger.log(Level.INFO, "All Tasks paused.");

		synchronized(this){
			tasksPaused = true;
		}
		for (LidaTask task : runningTasks) {
			if (task instanceof TaskSpawner) {
				((TaskSpawner) task).pauseSpawnedTasks();
			}
		}
	}
	public void resumeSpawnedTasks() {

		logger.log(Level.INFO, "resume spawned tasks called");
		if (shuttingDown)
			return;

		synchronized (this) {
			tasksPaused = false;
		}

		for (LidaTask task : runningTasks) {
			int status = task.getStatus();
		
			if ((status & (LidaTask.RUNNING | LidaTask.WAITING_TO_RUN | LidaTask.TO_RESET)) != 0) {
				task.setTaskStatus(LidaTask.WAITING_TO_RUN);
				logger.log(Level.FINEST, "Resuming task {0}", task);
				runTask(task);
				if (task instanceof TaskSpawner) {
					((TaskSpawner) task).resumeSpawnedTasks();
				}
			}// if
		}// for
	}// method

	/**
	 * @return the tasksPaused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}

	public void stopRunning() {
		// First ensure that 'resumeSpawnedTasks()' will not function normally
		// if called by
		// setting shuttingDown to true.
		// Then halt the execution of tasks in the runningTasks list.
		synchronized (this) {
			shuttingDown = true;
		}
		pauseSpawnedTasks();
		// Tell the running tasks to shut themselves down.
		synchronized (this) {
			for (LidaTask s : runningTasks) {
				logger.log(Level.FINER, "Stopping task: {0}", s);
				s.stopRunning();
			}// for
		}
		// Now that we can be sure that active tasks will no longer be executed
		// the executor service can be shutdown.
		executorService.shutdown();

		this.setTaskStatus(LidaTask.CANCELED);
		logger.log(Level.INFO, "Shutdown ThreadSpawner " + this.toString()
				+ "\n");
	}// method

	public void setTaskManager(LidaTaskManager taskManager) {
		super.setTaskManager(taskManager);
		this.taskManager = taskManager;
		tasksPaused = taskManager.isTasksPaused();
	}

}// class