package edu.memphis.ccrg.lida.sensorymemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;

public abstract class SensoryMemoryImpl extends LidaModuleImpl implements SensoryMemory, SensoryMotorListener{
	
	private List<SensoryMemoryListener> listeners = new ArrayList<SensoryMemoryListener>();
	protected Environment environment;
	
	public SensoryMemoryImpl(){
		super(ModuleType.SensoryMemory);

	}
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		listeners.add(l);		
	}
	
	public void setEnvironment(Environment environment2){
		environment = environment2;
	}
	
	public abstract Object getContent(String type, Object... parameters);

	public abstract void processSensors();	
	
}//class
