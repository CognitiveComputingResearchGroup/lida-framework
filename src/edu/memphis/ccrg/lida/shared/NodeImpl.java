package edu.memphis.ccrg.lida.shared;

import java.util.Map;

import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

/**
 * Default Node Implementation
 * 
 * @author Javier Snaider
 * 
 */
public class NodeImpl implements Node {

	private long id;
	private double activation;
	private ExciteBehavior eb;
	private DecayBehavior db;
	protected PamNode refNode;
	private String label = "";

	public NodeImpl() {
	}

	public NodeImpl(NodeImpl n) {
		this.id = n.id;
		this.activation = n.activation;
		this.eb = n.eb;
		this.db = n.db;
		this.refNode = n.refNode;
	}

	public void decay() {
		if (db != null) {
			double newActivation = db.decay(activation); 
			synchronized(this){
				activation = newActivation;
			}
		}
	}
	
	/**
	 * The current activation of this node is increased by the
	 * excitation value.
	 * 
	 * @param   excitation the value to be added to the current activation of
	 *          this node
	 */
	public void excite(double excitation) {
		//System.out.println(excitation);
		if (eb != null) {
			//System.out.println("before" + activation);
			double newActivation = eb.excite(activation, excitation);
			synchronized(this){
				activation = newActivation;
			}			
			//System.out.println("after" + activation);
		}
	}

	public double getImportance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PamNode getReferencedNode() {
		return refNode;
	}

	public boolean isOverThreshold() {
		return false;
	}

	public void setDecayBehavior(DecayBehavior db) {
		this.db = db;
	}

	public void setExciteBehavior(ExciteBehavior eb) {
		this.eb = eb;
	}

	public void setValue(Map<String, Object> values) {
		// TODO Auto-generated method stub
	}

	public void setReferencedNode(PamNode n) {
		refNode = n;
	}

	public DecayBehavior getDecayBehavior() {
		return db;
	}

	public ExciteBehavior getExciteBehavior() {
		return eb;
	}

	public synchronized void setActivation(double d) {
		//System.out.println("setting " + activation);
		this.activation = d;
	}

	public double getActivation() {
		//System.out.println("getting " + activation);
		return this.activation;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label=label;
	}

	public String getIds() {
		return "" + id;
	}

	/**
	 * This method compares this object with any kind of Node. returns true if
	 * the id of both are the same.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		return ((Node) o).getId() == id;
	}

	public int hashCode() {
		return ((int) id % 31);
	}
}
