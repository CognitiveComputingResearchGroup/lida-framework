package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;

public interface SensoryMemory extends LidaModule, SensoryMotorListener{

	public abstract void processSensors();
	
	public abstract Object getContent(String type, Object... parameters);
	
	public abstract void addSensoryMemoryListener(SensoryMemoryListener l);

}
