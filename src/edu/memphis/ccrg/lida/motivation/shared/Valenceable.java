package edu.memphis.ccrg.lida.motivation.shared;

/**
 * A representation a valence either positive or negative.
 * @author Ryan J. McCall
 */
public interface Valenceable {

	/**
	 * Sets the sign of this feeling's valence.
	 * @param s
	 */
	public void setValenceSign(int s);
	
	/**
	 * Gets the sign of this feeling's valence.
	 * @return
	 */
	public int getValenceSign();
}