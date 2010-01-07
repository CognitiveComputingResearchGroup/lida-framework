/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.LidaTaskStatus;

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
	private Logger logger = Logger.getLogger("lida.globalworkspace.triggers");

	private String name;
	private TriggerListener gw;
	public TriggerTask(int ticksForCycle,TriggerListener gw,String name) {
		super(ticksForCycle);
		this.gw=gw;
		this.name=name;
	}

	@Override
	protected void runThisLidaTask() {
		logger.log(Level.FINE,name,LidaTaskManager.getActualTick());
		gw.triggerBroadcast();
		setTaskStatus(LidaTaskStatus.FINISHED);
	}		
	public String toString(){
		return "TriggerTask "+name +" " + getTaskId();
	}


}//class
