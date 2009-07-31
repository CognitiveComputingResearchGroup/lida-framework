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
		taskManager.checkForStartOrPause();
		//If not is ticks Mode then business as usual.
		if (!LidaTaskManager.isTicksMode()){
			//System.out.println("not in ticks mode");
			runOneStep();
		}else if(hasEnoughTicks()){
			//System.out.println("use ticks");
			useOneCycleOfTicks();
			runOneStep();
		}
		setTaskStatus(LidaTask.RUNNING);
		//System.out.println("module driver impl, run, setting task status to run " + LidaTask.RUNNING);
		
		return;
	}// method
	
	private void runOneStep(){
		runDriverOneProcessingStep();
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(taskManager.getSleepTime() * getNumberOfTicksPerCycle());
		}catch (InterruptedException e){
			stopRunning();
		}
	}

	public void stopRunning() {
		setTaskStatus(LidaTask.CANCELLED);
		//System.out.println("module driver impl setting task status to cancelled " + LidaTask.CANCELLED);
		super.stopRunning();
		logger.info("Driver stopped\n");
	}// method

}// class