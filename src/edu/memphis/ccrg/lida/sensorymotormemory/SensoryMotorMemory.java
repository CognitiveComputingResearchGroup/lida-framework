package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.Map;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

public interface SensoryMotorMemory extends LidaModule, SensoryMemoryListener, ActionSelectionListener{

	public abstract void addSensoryMotorListener(SensoryMotorListener l);
	
	public abstract void executeAction(LidaAction a);

	public abstract void setActionMap(Map<Long, LidaAction> actionMap);
	
	public abstract void addAction(long actionId, LidaAction action);
}
