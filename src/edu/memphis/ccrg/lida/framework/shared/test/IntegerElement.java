package edu.memphis.ccrg.lida.framework.shared.test;

/**
 * An element of the framework with an int id.
 * @author Ryan J. McCall
 */
public interface IntegerElement extends Nameable {

	/**
	 * Sets int id.
	 * @param id the int id
	 */
	public void setId(int id);
	/**
	 * Gets int id.
	 * @return the int id
	 */
	public int getId();

}
