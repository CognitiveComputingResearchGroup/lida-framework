/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;

/**
 * This class implements the LidaTask Interface. This class should be used as the base class for all LidaTasks.
 * @author Javier Snaider
 */
public abstract class LidaTaskImpl extends ActivatibleImpl implements LidaTask {

	private static int defaultTicksPerStep = 1;
	private long taskID;
	private int ticksPerStep = defaultTicksPerStep;
	protected int status = LidaTask.WAITING;
	private LidaTaskManager taskManager;
	private Map<String, Object> parameters;
	private TaskSpawner ts;
	
	public LidaTaskImpl(LidaTaskManager tm) {
		this(defaultTicksPerStep, tm,null);
	}
	public LidaTaskImpl(int ticksForCycle,LidaTaskManager tm) {
		this(ticksForCycle, tm,null);
	}

	public LidaTaskImpl(int ticksForCycle, LidaTaskManager tm,TaskSpawner ts) {
		taskManager = tm;
		taskID = LidaTaskManager.getNextTaskID();
		this.ts=ts;
		setNumberOfTicksPerStep(ticksForCycle);
		
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
	 * Overwrite the run ThisLidaTask.
	 * @see java.util.concurrent.Callable#call()
	 */
	public LidaTask call() {
		
		runThisLidaTask();

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
	 * @param status
	 *            the status to set
	 */
	public void setTaskStatus(int status) {
		// If a task is canceled it cannot be restarted.
		// So only set the status if the status is not CANCELED.
		if (this.status != LidaTask.CANCELLED)
			this.status = status;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getTaskId()
	 */
	public long getTaskId() {
		return taskID;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#setTaskID(long)
	 */
	public void setTaskID(long id) {
		taskID = id;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getTicksPerStep()
	 */
	public int getTicksPerStep() {
		return ticksPerStep;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#setNumberOfTicksPerStep(int)
	 */
	public void setNumberOfTicksPerStep(int ticks) {
		if (ticks > 0)
			ticksPerStep = ticks;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#reset()
	 */
	public void reset() {

	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#stopRunning()
	 */
	public void stopRunning() {
		setTaskStatus(LidaTask.CANCELLED);
	}

	/**
	 * @param taskManager
	 */
	public void setTaskManager(LidaTaskManager taskManager) {
		this.taskManager = taskManager;
		if (taskID == 0) {
			taskID = LidaTaskManager.getNextTaskID();
		}
	}

	/**
	 * @return the LidaTaskManager
	 */
	protected LidaTaskManager getTaskManager(){
		return taskManager;
	}
	
	/**
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#init(java.util.Map)
	 */
	public void init(Map<String, Object> parameters) {
		this.parameters = parameters;
		init();
	}

	protected void init() {
	}

	public Object getParameter(String name) {
		Object res = null;
		if (parameters != null) {
			res = parameters.get(name);
		}
		return res;
	}
	public abstract String toString();
	
	//TODO: Changer the status by an enum
	
	public String getStatusString(){
		String res = "--";
		switch(this.status){
		case LidaTask.RUNNING:
			res = "Running";
			break;
		case LidaTask.WAITING_TO_RUN:
			res = "Waiting to Run";
			break;
		case LidaTask.WAITING:
			res = "Waiting";
			break;
		case LidaTask.TO_RESET:
			res = "To reset";
			break;
		case LidaTask.FINISHED_WITH_RESULTS:
			res = "Finished w results";
			break;
		case LidaTask.FINISHED:
			res = "Finished";
			break;
		case LidaTask.CANCELLED:
			res = "Cancelled";
			break;
		}
		return res;
	}//method

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
	
}// class
