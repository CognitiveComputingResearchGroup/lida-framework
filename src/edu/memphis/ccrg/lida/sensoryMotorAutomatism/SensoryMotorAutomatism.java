package edu.memphis.ccrg.lida.sensoryMotorAutomatism;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryListener;

public interface SensoryMotorAutomatism extends SensoryMemoryListener {

	public abstract void addSensoryMotorListener(SensoryMotorListener l);
}
