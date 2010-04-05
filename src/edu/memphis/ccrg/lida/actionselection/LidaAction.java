package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * Encapsulation of an action to be executed.
 * Can be used as Flyweight objects.
 * 
 * @author ryanjmccall
 *
 */
public interface LidaAction {

	/**
	 * The actual action that should be performed.
	 * The action can interact directly with any module in LIDA, specially the
	 * SensoryMotorMemory.
	 */
	public abstract void performAction();
	
	/**
	 * @return the action content.
	 */
	public abstract Object getContent();
	
	/**
	 * @param content the content to set.
	 */
	public abstract void setContent(Object content);
	
	/**
	 * @return the action label.
	 */
	public abstract String getLabel();
	
	/**
	 * @param label the action label to set.
	 */
	public abstract void setLabel(String label);

	/**
	 * Sets an associated LidaModule.
	 * @param module the module to be associated.
	 */
	public abstract void setAssociatedModule(LidaModule module);
}
