/**
 * 
 */
package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

/**
 * @author Javier Snaider
 * 
 */
public class SMBackgroundTask extends LidaTaskImpl {

	private SensoryMemory sm;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl#runThisLidaTask()
	 */
	@Override
	protected void runThisLidaTask() {
		sm.runSensors();		
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module instanceof SensoryMemory) {
			sm = (SensoryMemory) module;
		}
	}
}
