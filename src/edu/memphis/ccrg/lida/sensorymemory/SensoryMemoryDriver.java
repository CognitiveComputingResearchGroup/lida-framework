package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class SensoryMemoryDriver extends GenericModuleDriver {
	
	private SensoryMemory sm;
	
	public SensoryMemoryDriver(SensoryMemory sm, LidaTaskManager timer){
		super( timer);
		this.sm = sm;
	}//constructor
		
	public void cycleStep(){
		sm.processSensors();		
	}
	
}//class SMDriver