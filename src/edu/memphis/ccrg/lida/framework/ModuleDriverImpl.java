package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass().getName());
	protected boolean keepRunning = true;
	private LidaTaskManager taskManager;

	public ModuleDriverImpl(int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm);
		taskManager = tm;
	}
	
	public void run(){
		while(keepRunning){
			if (!taskManager.isInTicksMode()){
				sleep();
				runThisDriver();
			}else if(hasEnoughTicks()){
				useOneStepOfTicks();
				sleep();
				runThisDriver();
			}
		}
	}
	private void sleep(){
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(taskManager.getTickDuration() * getTicksPerStep());
		}catch(InterruptedException e){
			stopRunning();
		}
	}
	protected abstract void runThisDriver();
	
	public synchronized void stopRunning(){
		super.stopRunning();
		keepRunning = false;
	}

	/**
	 * Used by EnvironmentImpl since an end user would not have a 
	 * LidaTaskManager Object when creating the <? extends EnvironmentImpl> object.
	 * @param taskManager
	 */
	public void setTaskManager(LidaTaskManager taskManager) {
		super.setTaskManager(taskManager);
		this.taskManager = taskManager;
	}
	public LidaTaskManager getTaskManager() {
		return taskManager;
	}

}// class