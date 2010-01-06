package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;

public interface SensoryMemory extends LidaModule{

	public abstract void processSensors();
	
	public abstract Object getContent(String type, Object... parameters);
	
	public abstract void addSensoryMemoryListener(SensoryMemoryListener l);

}
