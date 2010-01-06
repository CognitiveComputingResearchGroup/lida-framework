package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

public class SensoryMemoryDriver extends ModuleDriverImpl {
	
	private SensoryMemory sm;
	
	public SensoryMemoryDriver(SensoryMemory sm, int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm,ModuleName.SensoryMemoryDriver);
		this.sm = sm;
	}//constructor
		
	public SensoryMemoryDriver(){
		super(DEFAULT_TICKS_PER_CYCLE, null,ModuleName.SensoryMemoryDriver);
	}//constructor
		
	public void runThisDriver(){
		sm.processSensors();		
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return ModuleName.SensoryMemoryDriver + "";
	}
	
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof SensoryMemory
					&& module.getModuleName() == ModuleName.SensoryMemory) {
				sm =  (SensoryMemory) module;
			}
		}
	}
		
}//class SMDriver