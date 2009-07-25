/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

/**
 * @author Javier Snaider
 *
 */
public abstract class LidaTaskBase implements LidaTask {

	private long taskID;
	private int ticksForCycle=0;
	private int accumulatedTicks;
	
	
	public LidaTaskBase(){
		setTaskID(LidaTaskManager.getNextTaskID());
	}
	
	public LidaTaskBase(int ticksForCycle){
		this();
		this.ticksForCycle=ticksForCycle;
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
		ticksForCycle=ticks;
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

}
