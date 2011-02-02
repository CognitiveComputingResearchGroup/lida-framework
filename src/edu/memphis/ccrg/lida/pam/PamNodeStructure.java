package edu.memphis.ccrg.lida.pam;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * NodeStructure for {@link PamNode} and {@link PamLink}.  Part of {@link PerceptualAssociativeMemory}
 */
public interface PamNodeStructure extends NodeStructure {
	
	/**
	 * When you excite the parents of a node you might want to excite the connecting links too.
	 * Thus this method Finds and returns the parents of specified Node with all of 
	 * the links between them.
	 * @param n supplied node
	 * @return map of parents and links connecting node to them
	 */
	public Map<PamNode, PamLink> getParentsWithLinks(PamNode n);

}