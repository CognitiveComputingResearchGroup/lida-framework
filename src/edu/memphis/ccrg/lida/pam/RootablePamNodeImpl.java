package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.NodeType;
import edu.memphis.ccrg.lida.framework.shared.RootableNode;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableDesirableImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * A {@link PamNodeImpl} that is also a {@link RootableNode}.
 * @author Ryan J. McCall
 *
 */
public class RootablePamNodeImpl extends PamNodeImpl implements RootableNode,
		PamNode {

	private LearnableDesirableImpl learnedDesirable;
	private NodeType nodeType;
	
	/**
	 * Constructs a new {@link RootablePamNodeImpl}.
	 */
	public RootablePamNodeImpl(){
		super();
		learnedDesirable = new LearnableDesirableImpl();
	}
	
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public void setNodeType(NodeType t) {
		nodeType = t;
	}

	@Override
	public void decayDesirability(long t) {
		learnedDesirable.decayDesirability(t);
	}

	@Override
	public void exciteDesirability(double a) {
		learnedDesirable.exciteDesirability(a);
	}

	@Override
	public double getDesirability() {
		return learnedDesirable.getDesirability();
	}

	@Override
	public DecayStrategy getDesirabilityDecayStrategy() {
		return learnedDesirable.getDesirabilityDecayStrategy();
	}

	@Override
	public ExciteStrategy getDesirabilityExciteStrategy() {
		return learnedDesirable.getDesirabilityExciteStrategy();
	}

	@Override
	public double getTotalDesirability() {
		return learnedDesirable.getTotalDesirability();
	}

	@Override
	public void setDesirability(double d) {
		learnedDesirable.setDesirability(d);
	}

	@Override
	public void setDesirabilityDecayStrategy(DecayStrategy d) {
		learnedDesirable.setDesirabilityDecayStrategy(d);
	}

	@Override
	public void setDesirabilityExciteStrategy(ExciteStrategy s) {
		learnedDesirable.setDesirabilityExciteStrategy(s);
	}

}
