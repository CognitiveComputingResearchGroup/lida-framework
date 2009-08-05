package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	protected boolean keepRunning = true;
	protected LidaTaskManager taskManager;

	public ModuleDriverImpl(LidaTaskManager tm) {
		super();
		taskManager = tm;
	}

	public void run() {		
		//If not is ticks Mode then business as usual.
		if (!LidaTaskManager.isTicksModeEnabled()){
			runOneStep();
		}else if(hasEnoughTicks()){
			useOneStepOfTicks();
			runOneStep();
		}
		//setTaskStatus(LidaTask.RUNNING);
		//System.out.println("module driver impl, run, setting task status to run " + LidaTask.RUNNING);
		
	}// method
	private void runOneStep(){
		runDriverOneProcessingStep();
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(taskManager.getTimeScale() * getNumberOfTicksPerStep());
		}catch (InterruptedException e){
			stopRunning();
		}
	}
	public LidaTaskManager getTaskManager(){
		return taskManager;
	}
}// class