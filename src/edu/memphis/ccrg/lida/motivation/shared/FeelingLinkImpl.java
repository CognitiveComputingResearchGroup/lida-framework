package edu.memphis.ccrg.lida.motivation.shared;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;

public class FeelingLinkImpl extends LinkImpl implements Valenceable {
	
	private ValenceableImpl valence = new ValenceableImpl();

	@Override
	public int getValenceSign() {
		return valence.getValenceSign();
	}

	@Override
	public void setValenceSign(int s) {
		valence.setValenceSign(s);
	}
	
	@Override
	public void updateLinkValues(Link l){
		super.updateLinkValues(l);
		if(l instanceof Valenceable){
			Valenceable otherLink = (Valenceable) l;
			valence.setValenceSign(otherLink.getValenceSign());
		}
	}
}