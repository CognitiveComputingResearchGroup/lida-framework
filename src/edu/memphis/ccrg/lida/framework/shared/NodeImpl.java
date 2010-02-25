package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * Default Node Implementation
 * 
 * @author Javier Snaider
 * 
 */
public class NodeImpl extends ActivatibleImpl implements Node {

	private long id;
	private String label = "";
	private double importance;
	protected PamNode refNode;
	private NodeClass nodeClass = NodeClass.none;
	
	public NodeImpl(){
		super();
	}

	public NodeImpl(NodeImpl n) {
		super(n.getActivation(), n.getExciteStrategy(), n.getDecayStrategy());
		this.id = n.id;
		this.refNode = n.refNode;
	}
	
	public void init(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label=label;
	}

	public double getImportance() {
		return importance;
	}
	public void setImportance(double importance) {
		this.importance = importance;
	}

	public PamNode getReferencedNode() {
		return refNode;
	}
	public void setReferencedNode(PamNode n) {
		refNode = n;
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
	public String toString(){
		return getLabel() + " node ["+getId()+"] ";
	}

	public NodeClass getNodeClass() {
		return nodeClass ;
	}

	public void setNodeClass(NodeClass n) {
		nodeClass = n;
	}
}//class
