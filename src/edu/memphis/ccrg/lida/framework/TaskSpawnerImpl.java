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

public abstract class TaskSpawnerImpl extends LidaTaskImpl implements TaskSpawner{

	private static Logger logger = Logger.getLogger("lida.framework.TaskSpawnerImpl");
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
	private boolean tasksPaused = false;
	
	/**
	 * Used to prevent resumeSpawnedTasks() from running once a shutdown has been started. 
	 */
	private boolean shuttingDown = false;
	
	public TaskSpawnerImpl(int ticksForCycle) {
		super(ticksForCycle);
		//thread pool executor
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

	public void addTask(LidaTask task) {
		task.setTaskStatus(LidaTask.WAITING_TO_RUN);
		synchronized(this){
			runningTasks.add(task);
		}
		runTask(task);
	}
	protected void runTask(LidaTask task) {
		if (!tasksPaused) {
			if (shouldRun(task)) {
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
	}//method

	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is overridden then is should still be called first using super.
	 */
	public void receiveFinishedTask(LidaTask task, Throwable t) {
		switch (task.getTaskStatus()) {
		case LidaTask.FINISHED: 
			// TODO: get and process result
			//System.out.println("FINISHED");
		case LidaTask.CANCELLED:
			logger.log(Level.FINEST, "cancelling task {0}", task);
			synchronized(this){
				runningTasks.remove(task);
			}
			//System.out.println("CANCELLED");
			break;
		case LidaTask.TO_RESET:
			logger.log(Level.FINEST, "reseting task {0}", task);
			task.reset();
			//System.out.println("TO_RESET");
		case LidaTask.WAITING_TO_RUN:
			//TODO:
		case LidaTask.RUNNING:
			logger.log(Level.FINEST, "Running task {0}", task);
			task.setTaskStatus(LidaTask.WAITING_TO_RUN);			
			runTask(task);
			//System.out.println("RUN");
			break;
		}
	}// method

	public Collection<LidaTask> getAllTasks() {
		logger.log(Level.FINEST,"getting all tasks");
		return Collections.unmodifiableCollection(runningTasks);
	}
	public int getSpawnedTaskCount() {
		logger.log(Level.FINEST,"get spawned count");
		return runningTasks.size();
	}// method

	public void stopRunning() {
		logger.log(Level.FINE,"stop running called in task spawner");
		for (LidaTask s : runningTasks) {
			logger.log(Level.INFO, "Stopping task: {0}", s);
			s.stopRunning();
			//Don't remove this! we need it!!!!!!
			s.setTaskStatus(LidaTask.CANCELLED);
			//Don't remove this.
		}
		executorService.shutdownNow();
		logger.info("All spawned tasks have been told to stop");
	}// method

	/**
	 * This method is override in this class in order to spawn the ticks to the
	 * sub tasks
	 */
	@Override
	public void addTicks(int ticks) {
		logger.log(Level.FINE,"Add ticks called");
		super.addTicks(ticks);
		for (LidaTask s : runningTasks) {
			s.addTicks(ticks);
			runTask(s);
		}// for
	}// method
	
	public void pauseSpawnedTasks() {
		logger.log(Level.FINE, "All Tasks paused.");
		synchronized(this){
			tasksPaused = true;
		}
	}

	public void resumeSpawnedTasks() {
		logger.log(Level.FINE,"resume spawned tasks called");
		if(shuttingDown)
			return;
		
		synchronized(this){
			tasksPaused = false;
		}
		logger.log(Level.FINE,"resume spawned tasks called");
		for (LidaTask task : runningTasks) {
			int status = task.getTaskStatus();
			if ((status & (LidaTask.RUNNING | LidaTask.WAITING_TO_RUN | LidaTask.TO_RESET)) != 0) {
				task.setTaskStatus(LidaTask.WAITING_TO_RUN);
				logger.log(Level.FINEST, "Resuming task {0}", task);
				runTask(task);
			}//if
		}//for
	}// method
	/**
	 * @return the tasksPaused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}

	public void stopRunning() {
		// First ensure that 'resumeSpawnedTasks()' will not function normally if called by 
		// setting shuttingDown to true.
		// Then halt the execution of tasks in the runningTasks list.
		synchronized(this){
			shuttingDown = true;
		}
		pauseSpawnedTasks();
		
		//Now that we can be sure that active tasks will no longer be executed the executor service can be shutdown.
		executorService.shutdown();
		
		//Tell the running tasks to shut themselves down.
		synchronized(this){
			for(LidaTask s : runningTasks) {
				logger.log(Level.INFO, "Stopping task: {0}", s);
				s.stopRunning();
			}//for
		}
		this.setTaskStatus(LidaTask.CANCELLED);
		logger.info("ThreadSpawner " + this.toString() + " and all tasks it spawned have been told to stop");
	}// method

}// class