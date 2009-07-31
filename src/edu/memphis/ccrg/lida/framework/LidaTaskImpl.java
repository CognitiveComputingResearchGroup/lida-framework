/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.shared.ActivatibleImpl;

/**
 * @author Javier Snaider
 *
 */
public abstract class LidaTaskImpl extends ActivatibleImpl implements LidaTask {
	
	private long taskID;
	private int numberOfTicksPerCycle = 1;
	private int accumulatedTicks;
	protected int status = LidaTask.WAITING;
	
	public LidaTaskImpl(){
		this (1);
	}
	
	public LidaTaskImpl(int ticksForCycle){
		setTaskID(LidaTaskManager.getNextTaskID());
		setTicksForCycle(ticksForCycle);
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long id) {
		taskID=id;		
	}
	public int getNumberOfTicksPerCycle() {
		return numberOfTicksPerCycle;
	}

	public void setTicksForCycle(int ticks) {
		if(ticks > 0)
			numberOfTicksPerCycle=ticks;		
	}

	public void addTicks(int ticks) {
		this.accumulatedTicks = accumulatedTicks + ticks;
	}

	public int getAccumulatedTicks() {
		return accumulatedTicks;
	}

	public boolean consumeTicksForACycle() {
		if (accumulatedTicks >= numberOfTicksPerCycle){
			accumulatedTicks = accumulatedTicks - numberOfTicksPerCycle;
			return true;
		}
		return false;
	}

	public boolean hasEnoughTicks() {
		return accumulatedTicks >= numberOfTicksPerCycle;
	}
	
	public void reset(){
		
	}
	
}//class
