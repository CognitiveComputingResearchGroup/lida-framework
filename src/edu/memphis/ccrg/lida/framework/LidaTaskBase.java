/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.shared.ActivatibleImpl;

/**
 * @author Javier Snaider
 *
 */
public abstract class LidaTaskBase extends ActivatibleImpl implements LidaTask {
	
	private long taskID;
	private int ticksForCycle=1;
	private int accumulatedTicks;
	protected int status=LidaTask.WAITING;
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public LidaTaskBase(){
		this (1);
	}
	
	public LidaTaskBase(int ticksForCycle){
		setTaskID(LidaTaskManager.getNextTaskID());
		setTicksForCycle(ticksForCycle);
	}

	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long id) {
		taskID=id;		
	}
	public int getTicksForCycle() {
		return ticksForCycle;
	}

	public void setTicksForCycle(int ticks) {
		if(ticks > 0){
		ticksForCycle=ticks;
		}
	}

	public void addTicks(int ticks) {
		this.accumulatedTicks = accumulatedTicks + ticks;
	}

	public int getAccumulatedTicks() {
		return accumulatedTicks;
	}

	public boolean consumeTicksForACycle() {
		if (accumulatedTicks>=ticksForCycle){
			accumulatedTicks = accumulatedTicks-ticksForCycle;
			return true;
		}
		return false;
	}

	public boolean hasEnoughTicks() {
		return (accumulatedTicks>=ticksForCycle);
	}
	
	public void reset(){
		
	}
}
