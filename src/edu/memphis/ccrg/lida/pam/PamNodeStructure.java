package edu.memphis.ccrg.lida.pam;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

public interface PamNodeStructure {
	
	//TODO set excite strategies for links too?

	/**
	 * Set the excite behavior for all nodes
	 */
	public void setNodesExciteStrategy(ExciteStrategy behavior);

	/**
	 * Set the decay behavior for all nodes.
	 * @param strategy How nodes will be decayed
	 */
	public void setNodesDecayStrategy(DecayStrategy strategy);

	/**
	 * When you excite the parents of a node you might want to excite the connecting links too.
	 * Thus this method find the parents and all the links between supplied node and them
	 * @param n supplied node
	 * @return map of parents and links connecting node to them
	 */
	public Map<PamNode, PamLink> getParentsAndConnectingLinksOf(Node n);

	/**
	 * Decay the {@link Linkable}s of this {@link PamNodeStructure}
	 */
	public void decayLinkables(long ticks);

}