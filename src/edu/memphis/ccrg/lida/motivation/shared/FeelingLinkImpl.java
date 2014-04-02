package edu.memphis.ccrg.lida.motivation.shared;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;

/**
 * A {@link Link} having a valence.
 * @author Ryan J McCall
 */
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