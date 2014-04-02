package edu.memphis.ccrg.lida.motivation.pam;

import edu.memphis.ccrg.lida.motivation.shared.FeelingNode;
import edu.memphis.ccrg.lida.motivation.shared.ValenceableImpl;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * A {@link PamNode} with a valence and a flag signifying whether or not it is a drive feeling.
 * @author Ryan J McCall
 */
public class FeelingPamNodeImpl extends PamNodeImpl implements FeelingNode {

	private ValenceableImpl valence = new ValenceableImpl();
	private boolean isDrive;

	@Override
	public int getValenceSign() {
		return valence.getValenceSign();
	}

	@Override
	public void setValenceSign(int s) {
		valence.setValenceSign(s);
	}
	
	@Override
	public double getAffectiveValence() {
		return getValenceSign()*getTotalActivation();
	}
	
	@Override
	public boolean isDrive() {
		return isDrive;
	}

	@Override
	public void setDrive(boolean b) {
		isDrive = b;
	}
}