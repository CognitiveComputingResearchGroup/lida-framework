package edu.memphis.ccrg.lida.framework;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * @author Javier
 *
 */
public class LidaExecutorService extends ThreadPoolExecutor {
	private static Logger logger=Logger.getLogger("lida.framework.LidaExecutorService");
	private LidaTaskManager taskManager;

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
	public LidaExecutorService(int corePoolSize, int maximumPoolSize,
							   long keepAliveTime, TimeUnit unit,LidaTaskManager taskManager) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
		this.taskManager=taskManager;
	}
	
	/**
	 * Calls reciveFinishedTask method in LidaTaskManager.
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@SuppressWarnings("unchecked")
	protected void afterExecute(Runnable r, Throwable t){
		super.afterExecute(r, t);
//		logger.log(Level.FINEST,r.getClass().getName(),LidaTaskManager.getActualTick());
//		try {
//			taskManager.receiveFinishedTask(((Future<LidaTask>)r).get(), t);
//		} catch (InterruptedException e) {
//			logger.log(Level.INFO,e.getMessage(),LidaTaskManager.getActualTick());
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			logger.log(Level.WARNING,e.getMessage(),LidaTaskManager.getActualTick());
//			e.printStackTrace();
//		}
	}
	
}//class