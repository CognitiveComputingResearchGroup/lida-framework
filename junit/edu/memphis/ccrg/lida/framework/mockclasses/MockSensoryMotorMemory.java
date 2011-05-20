package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.AgentAction;
import edu.memphis.ccrg.lida.sensorymotormemory.BasicSensoryMotorMemory;


public class MockSensoryMotorMemory extends BasicSensoryMotorMemory implements
		ActionSelectionListener {
	
	public boolean actionReceived = false;
	public AgentAction action;
	
	@Override
	public synchronized void receiveAction(AgentAction action) {
		if(action != null){
			actionReceived = true;
			this.action=action;
		}else{
			actionReceived = false;
		}
	}
	
}
