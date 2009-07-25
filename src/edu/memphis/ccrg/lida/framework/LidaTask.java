package edu.memphis.ccrg.lida.framework;


/**
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public interface LidaTask extends Runnable{
	
	public abstract void stopRunning();
	
	public abstract void setTaskID(long id);
	
	public abstract long getTaskID();
	
	/**
	 * Sets how much ticks are needed to complete a cycle or a run of this task
	 * @param ticks
	 */
	public abstract void setTicksForCycle(int ticks);

	/**
	 * Gets the number of ticks needed to  complete a cycle or a run of this task
	 * @return ticks 
	 */
	public abstract int getTicksForCycle();
	public abstract int getAccumulatedTicks();
	public abstract void addTicks(int ticks);
	public abstract boolean hasEnoughTicks();
	public abstract boolean consumeTicksForACycle();
}
