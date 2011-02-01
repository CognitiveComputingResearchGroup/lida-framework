package edu.memphis.ccrg.lida.pam;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * NodeStructure for {@link PamNode} and {@link PamLink}.  Part of {@link PerceptualAssociativeMemory}
 */
public interface PamNodeStructure extends NodeStructure {
	
	//TODO is there a better way to manage the strategies of the PamNodeStructure?
	//If yes then create 2 for links and move into NodeStructure
	/**
	 * Set the excite behavior for all nodes.
	 * 
	 * @param behavior
	 *            the new nodes excite strategy
	 */
	public void setNodesExciteStrategy(ExciteStrategy behavior);

	/**
	 * Set the decay behavior for all nodes.
	 * @param strategy How nodes will be decayed
	 */
	public void setNodesDecayStrategy(DecayStrategy strategy);

	/**
	 * When you excite the parents of a node you might want to excite the connecting links too.
	 * Thus this method Finds and returns the parents of specified Node with all of 
	 * the links between them.
	 * @param n supplied node
	 * @return map of parents and links connecting node to them
	 */
	public Map<PamNode, PamLink> getParentsWithLinks(PamNode n);

	/**
	 * TODO abstract to NodeStructure?
	 * then workspace buffer doesn't need to do it.
	 * both of these classes have nodeRemovalThreshold so this would simplify
	 * Decay the {@link Linkable}s of this {@link PamNodeStructure}.
	 * 
	 * @param ticks
	 *            the ticks
	 */
	public void decayNodeStructure(long ticks);

}