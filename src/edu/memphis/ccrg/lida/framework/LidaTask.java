package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.shared.Activatible;


/**
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public interface LidaTask extends Runnable, Activatible{
	
	//TODO: Explain these
	/**
	 * Task been scheduled to run, but is not running yet.
	 */
	public static final int WAITING=0;
	
	/**
	 * Task is running
	 */
	public static final int RUNNING=1;
	
	/**
	 * Task is finished and has a result
	 */
	public static final int FINISHED=2;
	
	/**
	 * Task is not running and not finished.
	 */
	public static final int WAITING_TO_RUN=4;
	
	public static final int TO_RESET=8;

	/**
	 * Task is finished
	 */
	public static final int CANCELLED=16;
	
	public abstract int getTaskStatus();
	public abstract void setTaskStatus(int status);

	public abstract void stopRunning();
	
	/**
	 * Each lida task is meant to have a unique id that is set at the time of creation.
	 * 
	 * @param id
	 */
	public abstract void setTaskID(long id);
	
	public abstract long getTaskID();
	
	/**
	 * Sets how many ticks are needed to complete a cycle or 'one run' of this task
	 * @param ticks
	 */
	public abstract void setNumberOfTicksPerStep(int ticks);

	/**
	 * Gets the number of ticks needed to complete a cycle or 'one run' of this task
	 * @return ticks 
	 */
	public abstract int getNumberOfTicksPerStep();
	
	public abstract int getAccumulatedTicks();
	public abstract void addTicks(int ticks);
	public abstract boolean hasEnoughTicks();
	public abstract boolean useOneStepOfTicks();

	public abstract void reset();	
}
