package edu.memphis.ccrg.lida.actionselection;

/**
 * Encapsulation of an action to be executed.
 * @author ryanjmccall
 *
 */
public interface LidaAction {

	/**
	 * 
	 */
	public abstract void performAction();
	
	/**
	 * 
	 * @return
	 */
	public abstract Object getContent();
	
	/**
	 * 
	 * @param o
	 */
	public abstract void setContent(Object o);
}
