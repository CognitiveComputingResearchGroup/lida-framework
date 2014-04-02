package edu.memphis.ccrg.lida.motivation.shared;

/**
 * A representation of a valence, either positive or negative.
 * @author Ryan J. McCall
 */
public interface Valenceable {

	/**
	 * Sets the sign of this feeling's valence.
	 * Zero or positive set the sign to positive, otherwise the sign is set to negative.
	 * @param s a valence sign code
	 */
	public void setValenceSign(int s);
	
	/**
	 * Gets the valence sign.
	 * @return 1 for a positive sign; -1 for a negative sign.
	 */
	public int getValenceSign();
}