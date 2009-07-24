package edu.memphis.ccrg.lida.framework;

/**
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public interface LidaTask extends Runnable{
	
	public abstract void stopRunning();
	
	public abstract void setThreadID(long id);
	
	public abstract long getThreadID();

}
