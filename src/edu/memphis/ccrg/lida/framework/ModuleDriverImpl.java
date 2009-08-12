package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	protected boolean keepRunning = true;
	protected LidaTaskManager taskManager;
	
	public ModuleDriverImpl(LidaTaskManager tm, int ticksPerCycle){
		super(ticksPerCycle);
		taskManager = tm;
	}

	public void run() {		
		//If not is ticks Mode then business as usual.
		if (!LidaTaskManager.isInTicksMode()){
			runOneStep();
		}else if(hasEnoughTicks()){
			useOneStepOfTicks();
			runOneStep();
		}
	}// method
	private void runOneStep(){
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(LidaTaskManager.getTickDuration() * getTicksPerStep());
		}catch (InterruptedException e){
			stopRunning();
		}
		runDriverOneProcessingStep();
	}
	
	public LidaTaskManager getTaskManager(){
		return taskManager;
	}
}// class