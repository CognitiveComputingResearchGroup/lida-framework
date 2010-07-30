/**
 * 
 */
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;

/**
 * This class implements the LidaTask Interface. This class should be used as the base class for all LidaTasks.
 * @author Javier Snaider
 */
public abstract class LidaTaskImpl extends ActivatibleImpl implements LidaTask {
	
	private static Logger logger= Logger.getLogger("lida.framework.tasks.LidaTask");

	private static int defaultTicksPerStep = 1;
	private long taskID;
	private int ticksPerStep = defaultTicksPerStep;
	private long nextExcecutionTickLap = defaultTicksPerStep;
	protected LidaTaskStatus status = LidaTaskStatus.WAITING;
	private Map<String, ? extends Object> parameters;
	private TaskSpawner ts;
	private long scheduledTick;
	private static long nextTaskID;
	
	public LidaTaskImpl() {
		this(defaultTicksPerStep,null);
	}
	public LidaTaskImpl(int ticksForCycle) {
		this(ticksForCycle,null);
	}
	public LidaTaskImpl(int ticksForCycle,TaskSpawner ts) {
		taskID = nextTaskID++;
		this.ts=ts;
		setNumberOfTicksPerStep(ticksForCycle);
	}
	
	/**
	 * @return the scheduledTick
	 */
	public long getScheduledTick() {
		return scheduledTick;
	}
	/**
	 * @param scheduledTick the scheduledTick to set
	 */
	public void setScheduledTick(long scheduledTick) {
		this.scheduledTick = scheduledTick;
	}
	
	/**
	 * @return the defaultTicksPerStep
	 */
	public static int getDefaultTicksPerStep() {
		return defaultTicksPerStep;
	}
	
	
	/**
	 * Sets the default number of ticks that is used in the constructor without this parameter.
	 * @param defaultTicksPerStep
	 */
	public static void setDefaultTicksPerStep(int defaultTicksPerStep) {
		if (defaultTicksPerStep > 0) {
			LidaTaskImpl.defaultTicksPerStep = defaultTicksPerStep;
		}
	}

	/** 
	 * This method is not supposed to be called directly nor overwritten.
	 * Overwrite the runThisLidaTask method.
	 * @see java.util.concurrent.Callable#call()
	 */
	public LidaTask call() {
		nextExcecutionTickLap=ticksPerStep;
		runThisLidaTask();
		
		if (ts != null) 
			ts.receiveFinishedTask(this);
		else 
			logger.log(Level.WARNING, "This task {1} doesn't have an assigned TaskSpawner",new Object[] { LidaTaskManager.getActualTick(), this });
		
		return this;
	}


	/**
	 * To be overridden by child classes. Overriding method should execute a
	 * handful of statements considered to constitute a single iteration of the
	 * task. For example, a codelet might look in a buffer for some
	 * representation and make a change to it in a single iteration.
	 * 
	 */
	protected abstract void runThisLidaTask();

	/**
	 *  If a task is canceled it cannot be restarted.
     *  So only set the status if the status is not CANCELED.
     *  
	 * @param status - the status to set
	 */
	public void setTaskStatus(LidaTaskStatus status) {
		if (this.status != LidaTaskStatus.CANCELED)
			this.status = status;
	}

	/**
	 * @return the status
	 */
	public LidaTaskStatus getStatus() {
		return status;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#getTaskId()
	 */
	public long getTaskId() {
		return taskID;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#setTaskID(long)
	 */
	public void setTaskID(long id) {
		taskID = id;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#getTicksPerStep()
	 */
	public int getTicksPerStep() {
		return ticksPerStep;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#setNumberOfTicksPerStep(int)
	 */
	public void setNumberOfTicksPerStep(int ticks) {
		if (ticks > 0){
			ticksPerStep = ticks;
			setNextExcecutionTickLap(ticks);
		}
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#reset()
	 */
	public void reset() {

	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#stopRunning()
	 */
	public void stopRunning() {
		setTaskStatus(LidaTaskStatus.CANCELED);
	}

//	/**
//	 * @param taskManager
//	 */
//	public void setTaskManager(LidaTaskManager taskManager) {
//		this.taskManager = taskManager;
//		if (taskID == 0) {
//			taskID = LidaTaskManager.getNextTaskID();
//		}
//	}
//
//	/**
//	 * @return the LidaTaskManager
//	 */
//	public LidaTaskManager getTaskManager(){
//		return taskManager;
//	}
	
	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#init(java.util.Map)
	 */
	public void init(Map<String, ? extends Object> parameters) {
		this.parameters = parameters;
		init();
	}

	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the LidaTask
	 */
	protected void init() {
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getParameter(java.lang.String)
	 */
	@Override
	public Object getParam(String name,Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
	

	/* 
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getTaskSpawner()
	 */
	public TaskSpawner getTaskSpawner() {		
		return ts;
	}

	/*
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#setTaskSpawner(edu.memphis.ccrg.lida.framework.TaskSpawner)
	 */
	public void setTaskSpawner(TaskSpawner ts) {
		this.ts=ts;		
	}
	
	public long getNextExcecutionTickLap() {		
		return nextExcecutionTickLap;
	}
	
	public void setNextExcecutionTickLap(long lapTick) {
		this.nextExcecutionTickLap=lapTick;	
	}	
}// class
