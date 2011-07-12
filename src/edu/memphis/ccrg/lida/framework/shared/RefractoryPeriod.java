package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Implementors of this interface can have a refractory period.
 * The unit of the time period is in ticks, the unit of time in the framework.
 * 
 * @see TaskManager
 * @author Ryan J. McCall
 */
public interface RefractoryPeriod {
	
	/**
	 * Sets refractoryPeriod
	 * @param ticks length of refractory period in ticks
	 * @see TaskManager
	 */
	void setRefractoryPeriod(int ticks);
	
	/**
	 * Gets refractoryPeriod
	 * @return length of refractory period in ticks
	 * @see TaskManager
	 */
	int getRefractoryPeriod();

}
