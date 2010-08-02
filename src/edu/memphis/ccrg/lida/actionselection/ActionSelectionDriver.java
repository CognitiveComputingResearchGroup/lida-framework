package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;

public class ActionSelectionDriver extends ModuleDriverImpl{

	private ActionSelection net;
    
    public ActionSelectionDriver(){
    	super(DEFAULT_TICKS_PER_CYCLE, ModuleName.ActionSelectionDriver);
    }
	
	@Override
	protected void runThisDriver() {
		net.selectAction();
		//TODO perform this inside BehaviorNetwork
		net.sendAction(net.getFiredBehavior().getActionId());
	}//method
	
	@Override
	public String toString() {
		return ModuleName.ActionSelectionDriver + "";
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if(module instanceof ActionSelection)
			net = (ActionSelection) module;
	}
	
}//class
