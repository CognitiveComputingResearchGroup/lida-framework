package edu.memphis.ccrg.lida.actionselection;

/**
 * Encapsulation of an action to be executed.
 * @author ryanjmccall
 *
 */
public interface LidaAction {

	public abstract Object getContent();
	public abstract void setContent(Object o);
}
