package edu.memphis.ccrg.lida.motivation.shared;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;


/**
 * The default implementation of {@link FeelingNode}.
 * @author Ryan J. McCall
 */
public class FeelingNodeImpl extends NodeImpl implements FeelingNode {

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
		return getValenceSign()*getActivation();
	}

	@Override
	public boolean isDrive() {
		return isDrive;
	}

	@Override
	public synchronized void setDrive(boolean b) {
		isDrive = b;
	}
	
	@Override
	public void updateNodeValues(Node n){
		super.updateNodeValues(n);
		if (n instanceof FeelingNode) {
			FeelingNode otherNode = (FeelingNode) n;
			isDrive = otherNode.isDrive();
			valence.setValenceSign(otherNode.getValenceSign());
		}
	}
}