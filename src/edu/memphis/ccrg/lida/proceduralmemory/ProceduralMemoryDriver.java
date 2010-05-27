package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public class ProceduralMemoryDriver extends ModuleDriverImpl {

	private ProceduralMemory proceduralMemory;
	private static final int DEFAULT_TICKS_PER_CYCLE = 10;

	public ProceduralMemoryDriver(ProceduralMemory pm, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm,ModuleName.ProceduralDriver);
		proceduralMemory = pm;
	}
	
	public ProceduralMemoryDriver() {
		super(DEFAULT_TICKS_PER_CYCLE,ModuleName.ProceduralDriver);
	}

	/**
	 * 
	 */
	@Override
	public void runThisDriver() {
			proceduralMemory.activateSchemes();
	}// method

	@Override
	public String toString() {
		return ModuleName.ProceduralDriver + "";
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof ProceduralMemory
					&& module.getModuleName() == ModuleName.ProceduralMemory) {
				proceduralMemory = (ProceduralMemory) module;
			}
		}
	}
	
}// class