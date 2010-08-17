package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;


public class ActionSelectionDriver extends ModuleDriverImpl {

	private ActionSelection net;	
	private List<ActionSelectionTrigger> actionSelectionTriggers = new ArrayList<ActionSelectionTrigger>();
	    
    public ActionSelectionDriver(){
    	super(DEFAULT_TICKS_PER_CYCLE, ModuleName.ActionSelectionDriver);
    }
	
	@Override
	public void runThisDriver() {
		net.selectAction();
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

	
	/**
	 * To register Triggers
	 * @param t a new Trigger
	 */
	public void addActionSelectionTrigger(ActionSelectionTrigger t){
		
	}
	
	private void resetTriggers() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.reset();
		}
	}
	
}//class
