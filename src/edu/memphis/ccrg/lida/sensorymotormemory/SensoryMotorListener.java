package edu.memphis.ccrg.lida.sensorymotormemory;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.ModuleListener;

public interface SensoryMotorListener extends ModuleListener{
	
	public abstract void receiveAction(LidaAction a);

}
