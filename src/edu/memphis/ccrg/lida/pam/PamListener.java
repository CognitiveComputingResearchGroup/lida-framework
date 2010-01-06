package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * A PamListener receives percepts from Pam asynchronously
 * @author Ryan J. McCall
 * 
 */
public interface PamListener extends ModuleListener{
	/**
	 * This method should return as possible in order to 
	 * no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param ns a NodeStructure
	 */
	public void receiveNodeStructure(NodeStructure ns);
	
	/**
	 * Same as above for a single node.
	 * @param node
	 */
	public void receiveNode(Node node);
	
	/**
	 * Same as above for a single link.
	 * @param l
	 */
	public void receiveLink(Link l);
}