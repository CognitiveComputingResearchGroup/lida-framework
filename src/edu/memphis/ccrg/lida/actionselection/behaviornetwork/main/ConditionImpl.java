/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;


/**
 * @author Javier Snaider
 *
 */
public class ConditionImpl extends ActivatibleImpl implements Condition {

	private double desirability;
	private Object id;
	private double weight = 1.0;
	
	
	/**
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
	public Object getId() {
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

	/**
	 * @param desirability the desirability to set
	 */
	public void setDesirability(double desirability) {
		this.desirability=desirability;
	}
		
	public boolean equals (Object o){
		if (o instanceof Condition){
			return id.equals(((Condition)o).getId());
		}
		return false;
	}
	
	public int hashCode(){
		return id.hashCode();
	}


	/**
	 * @param incidence the incidence to set
	 */
	@Override
	public void setWeight(double incidence) {
		this.weight = incidence;
	}

	/**
	 * @return the incidence
	 */
	@Override
	public double getWeight() {
		return weight;
	}
}
