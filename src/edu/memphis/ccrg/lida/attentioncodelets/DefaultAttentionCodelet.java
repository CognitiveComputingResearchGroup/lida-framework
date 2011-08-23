package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Default {@link AttentionCodelet} which seeks content above a threshold
 * to create a coalition from.
 * @author Ryan J. McCall
 */
public class DefaultAttentionCodelet extends AttentionCodeletImpl {

	private static final Logger logger = Logger
			.getLogger(DefaultAttentionCodelet.class.getCanonicalName());

	private static final double DEFAULT_ATTENTION_THRESHOLD = 0.5;
	/**
	 * Threshold which content must have in order to be added to the coalition
	 */
	protected double attentionThreshold = DEFAULT_ATTENTION_THRESHOLD;
	
	private static final int DEFAULT_RETRIEVAL_DEPTH = 0;
	/**
	 * Depth of content beyond the sought content the attention codelet will
	 * return.  Currently only supported for one level beyond sought content
	 */
	protected int retrievalDepth = DEFAULT_RETRIEVAL_DEPTH;

	@Override
	public void init() {
		super.init();
		attentionThreshold = (Double) getParam("attentionThreshold",
				DEFAULT_ATTENTION_THRESHOLD);
		retrievalDepth = (Integer) getParam("retrievalDepth", DEFAULT_RETRIEVAL_DEPTH);
	}

	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		soughtContent.clearNodeStructure();

		Node winner = null;
		double winnerActivation = -1.0;
		NodeStructure ns = (NodeStructure) buffer.getBufferContent(null);
		for (Node n : ns.getNodes()) {
			double activation = n.getActivation();
			if (activation >= attentionThreshold
					&& activation > winnerActivation) {
				winner = n;
				winnerActivation = activation;
			}
		}

		if (winner != null) {
			soughtContent.addDefaultNode(winner);
			return true;
		}

		return false;
	}

	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		NodeStructure bufferNS = buffer.getBufferContent(null);
		NodeStructure retrievedSubGraph = new NodeStructureImpl();
		if (bufferNS != null) {
			// TODO add getNeighborhood(originNode, maxDistanceFromOrigin, requiredActivation) method in NodeStructure?
			//or method here.
			for (Node n : soughtContent.getNodes()) {// typically a small number
				if (bufferNS.containsNode(n)) {
					retrievedSubGraph.addDefaultNode(bufferNS.getNode(n.getId()));
					if(retrievalDepth > DEFAULT_RETRIEVAL_DEPTH){
						getNeighbors(bufferNS, retrievedSubGraph, n);
					}
				}
			}
		} else {
			logger.log(Level.WARNING, "Buffer returned null NodeStructure",
					TaskManager.getCurrentTick());
		}
		return retrievedSubGraph;
	}
	
	private void getNeighbors(NodeStructure bufferNS, NodeStructure retrievedSubGraph, Node n) {
		Map<Linkable, Link> sinks = bufferNS.getConnectedSinks(n);
		for (Linkable sink : sinks.keySet()) {
			if (sink instanceof Node && sink.getActivation() >= attentionThreshold){
				retrievedSubGraph.addDefaultNode((Node) sink);
				Link connectingLink = sinks.get(sink);
				retrievedSubGraph.addDefaultLink(connectingLink);
			}
		}

		Map<Node, Link> sources = bufferNS.getConnectedSources(n);
		for (Node source : sources.keySet()) {
			if(source.getActivation() >= attentionThreshold){
				retrievedSubGraph.addDefaultNode(source);
				Link connectingLink = sources.get(source);
				retrievedSubGraph.addDefaultLink(connectingLink);
			}
		}
	}

}
