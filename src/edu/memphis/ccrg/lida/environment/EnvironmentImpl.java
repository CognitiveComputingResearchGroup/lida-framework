package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;

public abstract class EnvironmentImpl extends ModuleDriverImpl implements Environment{

	public EnvironmentImpl() {
		super(10, ModuleName.Environment);
	}
	
	public EnvironmentImpl(int ticksPerCycle){
		super(ticksPerCycle, ModuleName.Environment);
	}
	
	
	public void addListener(ModuleListener listener) {
	}

	public void addModule(LidaModule lm) {
		
	}
	public LidaModule getSubmodule(ModuleName type) {
		return null;
	}
	public void decayModule(long ticks){
		
	}
	
	
}//class
