package edu.memphis.ccrg.lida.framework;

import java.util.Map;
import java.util.concurrent.Callable;

import edu.memphis.ccrg.lida.framework.shared.Activatible;


/**
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */

public interface LidaTask extends Callable, Activatible{
	
	/**
	 * Task is scheduled to be run, but is not running yet.
	 */
	public static final int WAITING=0;
	
	/**
	 * Task is running
	 */
	public static final int RUNNING=1;
	
	/**
	 * Task is finished
	 */
	public static final int CANCELLED=16;
	
	/**
	 * Task is finished and has a result
	 */
	public static final int FINISHED=2;
	
	/**
	 * Task is not running and not finished.
	 */
	public static final int WAITING_TO_RUN=4;
	
	/**
	 * Task has finished and must be reseted and started again.
	 */
	public static final int TO_RESET=8;

	/**
	 * Task has finished and has results to process
	 */
	public static final int FINISHED_WITH_RESULTS = 32;
	
	
	/**
	 * returns the LidaTask's status code. It must be one of the static members of this interface. 
	 * @return the status code.
	 */
	public abstract int getStatus();
	/**
	 * Sets the LidaTask's status code. It must be one of the static members of this interface. 
	 * @param the status code
	 */
	public abstract void setTaskStatus(int status);

	/**
	 * The LidaTask must stop.
	 */
	public abstract void stopRunning();
	
	/**
	 * Each LidaTask is meant to have a unique id that is set at the time of creation.
	 * 
	 * @param id
	 */
	public abstract void setTaskID(long id);
	
	/**
	 * Each LidaTask is meant to have a unique id that is set at the time of creation.
	 * 
	 * @return  id
	 */
	public abstract long getTaskId();
	
	/**
	 * Sets how many ticks are needed to complete a cycle or 'one run' of this task
	 * @param ticks
	 */
	public abstract void setNumberOfTicksPerStep(int ticks);

	/**
	 * Gets the number of ticks needed to complete a cycle or 'one run' of this task
	 * @return ticks 
	 */
	public abstract int getTicksPerStep();
	
	/**
	 * 
	 * @return The accumulated ticks that this LidaTask has.
	 */
	public abstract int getAccumulatedTicks();
	
	/**
	 * Adds Ticks to the LidaTasks
	 * @param ticks
	 */
	public abstract void addTicks(int ticks);
	
	/**
	 * @return true if the number of accumulated ticks is greater than the number of ticks per step
	 */
	public abstract boolean hasEnoughTicks();
	
	/**
	 * Consumes ticks from the accumulated ticks to perform an step.
	 * @return true if there was enough ticks to perform the step.
	 */
	public abstract boolean useOneStepOfTicks();

	
	/**
	 * Resets the LidaTask.
	 */
	public abstract void reset();
	public void init(Map<String, Object> parameters);
	public Object getParameter(String name);
	public abstract String getStatusString();	
	public abstract String toString();
}
