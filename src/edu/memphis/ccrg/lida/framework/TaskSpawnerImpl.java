package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.gui.panels.TaskManagerPanel;

/**
 * Implements the TaskSpawner interface.
 * 
 * 
 * @author Javier Snaider
 *
 */
public abstract class TaskSpawnerImpl extends LidaTaskImpl implements
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
		task.setTaskStatus(LidaTaskStatus.WAITING_TO_RUN);
		task.setTaskSpawner(this);
		runningTasks.add(task);
		runTask(task);
		logger.log(Level.FINEST, "Task {0} added", task);		
	}

	/**
	 * Schedule the LidaTask to be excecuted.
	 * Sets the task status to RUNNING.
	 * @param task
	 */
	protected void runTask(LidaTask task) {
		logger.log(Level.FINEST, "Running task {0}", task);
		task.setTaskStatus(LidaTaskStatus.RUNNING);
		getTaskManager().scheduleTask(task, task.getNextExcecutionTickLap());
	}

	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is overridden then is should still be called first using super.
	 */
	public void receiveFinishedTask(LidaTask task, Throwable t) {
		switch (task.getStatus()) {
		case FINISHED_WITH_RESULTS:
			processResults(task);
			removeTask(task);
			break;
		case FINISHED:
			removeTask(task);
			break;
		case CANCELLED:
			removeTask(task);
			break;
		case TO_RESET:
			logger.log(Level.FINEST, "reseting task {0}", task);
			task.reset();
		case WAITING_TO_RUN:
		case RUNNING:
			task.setTaskStatus(LidaTaskStatus.WAITING_TO_RUN);
			runTask(task);
			break;
		}
	}// method

	/**
	 * When a finished task is received and its status is FINISHED_WITH_RESULTS or FINISHED or CANCELLED
	 * This method is called to remove the task from this TaskSpawner
	 * @param task the LidaTask to remove.
	 */
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

	public void cancelTask(LidaTask task) {
		removeTask(task);
		getTaskManager().cancelTask(task);
		
	}
}// class