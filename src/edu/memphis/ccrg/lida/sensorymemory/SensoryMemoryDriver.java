package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class SensoryMemoryDriver extends GenericModuleDriver {
	
	private SensoryMemory sm;
	
	public SensoryMemoryDriver(SensoryMemory sm, FrameworkTaskManager timer){
		super( timer);
		this.sm = sm;
	}//constructor
		
	public void cycleStep(){
		sm.processSensors();		
	}
	
}//class SMDriver