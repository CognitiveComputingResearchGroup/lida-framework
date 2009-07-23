package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	private Set<Runnable> runningTasks = new HashSet<Runnable>();
	
	/**
	 * The ticksMode permits to run the framework in a step by step fashion. Modules have 
	 * cycles rates between them. For example, sensory memory could work ten times faster than PAM
	 * so one tick means one cycle for PAM and ten for Sensory Memory.
	 * In order to have the framework running accurately the relative speed of each part of the
	 * framework must be set. 
	 * 
	 * Map w/ key as the thread ID and the value for whether or not that thread
	 * should wait for the next time step.  TODO: Waiting for the next step has not yet
	 * been implemented.
	 */
	private Map<Long, Boolean> ticksModeMap = new HashMap<Long, Boolean>();
	//private int threadIdCounter = 0;

	public ThreadSpawnerImpl(){	
		int corePoolSize = 5;
		int maxPoolSize = 10;
	    long keepAliveTime = 10;
	    executorService = new FrameworkExecutorService(this, corePoolSize, maxPoolSize, keepAliveTime, 
	    											   TimeUnit.SECONDS);
	}// method
	
	public void setInitialCallableTasks(List<? extends Callable<Object>> initialCallables) {
		for (Callable<Object> c : initialCallables){
			FutureTask<Object> ft = new FutureTask<Object>(c);
			addTask(ft);
		}
	}	
	public void setInitialRunnableTasks(List<? extends Runnable> initialRunnables) {
		for (Runnable r : initialRunnables)
			addTask(r);
	}
	public void addTask(Runnable r) {
		executorService.submit(r);
		//r.setId(threadIdCounter); 
		//registerThread(threadIdCounter++);
		runningTasks.add(r);
	}
	public Collection<Runnable> getAllTasks() {
		return Collections.unmodifiableCollection(runningTasks);
	}

    /**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * Since 
	 */
	public void receiveFinishedTask(Runnable finishedTask, Throwable t) {
		runningTasks.remove(finishedTask);
	}//method
	
	public void stopSpawnedThreads() {
		Collection<Runnable> ctasks = runningTasks;
		//System.out.println(runningTasks.size());
		for (Runnable s: ctasks){
			if (s instanceof ThreadSpawner){
				//System.out.println("\nspawner telling to stop " + s.toString());
				((ThreadSpawner)s).stopSpawnedThreads();				
			}else if (s instanceof Stoppable){
				//System.out.println("\nstoppable telling to stop " + s.toString());
				((Stoppable)s).stopRunning();
			}
		}// for
		executorService.shutdownNow();
		logger.info("All threads have been told to stop");
	}// method

	public int getSpawnedThreadCount() {
		return runningTasks.size();
	}// method
	
	/**
	 * Called from the GUI.  This sets all the values in the map
	 * to true, signifying that all these threads should run once 
	 * cycle and pause.
	 */
	public synchronized void haveThreadRunOnceThenPause(){	
		Set<Long> keys = ticksModeMap.keySet();
		for(Long l: keys)
			ticksModeMap.put(l, true);
	}//method

	/**
	 * Threads call this functions with their ID.

	 * @param threadID
	 * @return boolean signaling whether to enter ticks mode
	 */
	public boolean shouldEnterTicksMode(long threadID){
		Boolean shouldEnterTicksMode = ticksModeMap.get(threadID);
		if(shouldEnterTicksMode == null){
			System.out.println("Thread with ID " + threadID + " not registered! This thread continues to run.");
			return false;
		}else if(shouldEnterTicksMode){
			ticksModeMap.put(threadID, false);
			return true;
		}else
			return false;				
	}//
	/**
	 * Add a thread for the 'ticks mode' functionality
	 * @param threadID
	 */
	public void registerThread(long threadID) {
		ticksModeMap.put(threadID, false);		
	}//
	
}// class