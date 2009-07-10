package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class SensoryMemoryDriver extends GenericModuleDriver {
	
	private SensoryMemory sm;
	
	public SensoryMemoryDriver(SensoryMemory sm, FrameworkTimer timer){
		super( timer);
		this.sm = sm;
	}//constructor
		
	public void cycleStep(){
		sm.processSensors();		
	}
	
}//class SMDriver