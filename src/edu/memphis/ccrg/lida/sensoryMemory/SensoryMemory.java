package edu.memphis.ccrg.lida.sensoryMemory;

public interface SensoryMemory{

	void processSensors();
	
	Object getContent(String type,Object... parameters);

}
