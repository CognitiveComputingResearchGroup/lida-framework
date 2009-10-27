/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;

/**
 * @author Javier Snaider
 */
public abstract class LidaTaskImpl extends ActivatibleImpl implements LidaTask {

	private static int defaultTicksPerStep = 1;
	private long taskID;
	private int ticksPerStep = defaultTicksPerStep;
	private int accumulatedTicks;
	protected int status = LidaTask.WAITING;
	private LidaTaskManager taskManager;
	private Map<String, Object> parameters;
	
	public LidaTaskImpl(LidaTaskManager tm) {
		this(defaultTicksPerStep, tm);
	}

	public LidaTaskImpl(int ticksForCycle, LidaTaskManager tm) {
		taskManager = tm;
		taskID = LidaTaskManager.getNextTaskID();
		setNumberOfTicksPerStep(ticksForCycle);
	}
	
	/**
	 * @return the defaultTicksPerStep
	 */
	public static int getDefaultTicksPerStep() {
		return defaultTicksPerStep;
	}
	public static void setDefaultTicksPerStep(int defaultTicksPerStep) {
		if (defaultTicksPerStep > 0) {
			LidaTaskImpl.defaultTicksPerStep = defaultTicksPerStep;
		}
	}

	public LidaTask call() {
		if (!LidaTaskManager.isInTicksMode()) {
			sleep();
			runThisLidaTask();
		} else if (hasEnoughTicks()) {
			useOneStepOfTicks();
			sleep();
			runThisLidaTask();
		}
		return this;
	}

	private void sleep() {
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(taskManager.getTickDuration() * getTicksPerStep());
		} catch (InterruptedException e) {
			stopRunning();
		}
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

	public long getTaskId() {
		return taskID;
	}

	public void setTaskID(long id) {
		taskID = id;
	}

	public int getTicksPerStep() {
		return ticksPerStep;
	}

	public void setNumberOfTicksPerStep(int ticks) {
		if (ticks > 0)
			ticksPerStep = ticks;
	}

	public void addTicks(int ticks) {
		this.accumulatedTicks = accumulatedTicks + ticks;
	}

	public int getAccumulatedTicks() {
		return accumulatedTicks;
	}

	public boolean useOneStepOfTicks() {
		if (accumulatedTicks >= ticksPerStep) {
			accumulatedTicks = accumulatedTicks - ticksPerStep;
			return true;
		}
		return false;
	}

	public boolean hasEnoughTicks() {
		return accumulatedTicks >= ticksPerStep;
	}

	public void reset() {

	}

	public void stopRunning() {
		setTaskStatus(LidaTask.CANCELLED);
	}

	public void setTaskManager(LidaTaskManager taskManager) {
		this.taskManager = taskManager;
		if (taskID == 0) {
			taskID = LidaTaskManager.getNextTaskID();
		}
	}

	protected LidaTaskManager getTaskManager(){
		return taskManager;
	}
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
	
}// class
