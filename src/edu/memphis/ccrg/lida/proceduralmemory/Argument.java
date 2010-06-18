package edu.memphis.ccrg.lida.proceduralmemory;
import java.util.List;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * Argument is used to denote a "slot" for real Node. It implements the Node interface, so
 * it can be used as part of a NodeStructure. 
 * It is mostly used for define Context and results for Schemes and SBC.
 * The Argument can be bound to a real Node.
 *  Then, the Argument works as a wrapper for the real Node and can be used 
 * directly in any operation like comparisons.
 * If the real Node is not yet binded, the Argument ignores invocations to methods like ?? 
 * 
 * @author Javier Snaider
 *
 */
public interface Argument extends Node {
	public long getArgumentId();
	public void bindNode(Node n);
	public Node getBindedNode();
	public boolean isBinded();
	public void addContextCondition (NodeStructure condition);
	public void addResultCondition (NodeStructure condition);	
	public List<NodeStructure> getContextConditions();
	public List<NodeStructure> getResultConditions();
	
	/**
	 * @return The id of the linked node or the argumentId * (-1) if no Node has been linked.
	 */
	public long getId();
	
}
