package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;

public interface SensoryMemory extends LidaModule, SensoryMotorListener, Saveable {

	public abstract void runSensors();
	
	public abstract Object getContent(String type, Object... parameters);
	
	public abstract void addSensoryMemoryListener(SensoryMemoryListener l);

}
