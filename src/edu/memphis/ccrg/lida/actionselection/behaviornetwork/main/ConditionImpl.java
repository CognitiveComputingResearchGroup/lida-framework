/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;

/**
 * Default implementation of {@link Condition}
 * @author Javier Snaider
 */
public class ConditionImpl extends ActivatibleImpl implements Condition {

	private double desirability;
	private Object id;
	private double weight = 1.0;
	
	/**
	 * 
	 * @param id the id of this Condition
	 * @param activation the initial activation
	 * @param desirability the initial desirability
	 */
	public ConditionImpl(Object id, double activation, double desirability) {
		this.id = id;
		setActivation(activation);
		this.desirability = desirability;
	}
	
	@Override
	public Object getConditionId() {
		return id;
	}

	@Override
	public double getDesirability() {
		return desirability;
	}
	
	@Override
	public double getNetDesirability() {
		double net = desirability - getActivation();
		if (net<0.0){
			net=0.0;
		}
		return net;
	}
	
	@Override
	public void setDesirability(double desirability) {
		this.desirability=desirability;
	}
	
	@Override
	public boolean equals (Object o){
		if (o instanceof Condition){
			return id.equals(((Condition)o).getConditionId());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return id.hashCode();
	}

	@Override
	public void setWeight(double w) {
		this.weight = w;
	}

	@Override
	public double getWeight() {
		return weight;
	}
}