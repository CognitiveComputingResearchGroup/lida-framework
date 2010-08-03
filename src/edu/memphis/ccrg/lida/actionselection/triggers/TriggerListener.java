package edu.memphis.ccrg.lida.actionselection.triggers;

/**
 * This interface should be implemented by the class that should receive a trigger
 * notification. In general, it is the same class that implements ActionSelection interface.
 *  
 * @author Javier Snaider
 * 
 */
public interface TriggerListener {
	public void triggerActionSelection();

}
