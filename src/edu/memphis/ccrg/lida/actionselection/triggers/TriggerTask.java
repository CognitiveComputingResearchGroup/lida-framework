package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;

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
	private Logger logger = Logger.getLogger("lida.actionselection.triggers");

	private String name;
	private TriggerListener as;
	public TriggerTask(int ticksForCycle,TriggerListener as,String name) {
		super(ticksForCycle);
		this.as=as;
		this.name=name;
	}

	@Override
	protected void runThisLidaTask() {
		logger.log(Level.FINE,name,LidaTaskManager.getActualTick());
		as.triggerActionSelection();
		setTaskStatus(LidaTaskStatus.FINISHED);
	}		
	public String toString(){
		return "TriggerTask "+name +" " + getTaskId();
	}


}//class