package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Collection;

/**
 * TaskSpawners create, manage, and end new LidaTasks.  
 * 
 * @author ryanjmccall
 *
 */
public interface TaskSpawner extends LidaTask{

	/**
	 * Gets the number of Tasks in this Spawner
	 * @return
	 */
	public abstract int getSpawnedTaskCount();
	/**
	 * Adds a new LidaTask to this Spawner.
	 * @param task the task to add.
	 */
	public abstract void addTask(LidaTask task);
	/**
	 * returns a unmodifiable Collection that contains the LidaTasks in this Spawner
	 * @return
	 */
	public abstract Collection<LidaTask> getSpawnedTasks();
	
	/**
	 * The supplied LidaTask will be start by the spawner right away.
	 */
	public abstract void setInitialTasks(Collection<? extends LidaTask> initialTasks);
		
	/**
	 * This method receives the tasks that have finished. Each TaskSpawner can choose what to do 
	 * with each LidaTask each time it finished to run one step. Generally the LidaTask's status commands this 
	 * action.
	 * 
	 * @param task
	 * @param t
	 */
	public abstract void receiveFinishedTask(LidaTask task, Throwable t);
	
	/**
	 * Cancels the task from the Task Queue. This is only possible if the tick for witch the task 
	 * is scheduled has not been reached.
	 * 
	 * @param task The task to cancel.
	 */
	public abstract void cancelTask(LidaTask task);

	public abstract LidaTaskManager getTaskManager();
	public abstract void setTaskManager(LidaTaskManager taskManager);

}
