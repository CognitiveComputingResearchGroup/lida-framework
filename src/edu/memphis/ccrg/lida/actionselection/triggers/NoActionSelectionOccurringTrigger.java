package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Map;
import java.util.Queue;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class NoActionSelectionOccurringTrigger implements ActionSelectionTrigger {
	
	/**
	 * How long since last broadcast before this trigger is activated
	 */
	private int delay;
	
	/**
	 * Java library class used to handle the timing
	 */
	private TriggerTask task;
	private String name="";
	private LidaTaskManager tm;
	private ActionSelectionDriver asd;

	/**
	 * @return the lidaTaskManager
	 */
	public LidaTaskManager getLidaTaskManager() {
		return tm;
	}

	/**
	 * @param lidaTaskManager the lidaTaskManager to set
	 */
	public void setLidaTaskManager(LidaTaskManager lidaTaskManager) {
		this.tm = lidaTaskManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#setUp(java.util.Map)
	 */
	public void setUp(Map<String, Object> parameters, ActionSelectionDriver asd) {
		this.asd=asd;
		Object o = parameters.get("delay");
		if ((o != null)&& (o instanceof Integer)) {
			delay= (Integer)o;
		}
		
		o = parameters.get("name");
		if ((o != null)&& (o instanceof String)) {
			name= (String)o;
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#start()
	 */
	public void start() {
						
		task=new TriggerTask(delay,asd,name);	
		asd.addTask(task);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	public void checkForTrigger(Queue<Scheme> behaviors) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#reset()
	 */
	public void reset() {
	
		if (task != null)
			asd.cancelTask(task);
		start();
	}

	@Override
	public void setUp(Map<String, Object> parameters, ActionSelection as) {
		// TODO Auto-generated method stub
		
	}
	
}
