package edu.memphis.ccrg.lida.sensorymotormemory;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

public interface SensoryMotorMemory extends SensoryMemoryListener, ActionSelectionListener{

	public abstract void addSensoryMotorListener(SensoryMotorListener l);
}
