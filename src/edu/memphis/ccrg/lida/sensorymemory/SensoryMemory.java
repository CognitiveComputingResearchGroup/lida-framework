package edu.memphis.ccrg.lida.sensorymemory;

public interface SensoryMemory{

	void processSensors();
	
	Object getContent(String type,Object... parameters);

}
