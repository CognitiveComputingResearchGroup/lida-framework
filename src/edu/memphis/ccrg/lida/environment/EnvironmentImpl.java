package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public abstract class EnvironmentImpl extends ModuleDriverImpl implements Environment{
	
	public EnvironmentImpl() {
		super(10);
	}
	
	public EnvironmentImpl(int ticksPerCycle){
		super(ticksPerCycle);
	}

}//class
