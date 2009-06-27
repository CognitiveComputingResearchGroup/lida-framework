/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;


/**
 * @author Javier Snaider
 * 
 */
public class TimeOutTrigger implements BroadcastTrigger {

	/**
	 * 
	 */
	private long delay;
	private Timer timer;
	private TriggerTask tt;
	private TriggerListener gw;
	private String name="";

	private class TriggerTask extends TimerTask{

		@Override
		public void run() {
			//System.out.println("time trigger");
			gw.triggerBroadcast();			
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	public void checkForTrigger(Set<Coalition> coalitions) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#reset()
	 */
	public void reset() {
		if (tt != null) {
			tt.cancel();
		}
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#setUp(java.util.Map)
	 */
	public void setUp(Map<String, Object> parameters, TriggerListener gw) {
		this.gw=gw;
		Object o = parameters.get("delay");
		if ((o != null)&& (o instanceof Long)) {
			delay= (Long)o;
		}
		
		o = parameters.get("name");
		if ((o != null)&& (o instanceof String)) {
			name= (String)o;
		}
		timer=new Timer(name,true);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#start()
	 */
	public void start() {
		tt=new TriggerTask();
		timer.schedule(tt, delay);
	}
}
