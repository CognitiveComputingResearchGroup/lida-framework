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
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.framework.LidaExecutorService;

public abstract class TaskSpawnerImpl extends LidaTaskBase implements TaskSpawner {
		
	private Logger logger=Logger.getLogger("lida.framework.TaskSpawnerImpl");
	/**
	 * Used to execute the tasks
	 */
	private ThreadPoolExecutor executorService;
	/**
	 * A map of the running tasks
	 */
	private Set<LidaTask> runningTasks = new HashSet<LidaTask>();
	
	public TaskSpawnerImpl(){	
		int corePoolSize = 5;
		int maxPoolSize = 10;
	    long keepAliveTime = 10;
	    executorService = new LidaExecutorService(this, corePoolSize, maxPoolSize, keepAliveTime, 
	    											   TimeUnit.SECONDS);
	}// method
	public TaskSpawnerImpl(int ticksForCycle){
		this();
		setTicksForCycle(ticksForCycle);
	}

	
	public void setInitialTasks(List<? extends LidaTask> initialTasks) {
		for (LidaTask r : initialTasks)
			addTask(r);
	}
	public void addTask(LidaTask r) {
		executorService.submit(r);
		runningTasks.add(r);
	}
	public Collection<LidaTask> getAllTasks() {
		return Collections.unmodifiableCollection(runningTasks);
	}

    /**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is override this super method should be called too.
	 */
	public void receiveFinishedTask(LidaTask finishedTask, Throwable t) {
		runningTasks.remove(finishedTask);
	}//method
	
	public int getSpawnedTaskCount() {
		return runningTasks.size();
	}// method

	public void stopRunning() {
		for (LidaTask s: runningTasks){
				logger.log(Level.INFO,"stoppable telling to stop {0}", s.toString());
				s.stopRunning();
			}
		executorService.shutdownNow();
		logger.info("All threads have been told to stop");
	}// method
	/**
	 * This method is override in this class in order to spawn the ticks to the sub tasks
	 */
	@Override
	public void addTicks(int ticks){
		super.addTicks(ticks);
		for(LidaTask s: runningTasks){
			s.addTicks(ticks);
		}		
	}
}// class