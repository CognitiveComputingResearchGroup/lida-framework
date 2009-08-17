package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass().getName());
	protected boolean keepRunning = true;
	protected LidaTaskManager taskManager;
	
	public ModuleDriverImpl(LidaTaskManager tm, int ticksPerCycle){
		super(ticksPerCycle);
		taskManager = tm;
	}
	//Only use this constructor if a LidaTaskManager is added later before running
	public ModuleDriverImpl(int ticksPerCycle){
		super(ticksPerCycle);
	}
	public ModuleDriverImpl(){
		super();
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
		runSingleProcessingStep();
	}
	
	public void setTaskManager(LidaTaskManager tm){
		taskManager = tm;
	}
	public LidaTaskManager getTaskManager(){
		return taskManager;
	}
}// class