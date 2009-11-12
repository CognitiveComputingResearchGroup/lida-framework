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
	private long delay;
	
	/**
	 * Java library class used to handle the timing
	 */
	private Timer timer;
	private TriggerTask tt;
	private TriggerListener gw;
	private String name="";
	
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

	/**
	 * TriggerTask is executed when the Timer object goes off
	 * In this case the global workspace is told to trigger 
	 * the broadcast
	 *
	 */
	private class TriggerTask extends TimerTask{
		@Override
		public void run() {
			gw.triggerBroadcast();			
		}		
	}//class
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	public void checkForTrigger(Set<Coalition> coalitions) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#reset()
	 */
	public void reset() {
		if (tt != null)
			tt.cancel();
		start();
	}

}//class