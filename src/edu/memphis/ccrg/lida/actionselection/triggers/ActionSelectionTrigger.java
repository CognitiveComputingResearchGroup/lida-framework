package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Map;
import java.util.Queue;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * A Trigger determines when a new Broadcast must be triggered.
 * Its start method should be invoked once (most of the cases when the ActionSelection starts)
 * Its command method is called every time a new coalition enters the AS.
 * It can implement a timer if it needed
 * See default Triggers as examples of implementation.
 * 
 * @author Javier Snaider
 *
 */
public interface ActionSelectionTrigger {
	
	/**
	 * This method is a generic way to setup the Trigger. It should be called when 
	 * the trigger is created.
	 * @param parameters a map for generic parameters.
	 * @param as A TriggerListener. Most of the cases is the same class that 
	 * implements ActionSelection Interface.
	 */
	public void setUp (Map<String,Object> parameters,ActionSelection as);
	/**
	 * Each time a new Coalition is put in the GW, this method is called for all registered the Triggers.
	 * @param coalitions All the coalitions in the GW.
	 */
	public void checkForTrigger (Queue<Scheme> behaviors);
	/**
	 * To reset the Trigger. Its called each time a new Broadcast is Triggered.
	 */
	public void reset();
	/**
	 * To start the Trigger behavior
	 */
	public void start();
}

