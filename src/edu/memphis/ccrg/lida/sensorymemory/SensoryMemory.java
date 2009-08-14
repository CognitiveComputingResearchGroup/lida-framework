package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;

public interface SensoryMemory{

	public abstract void setEnvironment(EnvironmentImpl environ);
	
	public abstract void processSensors();
	
	public abstract Object getContent(String type, Object... parameters);
	
	public abstract void addSensoryMemoryListener(SensoryMemoryListener l);

}
