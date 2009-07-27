package edu.memphis.ccrg.lida.framework;

import java.util.concurrent.Callable;

import edu.memphis.ccrg.lida.shared.Activatible;


/**
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public interface LidaTask extends Runnable,Activatible{
	public static final int WAITING=0;
	public static final int RUNNING=1;
	public static final int FINISHED=2;
	public static final int STOPPED=4;
	public static final int TO_RESET=8;
	public static final int CANCELLED=16;

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
	public abstract int getStatus();
	public abstract void setStatus(int status);
	public abstract void reset();
	
}
