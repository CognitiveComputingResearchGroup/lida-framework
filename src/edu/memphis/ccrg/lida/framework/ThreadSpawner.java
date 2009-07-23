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
public interface ThreadSpawner {

	public abstract int getSpawnedThreadCount();
	
	public abstract void stopSpawnedThreads();
	public abstract void addTask(Runnable r);
	public abstract Collection<Runnable> getAllTasks();
	
	/**
	 * The supplied runnables will be start by the spawner right away.
	 */
	public abstract void setInitialRunnableTasks(List<? extends Runnable> initialRunnables);
	
	/**
	 * The supplied Callable will be start by the spawner right away.
	 */
	public abstract void setInitialCallableTasks(List<? extends Callable<Object>> initialCallables);
	
	/**
	 * This method receives the tasks that have finished.
	 * 
	 * @param finishedTask
	 * @param t
	 */
	public abstract void receiveFinishedTask(Runnable finishedTask, Throwable t);

}
