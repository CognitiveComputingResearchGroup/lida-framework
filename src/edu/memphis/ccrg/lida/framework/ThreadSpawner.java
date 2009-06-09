package edu.memphis.ccrg.lida.framework;

/**
 * ThreadSpawners are classes that create, manage, and end new threads.  
 * 
 * @author ryanjmccall
 *
 */
public interface ThreadSpawner {

	public abstract int getThreadCount();
	public abstract void stopThreads();

}
