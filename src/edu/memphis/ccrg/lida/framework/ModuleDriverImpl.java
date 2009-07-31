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
		if (!LidaTaskManager.isTicksMode() || hasEnoughTicks()) {
			
			if(LidaTaskManager.isTicksMode())
				consumeTicksForACycle();
			
			cycleStep();
			try {
				// Sleeps a lap proportional for each task
				Thread.sleep(timer.getSleepTime() * getTicksForCycle());
			}catch (InterruptedException e){
				stopRunning();
			}
		}
		setStatus(LidaTask.RUNNING);
		return;
	}// method

	public void stopRunning() {
		setStatus(LidaTask.CANCELLED);
		super.stopRunning();
		logger.info("Driver stopped");
	}// method

}// class