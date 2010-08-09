package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.actionselection.triggers.TriggerListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;


public class ActionSelectionDriver extends ModuleDriverImpl implements TriggerListener{

	private ActionSelection net;	
    
    public ActionSelectionDriver(){
    	super(DEFAULT_TICKS_PER_CYCLE, ModuleName.ActionSelectionDriver);
    }
	
	@Override
	public void runThisDriver() {
		
		net.selectAction();
		//TODO perform this inside BehaviorNetwork
		net.sendAction();
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

	@Override
	public void triggerActionSelection() {	
		
	}
	
	/**
	 * To register Triggers
	 * @param t a new Trigger
	 */
	public void addActionSelectionTrigger(ActionSelectionTrigger t){
		
	}
	
}//class
