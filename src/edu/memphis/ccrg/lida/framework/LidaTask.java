package edu.memphis.ccrg.lida.framework;

import java.util.Map;
import java.util.concurrent.Callable;

import edu.memphis.ccrg.lida.framework.shared.Activatible;


/**
 * This is the base Interface for all the task process in LIDA.
 * All piece of process in the LIDA Framework have to implement this interface.
 * A LidaTask is intended as a small fraction of a process. For example a Codelet or a Feature 
 * detector are examples of LidaTask. However, if the process includes a loop, the LidaTask represents 
 * only one iteration of this process.
 * 
 * TaskSpawners send this LidaTask to the LidaTaskManager which executes them. TaskSpawners use
 * CompletionService, so when the task has finished, the TaskSpawner can decide if this particular task
 * must run again or not. This is based on the status of the LidaTask.
 * The LidaTask should set its status during it execution.
 * 
 * Most classes can extend from LidaTaskImpl instead of implementing this interface from scratch.
 * 
 * @author Ryan J. McCall, Javier Snaider
 *
 */

public interface LidaTask extends Callable<LidaTask>, Activatible{
	/**
	 * returns the LidaTask's status code. It must be one of the static members of this interface. 
	 * @return the status code.
	 */
	public abstract LidaTaskStatus getStatus();
	/**
	 * Sets the LidaTask's status code. It must be one of the static members of this interface. 
	 * @param the status code
	 */
	public abstract void setTaskStatus(LidaTaskStatus status);

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
	 * Resets the LidaTask.
	 */
	public abstract void reset();
	
	
	/**
	 * Receives a Map of init parameters for initalization.
	 * @param parameters the Map with the parameters.
	 */
	public void init(Map<String, Object> parameters);

	/**
	 * A convenience method to get the value of one init parameter
	 * @param name the name of the parameter
	 * @return the value of the parameter
	 * 
	 * @see init(Map<String, Object> parameters)
	 */
	public Object getParameter(String name);

	public abstract String toString();
	
	/**
	 * Sets the TaskSpawner that controls this LidaTask.
	 * @param ts the TaskSpawner
	 */
	public abstract void setTaskSpawner(TaskSpawner ts);
	
	/**
	 * Gets the TaskSpawner that controls this LidaTask.
	 * @return the TaskSpawner.
	 */
	public abstract TaskSpawner getTaskSpawner();
	
	
	/**
	 * Sets the the lap of ticks to schedule the next execution of this
	 * LidaTask.
	 * It should be set by the LidaTask.
	 * 
	 * @param lapTick the number of ticks to wait until the next execution.
	 */
	public abstract void setNextExcecutionTickLap(long lapTick);
	
	/**
	 * Gets the the lap of ticks to schedule the next execution of this
	 * LidaTask.
	 * 
	 * @return
	 */
	public abstract long getNextExcecutionTickLap();
	
	/**
	 * @return The last ScheduledTick for this task. Could be in the future if this task is 
	 * scheduled for execution. 
	 */
	public long getScheduledTick();
	
	/**
	 * Sets the next scheduledTick for this task. This method is used by LidaTaskManager when
	 * a new task is added.
	 * 
	 * @param scheduledTick
	 */
	public void setScheduledTick(long scheduledTick);

}
