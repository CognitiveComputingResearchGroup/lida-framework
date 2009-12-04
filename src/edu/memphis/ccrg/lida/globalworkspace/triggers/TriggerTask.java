/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

/**
 * @author Javier
 *
 */
/**
 * TriggerTask is executed when the Timer object goes off
 * In this case the global workspace is told to trigger 
 * the broadcast
 *
 */
public class TriggerTask extends LidaTaskImpl{

	private TriggerListener gw;
	public TriggerTask(int ticksForCycle, LidaTaskManager tm,TriggerListener gw) {
		super(ticksForCycle, tm);
		this.gw=gw;
	}

	@Override
	protected void runThisLidaTask() {
		gw.triggerBroadcast();						
	}		
	public String toString(){
		return "TriggerTask " + getTaskId();
	}


}//class
