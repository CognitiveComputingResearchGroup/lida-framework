package edu.memphis.ccrg.lida.shared;

import java.util.Map;

import edu.memphis.ccrg.lida.perception.PamNode;
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
	private String label;

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
			activation = db.decay(activation);
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
		if (eb != null) {
			activation = eb.excite(activation, excitation);
		}
	}

	public double getActivation() {
		return activation;
	}

	public double getImportance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PamNode getReferencedNode() {
		return refNode;
	}

	public boolean isRelevant() {
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

	public void synchronize() {
		// TODO Auto-generated method stub
	}

	public DecayBehavior getDecayBehavior() {
		return db;
	}

	public ExciteBehavior getExciteBehavior() {
		return eb;
	}

	public void setActivation(double d) {
		this.activation = d;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return "Node: " + id;
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
