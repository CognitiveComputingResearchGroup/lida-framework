package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Map;
import java.util.Queue;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * A Trigger determines when a new action selection must be triggered.
 * Its start method should be invoked once (most of the cases when the ActionSelection starts)
 * Its command method is called every time a new behavior enters the AS.
 * It can implement a timer if it needed
 * See default Triggers as examples of implementation.
 *  
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
	 * Each time a new behavior is put in the AS, this method is called for all registered the Triggers.
	 * @param behaviors All the behaviors in the AS.
	 */
	public void checkForTrigger (Queue<Scheme> behaviors);
	/**
	 * To reset the Trigger. Its called each time a new Action Selection is Triggered.
	 */
	public void reset();
	/**
	 * To start the Trigger behavior
	 */
	public void start();
}

