package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ThreadSpawners are classes that create, manage, and end new threads.  
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
	 * Adds a new LidaTask to this Spawner
	 * @param r
	 */
	public abstract void addTask(LidaTask r);
	/**
	 * returns a unmodifiable Collection that contains the LidaTasks in this Spawner
	 * @return
	 */
	public abstract Collection<LidaTask> getAllTasks();
	
	/**
	 * The supplied LidaTask will be start by the spawner right away.
	 */
	public abstract void setInitialTasks(List<? extends LidaTask> initialRunnables);
		
	/**
	 * This method receives the tasks that have finished.
	 * 
	 * @param finishedTask
	 * @param t
	 */
	public abstract void receiveFinishedTask(LidaTask finishedTask, Throwable t);

}
