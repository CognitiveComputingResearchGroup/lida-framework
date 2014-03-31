package edu.memphis.ccrg.lida.motivation.shared;

/**
 * The default implementation of {@link Valenceable}.
 * @author Ryan J. McCall
 */
public class ValenceableImpl implements Valenceable {
	
	private static final int POSITIVE_VALENCE_SIGN = 1;
	private static final int NEGATIVE_VALENCE_SIGN = -1;
	private int valenceSign = POSITIVE_VALENCE_SIGN;

	@Override
	public int getValenceSign() {
		return valenceSign;
	}

	@Override
	public synchronized void setValenceSign(int s) {
		if(s >= 0){
			valenceSign = POSITIVE_VALENCE_SIGN;
		}else{
			valenceSign = NEGATIVE_VALENCE_SIGN;
		}
	}
}