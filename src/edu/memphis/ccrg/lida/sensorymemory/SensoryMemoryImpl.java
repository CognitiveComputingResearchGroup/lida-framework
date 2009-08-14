package edu.memphis.ccrg.lida.sensorymemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;

public abstract class SensoryMemoryImpl implements SensoryMemory, SensoryMotorListener{
	
	private List<SensoryMemoryListener> listeners = new ArrayList<SensoryMemoryListener>();
	@SuppressWarnings("unused")
	protected EnvironmentImpl environment;
	
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		listeners.add(l);		
	}
	
	public void setEnvironment(EnvironmentImpl environ){
		environment = environ;
	}
	
	public abstract Object getContent(String type, Object... parameters);

	public abstract void processSensors();	
	
}//class
