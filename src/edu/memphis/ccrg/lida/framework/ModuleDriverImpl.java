package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl 
										  implements ModuleDriver {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	protected boolean keepRunning = true;
	protected LidaTaskManager timer;

	public ModuleDriverImpl(LidaTaskManager timer) {
		super();
		this.timer = timer;
	}

	public void run() {		
		timer.checkForStartPause();
		
		if (!LidaTaskManager.isTicksMode())
			runOneStep();
		else if(hasEnoughTicks()){
			consumeTicksForACycle();
			runOneStep();
		}
	
		setStatus(LidaTask.RUNNING);
		return;
	}// method
	
	private void runOneStep(){
		runDriverOneProcessingStep();
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(timer.getSleepTime() * getNumberOfTicksPerCycle());
		}catch (InterruptedException e){
			stopRunning();
		}
	}

	public void stopRunning() {
		setStatus(LidaTask.CANCELLED);
		super.stopRunning();
		logger.info("Driver stopped\n");
	}// method

}// class