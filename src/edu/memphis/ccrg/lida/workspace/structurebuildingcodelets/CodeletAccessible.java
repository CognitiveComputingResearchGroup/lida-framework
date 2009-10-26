package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * This interface defines how codelets can access data from Workspace sub modules.
 * Modules that need to be accessible to codelets should implement this interface.
 * 
 * @author ryanjmccall
 *
 */
public interface CodeletAccessible {

	/**
	 * @return NodeStructure representation of the buffer contents.
	 */
	public abstract NodeStructure getModuleContent();
	
	/**
	 * @return A collection of NodeStructures comprising the contents of the buffer
	 */
	public abstract Collection<NodeStructure> getModuleContentCollection();
	
	public abstract boolean addNode(Node n);
	
	public abstract boolean addLink(Link l);
	
	public abstract void mergeIn(NodeStructure ns);
	
	public abstract boolean deleteNode(Node n);
	
	public abstract boolean deleteLink(Link l);
}
