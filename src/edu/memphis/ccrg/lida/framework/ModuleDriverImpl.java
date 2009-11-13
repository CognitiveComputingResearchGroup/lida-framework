package edu.memphis.ccrg.lida.framework;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {

	public ModuleDriverImpl(int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm);
	}

	protected void runThisLidaTask(){
		runThisDriver();
	}
	protected abstract void runThisDriver();
	
}// class