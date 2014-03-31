package edu.memphis.ccrg.lida.motivation.pam;

import edu.memphis.ccrg.lida.motivation.shared.Valenceable;
import edu.memphis.ccrg.lida.motivation.shared.ValenceableImpl;
import edu.memphis.ccrg.lida.pam.PamLinkImpl;

public class FeelingPamLinkImpl extends PamLinkImpl implements Valenceable {

	private ValenceableImpl valence = new ValenceableImpl();

	@Override
	public int getValenceSign() {
		return valence.getValenceSign();
	}

	@Override
	public void setValenceSign(int s) {
		valence.setValenceSign(s);
	}
	
}