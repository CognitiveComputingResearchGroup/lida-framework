package edu.memphis.ccrg.lida.framework;

import java.util.logging.Logger;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger("lida.framework.ModuleDriverImpl");
	protected boolean keepRunning = true;

	public ModuleDriverImpl(int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm);
	}
	
	public void run(){
		while(keepRunning){
			getTaskManager().checkForStartPause();
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
			Thread.sleep(getTaskManager().getTickDuration() * getTicksPerStep());
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