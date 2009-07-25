package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class GenericModuleDriver extends TaskSpawnerImpl implements
		ModuleDriver {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	protected boolean keepRunning = true;
	protected LidaTaskManager timer;

	public GenericModuleDriver(LidaTaskManager timer) {
		super();
		this.timer = timer;
	}

	public void run() {
		while (keepRunning) {
			timer.checkForStartPause();
			if (LidaTaskManager.isTicksMode()) {
				if(hasEnoughTicks()){
					consumeTicksForACycle();
				}else{
					continue;
				}
			} else {
				try {
					Thread.sleep(timer.getSleepTime());
				} catch (InterruptedException e) {
					stopRunning();
				}
			}
			cycleStep();
		}// while
	}// method run

	public void stopRunning() {
		keepRunning = false;
		super.stopRunning();
		logger.info("Driver stopped");
	}// method

}// class