/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;


/**
 * This triggers activates if 'delay' milliseconds has passed without
 * a broadcast.
 * 
 * @author Javier Snaider
 * 
 */
public class NoBroadcastOccurringTrigger implements BroadcastTrigger {

	/**
	 * How long since last broadcast before this trigger is activated
	 */
	private int delay;
	
	/**
	 * Java library class used to handle the timing
	 */
	private TriggerTask task;
	private GlobalWorkspace gw;
	private String name="";
	private LidaTaskManager tm;

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
	public void setUp(Map<String, Object> parameters, GlobalWorkspace gw) {
		this.gw=gw;
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
		task=new TriggerTask(delay,gw,name);
		gw.addTask(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	public void checkForTrigger(Queue<Coalition> coalitions) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#reset()
	 */
	public void reset() {
		if (task != null)
			gw.cancelTask(task);
		start();
	}

}//class