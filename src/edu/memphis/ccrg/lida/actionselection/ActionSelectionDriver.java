package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionDriver extends ModuleDriverImpl {

	private List<ActionSelectionTrigger> actionSelectionTriggers = new ArrayList<ActionSelectionTrigger>();
	public ActionSelectionDriver(){
    	super(DEFAULT_TICKS_PER_CYCLE, ModuleName.ActionSelectionDriver);
    }//constructor
	
    public ActionSelectionDriver(ActionSelection as, int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm,ModuleName.ActionSelectionDriver);
	}//constructor
    
	@Override
	public void runThisDriver() {
		start();
		setTaskStatus(LidaTaskStatus.FINISHED); //Runs only once
	}//method
	
	@Override
	public String toString() {
		return ModuleName.ActionSelectionDriver + "";
	}
	
	/**
	 * To register Triggers
	 * @param t a new Trigger
	 */
	public void addActionSelectionTrigger(ActionSelectionTrigger t){
		actionSelectionTriggers.add(t);
	}
	
	public void resetTriggers() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.reset();
		}
	}
	
	public void start() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.start();
		}
	}	
	
	public void newBehaviorEvent(Queue<Scheme> behaviors) {		
		for (ActionSelectionTrigger trigger : actionSelectionTriggers)
			trigger.checkForTrigger(behaviors);
	}// method
	
}//class
