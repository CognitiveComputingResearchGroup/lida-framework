/**
 * 
 */
package edu.memphis.ccrg.globalworkspace.triggers;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.memphis.ccrg.globalworkspace.Coalition;
import edu.memphis.ccrg.globalworkspace.Trigger;
import edu.memphis.ccrg.globalworkspace.TriggerListener;


/**
 * @author Javier Snaider
 * 
 */
public class TimeOutTrigger implements Trigger {

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
			gw.trigger();			
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.globalworkspace.Trigger#command(java.util.Set, double)
	 */
	public void command(Set<Coalition> coallitions) {
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
	public void setUp(Map<String, Object> parameters,TriggerListener gw) {
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
