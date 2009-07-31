package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class TaskSpawnerImpl extends LidaTaskImpl implements TaskSpawner {

	private Logger logger = Logger.getLogger("lida.framework.TaskSpawnerImpl");
	/**
	 * Used to execute the tasks
	 */
	private ThreadPoolExecutor executorService;
	/**
	 * The running tasks
	 */
	private Set<LidaTask> runningTasks = new HashSet<LidaTask>();

	private boolean tasksArePaused = false;

	/**
	 * @return the tasksArePaused
	 */
	public boolean inTasksArePaused() {
		return tasksArePaused;
	}

	public TaskSpawnerImpl(int ticksForCycle) {
		super(ticksForCycle);
		//
		int corePoolSize = 5;
		int maxPoolSize = 100;
		long keepAliveTime = 10;
		executorService = new LidaExecutorService(this, corePoolSize, maxPoolSize, 
												  keepAliveTime, TimeUnit.SECONDS);
	}// method

	public TaskSpawnerImpl() {
		this(1);
	}

	public void setInitialTasks(List<? extends LidaTask> initialTasks) {
		for (LidaTask r : initialTasks)
			addTask(r);
	}

	public void addTask(LidaTask r) {
		executorService.execute(r);
		runningTasks.add(r);
	}

	public Collection<LidaTask> getAllTasks() {
		return Collections.unmodifiableCollection(runningTasks);
	}

	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is overridden then is should still be called first using super.
	 */
	public void receiveFinishedTask(LidaTask finishedTask, Throwable t) {
		switch (finishedTask.getStatus()) {
		case LidaTask.FINISHED: // TODO: get and process result
		case LidaTask.CANCELLED:
			runningTasks.remove(finishedTask);
			break;
		case LidaTask.TO_RESET:
			finishedTask.reset();
		case LidaTask.STOPPED:
		case LidaTask.RUNNING:
			if (!tasksArePaused) {
				if (shouldRun(finishedTask)) {
					logger.log(Level.FINEST, "restarting task {0}",
							finishedTask);
					finishedTask.setStatus(LidaTask.RUNNING);
					executorService.execute(finishedTask);
				}
			}
			break;
		}
	}// method

	public int getSpawnedTaskCount() {
		return runningTasks.size();
	}// method

	public void stopRunning() {
		for (LidaTask s : runningTasks) {
			logger.log(Level.INFO, "LidaTask telling to stop {0}", s.toString());
			s.stopRunning();
		}
		executorService.shutdownNow();
		logger.info("All threads have been told to stop");
	}// method

	/**
	 * This method is override in this class in order to spawn the ticks to the
	 * sub tasks
	 */
	@Override
	public void addTicks(int ticks) {
		super.addTicks(ticks);
		for (LidaTask s : runningTasks) {
			s.addTicks(ticks);
			if (shouldRun(s)) {
				s.setStatus(LidaTask.RUNNING);
				executorService.execute(s);
			}
		}//for
	}//method

	protected boolean shouldRun(LidaTask r) {
		boolean result = false;
		if (!LidaTaskManager.inTicksMode() || ((r.getStatus() != LidaTask.RUNNING) && (r.hasEnoughTicks()))) {
			result = true;
		}
		return result;
	}

	public void pauseSpawnedTasks() {
		tasksArePaused = true;
	}

	public void resumeSpawnedTasks() {
		tasksArePaused = false;
		for (LidaTask s : runningTasks) {
			int status = s.getStatus();
			if ((status & (LidaTask.RUNNING | LidaTask.STOPPED | LidaTask.TO_RESET)) != 0) {
				s.setStatus(LidaTask.RUNNING);
				executorService.execute(s);
			}
		}
	}

}// class