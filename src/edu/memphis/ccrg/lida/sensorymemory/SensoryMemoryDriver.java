package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class SensoryMemoryDriver extends ModuleDriverImpl {
	
	private SensoryMemory sm;
	
	public SensoryMemoryDriver(SensoryMemory sm, LidaTaskManager timer){
		super( timer);
		this.sm = sm;
	}//constructor
		
	public void runDriverOneProcessingStep(){
		sm.processSensors();		
	}
	
}//class SMDriver