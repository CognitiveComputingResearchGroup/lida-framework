/**
 * 
 */
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeClass;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * @author Javier Snaider
 * 
 */
public class ArgumentImpl implements Argument {

	private long argumentId;
	private Node node;
	private List<NodeStructure> contextConditions = new ArrayList<NodeStructure>();
	private List<NodeStructure> resultConditions = new ArrayList<NodeStructure>();

	public ArgumentImpl(long argumentId) {
		if (argumentId <= 0) {
			throw new IllegalArgumentException(
					"The argument argumentId must be positive");
		}
	}

	public long getArgumentId() {
		return argumentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.proceduralmemory.Argument#bindNode(edu.memphis.
	 * ccrg.lida.framework.shared.Node)
	 */
	@Override
	public void bindNode(Node node) {
		this.node = node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Argument#getBindedNode()
	 */
	@Override
	public Node getBindedNode() {
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.proceduralmemory.Argument#getContextConditions()
	 */
	@Override
	public List<NodeStructure> getContextConditions() {
		return Collections.unmodifiableList(contextConditions);
	}

	/**
	 * Returns the id of the binded Node. If the binded Node is null, then 
	 * the argumentId * (-1) is returned.
	 * Note: This can cause troubles with the location of the node in the NodeStructure where 
	 * the Argument is included. TODO: Check!
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Argument#getId()
	 */
	@Override
	public long getId() {
		if (node != null) {
			return node.getId();
		}
		return -argumentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.proceduralmemory.Argument#getResultConditions()
	 */
	@Override
	public List<NodeStructure> getResultConditions() {
		return Collections.unmodifiableList(resultConditions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Argument#isBinded()
	 */
	@Override
	public boolean isBinded() {
		return (node != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#getImportance()
	 */
	@Override
	public double getImportance() {
		if (node != null) {
			return node.getImportance();
		}
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#getNodeClass()
	 */
	@Override
	public NodeClass getNodeClass() {
		if (node != null) {
			return node.getNodeClass();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#getReferencedNode()
	 */
	@Override
	public PamNode getReferencedNode() {
		if (node != null) {
			return node.getReferencedNode();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#setId(long)
	 */
	@Override
	public void setId(long id) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#setImportance(double)
	 */
	@Override
	public void setImportance(double importance) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Node#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Node#setNodeClass(edu.memphis.
	 * ccrg.lida.framework.shared.NodeClass)
	 */
	@Override
	public void setNodeClass(NodeClass n) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Node#setReferencedNode(edu.memphis
	 * .ccrg.lida.pam.PamNode)
	 */
	@Override
	public void setReferencedNode(PamNode n) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Linkable#getIds()
	 */
	@Override
	public String getIds() {
		return "" + getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Linkable#getLabel()
	 */
	@Override
	public String getLabel() {
		if (node != null) {
			return node.getLabel();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Linkable#init(java.util.Map)
	 */
	@Override
	public void init(Map<String, Object> params) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Activatible#decay(long)
	 */
	@Override
	public void decay(long ticks) {
		if (node != null) {
			node.decay(ticks);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Activatible#excite(double)
	 */
	@Override
	public void excite(double amount) {
		if (node != null) {
			node.excite(amount);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Activatible#getActivation()
	 */
	@Override
	public double getActivation() {
		if (node != null) {
			return node.getActivation();
		}
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#getDecayStrategy()
	 */
	@Override
	public DecayStrategy getDecayStrategy() {
		if (node != null) {
			return node.getDecayStrategy();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#getExciteStrategy()
	 */
	@Override
	public ExciteStrategy getExciteStrategy() {
		if (node != null) {
			return node.getExciteStrategy();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#getTotalActivation()
	 */
	@Override
	public double getTotalActivation() {
		if (node != null) {
			return node.getTotalActivation();
		}
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#setActivation(double)
	 */
	@Override
	public void setActivation(double activation) {
		if (node != null) {
			node.setActivation(activation);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#setDecayStrategy(edu
	 * .memphis.ccrg.lida.framework.strategies.DecayStrategy)
	 */
	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#setExciteStrategy(
	 * edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)
	 */
	@Override
	public void setExciteStrategy(ExciteStrategy strategy) {
	}

	@Override
	public void addContextCondition(NodeStructure condition) {
		contextConditions.add(condition);
	}

	@Override
	public void addResultCondition(NodeStructure condition) {
		resultConditions.add(condition);
	}

	/** 
	 * If the Object o is an instance of Argument, 
	 * the argumentId is used to check the equals condition.
	 * If the Object o is an instance of Node (but not the Argument), then the 
	 * id of the binded Node is used to check the equals condition.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof Argument) {
				Argument a = (Argument) o;
				if (a.getArgumentId() == this.argumentId) {
					return true;
				}
			} else if (o instanceof Node && node != null) {
				Node n=(Node)o;
				if (n.getId()==node.getId()){
					return true;
				}
			}
		}
		return false;
	}

}
