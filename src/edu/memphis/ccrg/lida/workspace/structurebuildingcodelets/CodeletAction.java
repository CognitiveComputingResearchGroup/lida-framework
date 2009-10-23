package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * An encapsulation of the action of a codelet.
 *
 * @author ryanjmccall
 *
 */
public interface CodeletAction {

	/**
	 * An action from specified ordered buffer to a destination buffer 
	 * @param buffer
	 * @param destination
	 */	
	public void performAction(Collection<NodeStructure> buffer, CodeletWritable destination);
	/**
	 * An action from a standard buffer to a destination buffer 
	 * @param buffer
	 * @param destination
	 */
	public void performAction(NodeStructure buffer, CodeletWritable destination);
	
}
  