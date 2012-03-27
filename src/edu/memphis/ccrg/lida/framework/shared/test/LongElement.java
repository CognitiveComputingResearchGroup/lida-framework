package edu.memphis.ccrg.lida.framework.shared.test;

/**
 * An element of the framework with an long id.
 * @author Ryan J. McCall
 */
public interface LongElement extends Nameable {

	/**
	 * Sets long id.
	 * @param id the long id
	 */
	public void setId(long id);
	/**
	 * Gets long id.
	 * @return the long id
	 */
	public long getId();
	
}
