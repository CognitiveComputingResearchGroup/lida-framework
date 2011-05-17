package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.AgentAction;
import edu.memphis.ccrg.lida.sensorymotormemory.BasicSensoryMotorMemory;


public class MockSensoryMotorMemory extends BasicSensoryMotorMemory implements
		ActionSelectionListener {
	
	private boolean actionReceived;
	
	@Override
	public synchronized void receiveAction(AgentAction action) {
		if(action != null){
			actionReceived = true;
		}else{
			actionReceived = false;
		}
	}
	public boolean actionReceived(){		
		return actionReceived;
	}

}
