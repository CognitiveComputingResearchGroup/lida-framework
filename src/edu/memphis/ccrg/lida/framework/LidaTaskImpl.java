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
	private int ticksPerStep = 1;
	private int accumulatedTicks;
	protected int status = LidaTask.WAITING;
	
	public LidaTaskImpl(){
		this (1);
	}
	
	public LidaTaskImpl(int ticksForCycle){
		setTaskID(LidaTaskManager.getNextTaskID());
		setNumberOfTicksPerStep(ticksForCycle);
	}
	
	/**
	 * @param status the status to set
	 */
	public void setTaskStatus(int status) {
		//If a task is cancelled it cannot be restarted.
		//So only set the status if the status is not CANCELLED.
		if(this.status != CANCELLED)
			this.status = status;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	public long getTaskId() {
		return taskID;
	}

	public void setTaskID(long id) {
		taskID=id;		
	}
	public int getTicksPerStep() {
		return ticksPerStep;
	}

	public void setNumberOfTicksPerStep(int ticks) {
		if(ticks > 0)
			ticksPerStep = ticks;		
	}

	public void addTicks(int ticks) {
		this.accumulatedTicks = accumulatedTicks + ticks;
	}

	public int getAccumulatedTicks() {
		return accumulatedTicks;
	}

	public boolean useOneStepOfTicks() {
		if (accumulatedTicks >= ticksPerStep){
			accumulatedTicks = accumulatedTicks - ticksPerStep;
			return true;
		}
		return false;
	}

	public boolean hasEnoughTicks() {
		return accumulatedTicks >= ticksPerStep;
	}
	
	public void reset(){
		
	}
	
	public void stopRunning(){
		setTaskStatus(LidaTask.CANCELLED);
	}
}//class
