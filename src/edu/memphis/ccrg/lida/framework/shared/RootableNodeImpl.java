package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.framework.shared.activation.DesirableImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;


/**
 * Default implementation of {@link RootableNode}
 * @author Ryan J. McCall
 *
 */
public class RootableNodeImpl extends NodeImpl implements RootableNode {

	private NodeType nodeType;
	private DesirableImpl desirable;
	
	/**
	 * Constructs a new {@link RootableNodeImpl}.
	 */
	public RootableNodeImpl(){
		super();
		desirable = new DesirableImpl();
	}
	
	//RootableNode
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public void setNodeType(NodeType t) {
		nodeType = t;
	}

	//Desirable
	@Override
	public double getDesirability() {
		return desirable.getDesirability();
	}

	@Override
	public void setDesirability(double d) {
		desirable.setDesirability(d);
	}

	@Override
	public double getTotalDesirability() {
		return desirable.getTotalDesirability();
	}
	
	//NodeImpl
	@Override
	public void updateNodeValues(Node n){
		if(n instanceof RootableNode){
			RootableNode rn = (RootableNode) n;
			desirable.setDesirability(rn.getDesirability());
		}
	}
	
	//Object
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RootableNodeImpl) {
            return getId() == ((RootableNodeImpl) obj).getId();
        }
        return false;
    }

	@Override
	public void decayDesirability(long t) {
		desirable.decayDesirability(t);
	}

	@Override
	public void exciteDesirability(double a) {
		desirable.exciteDesirability(a);
	}

	@Override
	public DecayStrategy getDesirabilityDecayStrategy() {
		return desirable.getDesirabilityDecayStrategy();
	}

	@Override
	public ExciteStrategy getDesirabilityExciteStrategy() {
		return desirable.getDesirabilityExciteStrategy();
	}

	@Override
	public void setDesirabilityDecayStrategy(DecayStrategy d) {
		desirable.setDesirabilityDecayStrategy(d);
	}

	@Override
	public void setDesirabilityExciteStrategy(ExciteStrategy s) {
		desirable.setDesirabilityExciteStrategy(s);
	}
}
