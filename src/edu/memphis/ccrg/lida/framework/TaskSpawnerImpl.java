package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract  class TaskSpawnerImpl extends LidaTaskImpl implements
		TaskSpawner {

	private static Logger logger = Logger
			.getLogger("lida.framework.TaskSpawnerImpl");

	/**
	 * The running tasks
	 */
	private ConcurrentLinkedQueue<LidaTask> runningTasks = new ConcurrentLinkedQueue<LidaTask>();

	public TaskSpawnerImpl(int ticksForCycle, LidaTaskManager tm) {
		super(ticksForCycle, tm, null);
	}// method

	public TaskSpawnerImpl(LidaTaskManager tm) {
		this(1, tm);
	}

	public void setInitialTasks(Collection<? extends LidaTask> initialTasks) {
		for (LidaTask r : initialTasks)
			addTask(r);
	}

	public void addTask(LidaTask task) {
		task.setTaskStatus(LidaTask.WAITING_TO_RUN);
		task.setTaskSpawner(this);
		runningTasks.add(task);
		runTask(task);
		logger.log(Level.FINEST, "Task {0} added", task);		
	}

	protected void runTask(LidaTask task) {
		logger.log(Level.FINEST, "Running task {0}", task);
		task.setTaskStatus(LidaTask.RUNNING);
		getTaskManager().scheduleTask(task, task.getTicksPerStep());
	}

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
		case LidaTask.CANCELLED:
			removeTask(task);
			break;
		case LidaTask.TO_RESET:
			logger.log(Level.FINEST, "reseting task {0}", task);
			task.reset();
		case LidaTask.WAITING_TO_RUN:
		case LidaTask.RUNNING:
			task.setTaskStatus(LidaTask.WAITING_TO_RUN);
			runTask(task);
			break;
		}
	}// method

	protected void removeTask(LidaTask task) {
		logger.log(Level.FINEST, "cancelling task {0}", task);
		runningTasks.remove(task);
	}

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS
	 * This method is called to handle the results.
	 * 
	 * @param task
	 */
	protected void processResults(LidaTask task) {
		
	}

	public Collection<LidaTask> getSpawnedTasks() {
		logger.log(Level.FINEST, "getting all tasks");
		return Collections.unmodifiableCollection(runningTasks);
	}

	public int getSpawnedTaskCount() {
		logger.log(Level.FINEST, "get spawned count");
		return runningTasks.size();
	}// method


//	public void resumeSpawnedTasks() {
//
//		logger.log(Level.FINE, "resume spawned tasks called");
//		if (shuttingDown)
//			return;
//
//		for (LidaTask task : runningTasks) {
//			int status = task.getStatus();
//
//			if ((status & (LidaTask.RUNNING | LidaTask.WAITING_TO_RUN | LidaTask.TO_RESET)) != 0) {
//				task.setTaskStatus(LidaTask.WAITING_TO_RUN);
//				logger.log(Level.FINEST, "Resuming task {0}", task);
//				runTask(task);
//				if (task instanceof TaskSpawner) {
//					((TaskSpawner) task).resumeSpawnedTasks();
//				}
//			}// if
//		}// for
//	}// method

//	public void stopRunning() {
//		// First ensure that 'resumeSpawnedTasks()' will not function normally
//		// if called by
//		// setting shuttingDown to true.
//		// Then halt the execution of tasks in the runningTasks list.
//		synchronized (this) {
//			shuttingDown = true;
//		}
//		pauseSpawnedTasks();
//		// Tell the running tasks to shut themselves down.
//		synchronized (this) {
//			for (LidaTask s : runningTasks) {
//				logger.log(Level.FINER, "Stopping task: {0}", s);
//				s.stopRunning();
//			}// for
//		}
//
//		completionThread.interrupt();
//
//		this.setTaskStatus(LidaTask.CANCELLED);
//		logger.log(Level.FINE, "Shutdown ThreadSpawner " + this.toString()
//				+ "\n");
//	}// method

}// class