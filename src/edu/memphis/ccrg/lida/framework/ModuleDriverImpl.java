package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass().getName());
	protected boolean keepRunning = true;

	public ModuleDriverImpl(int ticksPerCycle){
		super(ticksPerCycle);
	}
	
	public void run(){
		while(keepRunning){
			if (!LidaTaskManager.isInTicksMode()){
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
			Thread.sleep(LidaTaskManager.getTickDuration() * getTicksPerStep());
		}catch(InterruptedException e){
			stopRunning();
		}
	}
	protected abstract void runThisDriver();
	
	public synchronized void stopRunning(){
		super.stopRunning();
		keepRunning = false;
	}

}// class