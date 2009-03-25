package edu.memphis.ccrg.lida.shared;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class NodeImpl implements Node {

	public Node copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void decay() {
		// TODO Auto-generated method stub

	}

	public void excite(double amount) {
		// TODO Auto-generated method stub

	}

	public double getCurrentActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getIdentifier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getImportance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Node getReferencedNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRelevant() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub

	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub

	}

	public void setValue(Map<String, Object> values) {
		// TODO Auto-generated method stub

	}

	public void setReferencedNode(Node n) {
		// TODO Auto-generated method stub

	}

	public void synchronize() {
		// TODO Auto-generated method stub

	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setActivation(double d) {
		// TODO Auto-generated method stub
		
	}

	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	public void setLabel(String label) {
		// TODO Auto-generated method stub
		
	}

	public int getLayerDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void printActivationString() {
		// TODO Auto-generated method stub
		
	}

	public void decay(DecayBehavior decayBehavior) {
		// TODO Auto-generated method stub
		
	}

}
