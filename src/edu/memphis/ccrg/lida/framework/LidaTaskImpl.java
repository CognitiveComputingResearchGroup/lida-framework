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
	private int numberOfTicksPerStep = 1;
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
		if(this.status != CANCELLED)
			this.status = status;
	}
	/**
	 * @return the status
	 */
	public int getTaskStatus() {
		return status;
	}

	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long id) {
		taskID=id;		
	}
	public int getNumberOfTicksPerStep() {
		return numberOfTicksPerStep;
	}

	public void setNumberOfTicksPerStep(int ticks) {
		if(ticks > 0)
			numberOfTicksPerStep=ticks;		
	}

	public void addTicks(int ticks) {
		this.accumulatedTicks = accumulatedTicks + ticks;
	}

	public int getAccumulatedTicks() {
		return accumulatedTicks;
	}

	public boolean useOneStepOfTicks() {
		if (accumulatedTicks >= numberOfTicksPerStep){
			accumulatedTicks = accumulatedTicks - numberOfTicksPerStep;
			return true;
		}
		return false;
	}

	public boolean hasEnoughTicks() {
		return accumulatedTicks >= numberOfTicksPerStep;
	}
	
	public void reset(){
		
	}
	
	public void stopRunning(){
		setTaskStatus(LidaTask.CANCELLED);
		//System.out.println("module driver impl setting task status to cancelled " + LidaTask.CANCELLED);

	}
}//class
