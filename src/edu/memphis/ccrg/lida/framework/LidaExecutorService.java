package edu.memphis.ccrg.lida.framework;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LidaExecutorService extends ThreadPoolExecutor {
	private static Logger logger=Logger.getLogger("lida.framework.LidaExecutorService");
	private TaskSpawner spawner;

	// consider overriding whatever executing method we use.  In the override
	// set id for the LidaTask to be run.  This way we can ensure that
	// all running threads in Lida have unique id as long as only this class
	// is used to execute threads.

   /**
	* (From ThreadPoolExecutor javadoc) 
	* @param corePoolSize the number of threads to keep in the
    * pool, even if they are idle.
    * @param maximumPoolSize the maximum number of threads to allow in the
    * pool.
    * @param keepAliveTime when the number of threads is greater than
    * the core, this is the maximum time that excess idle threads
    * will wait for new tasks before terminating.
    * @param unit the time unit for the keepAliveTime
    * argument.
    * @param workQueue the queue to use for holding tasks before they
    * are executed. This queue will hold only the <tt>Runnable</tt>
    * tasks submitted by the <tt>execute</tt> method.
    */
	public LidaExecutorService(TaskSpawner spawner, int corePoolSize, int maximumPoolSize,
							   long keepAliveTime, TimeUnit unit) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
		this.spawner = spawner;
	}
	
	protected void afterExecute(Runnable r, Throwable t){
		super.afterExecute(r, t);
		logger.finest(r.getClass().getName());
		spawner.receiveFinishedTask((LidaTask)r, t);
	}
	
	 //Future<?> 	submit(Runnable task)
     
     //<T> Future<T> submit(Runnable task, T result) 

}//class