package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.ArrayList;
import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Default {@link AttentionCodelet}, which seeks to create a {@link Coalition}
 * from the most activate content above a threshold.
 * 
 * @author Ryan J. McCall
 */
public class DefaultAttentionCodelet extends AttentionCodeletImpl {

//	private static final Logger logger = Logger.getLogger(DefaultAttentionCodelet.class.getCanonicalName());
	private static final double DEFAULT_ATTENTION_THRESHOLD = 0.0;
	/**
	 * Activation which content must have in order to be added to the
	 * {@link Coalition}
	 */
	protected double attentionThreshold = DEFAULT_ATTENTION_THRESHOLD;

	private static final int DEFAULT_RETRIEVAL_DEPTH = 0;
	/**
	 * Depth of content, beyond the sought content, the attention codelet will
	 * add to a {@link Coalition}. Currently only supported for one level beyond
	 * sought content.
	 */
	protected int retrievalDepth = DEFAULT_RETRIEVAL_DEPTH;
	
	/*
	 * Current most active node in the Workspace forming the root of the tree
	 * from which the coalition will be formed. 
	 */
	private Collection<Node> activeNodes = new ArrayList<Node>();

	/**
	 * If this method is overridden, this init() must be called first! i.e. use
	 * super.init(); Will set parameters with the following names:<br/>
	 * <br/>
	 * 
	 * <b>attentionThreshold</b> threshold content must have to be added to a
	 * {@link Coalition}<br/>
	 * <b>retrievalDepth</b> depth of neighboring nodes retrieved from most
	 * active content<br/>
	 * <br/>
	 * If any parameter is not specified its default value will be used.
	 * 
	 * @see AttentionCodeletImpl#init()
	 */
	@Override
	public void init() {
		super.init();
		attentionThreshold = (Double) getParam("attentionThreshold",DEFAULT_ATTENTION_THRESHOLD);
		retrievalDepth = (Integer) getParam("retrievalDepth",DEFAULT_RETRIEVAL_DEPTH);
	}

	/**
	 * Returns true if specified buffer contains at least one node above
	 * {@link #attentionThreshold}. Sets the most activated node as the
	 * codelet's new sought content
	 */
	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		double maxActivation = -1.0;
		Node maxActiveNode = null;
		NodeStructure ns = (NodeStructure) buffer.getBufferContent(null);
		for (Node n: ns.getNodes()) {
			double activation = n.getActivation();
			if (activation >= attentionThreshold && 
				activation > maxActivation) {
				maxActivation = activation;
				maxActiveNode = n;
			}
		}
		if(maxActiveNode != null){
			activeNodes.clear();
			activeNodes.add(maxActiveNode);
			return true;
		}
		return false;
	}

	/**
	 * Returns a the most active {@link WorkspaceContent} and possibly
	 * neighboring content as specified by {@link #retrievalDepth}s
	 */
	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		NodeStructure bufferStructure = buffer.getBufferContent(null);
		//TODO was throwing ConcurrentModificationException
		return bufferStructure.getSubgraph(activeNodes, retrievalDepth, attentionThreshold);
	}
//	for(Node n: activeNodes){
//	Node bufferNode = bufferNS.getNode(n.getId());
//	result.addNode(bufferNode,bufferNode.getFactoryType());
//	if (retrievalDepth > DEFAULT_RETRIEVAL_DEPTH) {
//		getNeighbors(bufferNS, result, n);
//	}
//}
	
//	private void getNeighbors(NodeStructure bufferNS,
//			NodeStructure retrievedSubGraph, Node n) {
//		Map<Linkable, Link> sinks = bufferNS.getConnectedSinks(n);
//		for (Linkable sink : sinks.keySet()) {
//			if (sink instanceof Node && 
//					sink.getActivation() >= attentionThreshold) {
//				Node sinkNode = (Node)sink;
//				retrievedSubGraph.addNode(sinkNode,sinkNode.getFactoryType());
//				Link connectingLink = sinks.get(sink);
//				retrievedSubGraph.addLink(connectingLink,connectingLink.getFactoryType());
//			}
//		}
//		Map<Node, Link> sources = bufferNS.getConnectedSources(n);
//		for (Node source : sources.keySet()) {
//			if (source.getActivation() >= attentionThreshold) {
//				retrievedSubGraph.addNode(source,source.getFactoryType());
//				Link connectingLink = sources.get(source);
//				retrievedSubGraph.addLink(connectingLink,connectingLink.getFactoryType());
//			}
//		}
//	}
}