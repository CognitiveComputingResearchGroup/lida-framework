package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ThreadSpawnerImpl implements ThreadSpawner {
	private Logger logger=Logger.getLogger("lida.framework.ThreadSpawnerImpl");
	/**
	 * Used to execute the tasks
	 */
	private ThreadPoolExecutor executorService;
	/**
	 * A map of the running tasks
	 */
	private Set<Runnable> tasks = new HashSet<Runnable>();

	public ThreadSpawnerImpl(){	
		int corePoolSize = 5;
		int maxPoolSize = 10;
	    long keepAliveTime = 10;
	    executorService = new FrameworkExecutorService(this, corePoolSize, maxPoolSize, keepAliveTime, 
	    											   TimeUnit.SECONDS);
	}// method

		/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * Since 
	 */
	public void receiveFinishedTask(Runnable finishedTask, Throwable t) {
		tasks.remove(finishedTask);
}//method
	
	public void stopSpawnedThreads() {
		Collection<Runnable> ctasks=tasks;
		for (Runnable s:ctasks){
			if (s instanceof ThreadSpawner){
				((ThreadSpawner)s).stopSpawnedThreads();
			}
			if ((s != null)&&(s instanceof Stoppable))
				((Stoppable)s).stopRunning();
		}// for
		executorService.shutdownNow();//TODO: move this?
		logger.info("all structure-building codelets told to stop");
	}// method

	public int getSpawnedThreadCount() {
		return tasks.size();
	}// method

	public void addTask(Runnable r) {
		executorService.submit(r);
		tasks.add(r);
	}

	public Collection<Runnable> getAllTasks() {
		return Collections.unmodifiableCollection(tasks);
	}
	public void setInitialCallableTasks(List<? extends Callable<Object>> initialCallables) {
		for (Callable<Object> c : initialCallables){
			FutureTask<Object> ft= new FutureTask<Object> (c);
			addTask(ft);
		}
	}	

	public void setInitialTasks(List<? extends Runnable> initialRunnables) {
		for (Runnable c : initialRunnables){
			addTask(c);
		}
	}

}// class