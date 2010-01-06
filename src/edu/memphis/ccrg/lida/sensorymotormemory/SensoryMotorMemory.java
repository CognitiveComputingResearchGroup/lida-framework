package edu.memphis.ccrg.lida.sensorymotormemory;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

public interface SensoryMotorMemory  extends SensoryMemoryListener{

	public abstract void addSensoryMotorListener(SensoryMotorListener l);
}
