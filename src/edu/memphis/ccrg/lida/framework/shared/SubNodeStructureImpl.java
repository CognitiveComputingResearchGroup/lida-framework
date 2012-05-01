/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Finds and returns a sub NodeStructure that contains all Nodes
 * connected to specified Nodes after given distance.
 * 
 *  @author Daqi Dong
 *  @author Pulin Agrawal
 */
public class SubNodeStructureImpl extends NodeStructureImpl {
//	TODO: Move whole contents of this class to NodeStructureImpl after
//	 * review them.
	private static final Logger logger = Logger
			.getLogger(NodeStructureImpl.class.getCanonicalName());

	/**
	 * @param nodes Set of specified nodes in NodeStructure
	 * @param d The farthest distance between specified nodes and 
	 * its neighbor nodes
	 * @return A sub NodeStructure which involves specified and all 
	 * satisfied neighbor nodes
	 */
	public  NodeStructure getSubgraph(Collection<Node> nodes,int d) {
		return getSubgraph(nodes,d,0.0);
	}

	/**
	 * @param nodes Set of specified nodes in NodeStructure
	 * @param d The farthest distance between specified nodes and
	 * its neighbor nodes
	 * @param threshold responds to lower bound of activation
	 * @return A sub NodeStructure which involves specified and all 
	 * satisfied neighbor nodes
	 */
	public  NodeStructure getSubgraph(Collection<Node> nodes,
			int d, double threshold) {
		if (nodes == null ){
			logger.log(Level.WARNING, "Collection of specified nodes are not available.",
					TaskManager.getCurrentTick());
			return null;
		}
		if (nodes.isEmpty()){
			logger.log(Level.WARNING, "Collection of specified nodes should not be empty.",
					TaskManager.getCurrentTick());
			return null;
		}
		if (d < 0){
			logger.log(Level.WARNING, "Desired distance should not be negative.",
					TaskManager.getCurrentTick());
			return null;
		}
		
		if (threshold < 0){
			logger.log(Level.WARNING, "Desired threshold should not be negative.",
					TaskManager.getCurrentTick());
			return null;
		}
		//	Distance should be not bigger than number of all links.
		if (d > getLinkCount()){
			d = getLinkCount();
		}
		
		//Preserve default Node and Link type of the originating NodeStructure
		String linkType = getDefaultLinkType();
		String nodeType = getDefaultNodeType();
		NodeStructure subNodeStructure = new NodeStructureImpl(nodeType, linkType);
		
		for (Node n : nodes) {
			//Add nodes to the sub node structure and scan from each node
			depthFirstSearch(n, d, subNodeStructure, threshold);
		}
		//	Add all simple links to the sub node structure
		for (Node subNode : subNodeStructure.getNodes()) {
			//Get the simple links for each Node already in the subgraph
			Map<Node, Link> sources = getConnectedSources(subNode);
			for (Node n : sources.keySet()) {
				//Add the simple link only if its source is present in the subgraph
				if (subNodeStructure.containsNode(n)) {
					subNodeStructure.addLink(sources.get(n), sources.get(n).getFactoryType());
				}
			}
		}
		//Add all complex links.
		for (Node subNode : subNodeStructure.getNodes()) {
			// Get the potential complex links for every node present in the subgraph
			Map<Linkable, Link> sinks = getConnectedSinks(subNode);
			for (Linkable l : sinks.keySet()) {
				//If Linkable is a link and the sub graph contains it then there is a complex link to add. 
				if ((l instanceof Link) && subNodeStructure.containsLinkable(l)){
					subNodeStructure.addLink(sinks.get(l), sinks.get(l).getFactoryType());
				}
			}
		}
		return subNodeStructure;
	}
		
	/*
	 * @param currentNode One specified node that be considered as neighbor nodes
	 * or specified nodes in sub NodeStructure 
	 * @param step The distance between specified nodes and this current Node
	 * @param distanceLeftToGo The farthest distance between specified nodes and
	 * its neighbor nodes
	 * @param subNodeStructure Nodes contained in subNodeStructure. 
	 * @param threshold Lower bound of Node's activation
	 * It involves specified nodes and all neighbor nodes whose distance
	 * from one of specified nodes is not bigger than farthest distance 
	 * coming from arguments, and those nodes' activation is not lower than threshold.
	 * Also it involves all links between these above nodes.
	 */
	private void depthFirstSearch(Node currentNode, int distanceLeftToGo, 
			NodeStructure subNodeStructure, double threshold) {
		Node actual = getNode(currentNode.getId());
		if (actual != null && (actual.getActivation() >= threshold)){
			subNodeStructure.addNode(actual, actual.getFactoryType());
			
			//Get all connected Sinks
			Map<Linkable, Link> subSinks = getConnectedSinks(actual);
			Set<Linkable> subLinkables = subSinks.keySet();
			for (Linkable l : subLinkables) {
				if (l instanceof Node && 0 < distanceLeftToGo){
					depthFirstSearch((Node)l, distanceLeftToGo - 1, subNodeStructure, threshold);
				}
			}
			//Get all connected Sources
			Map<Node, Link> subSources = getConnectedSources(actual);
			Set<Node> parentNodes = subSources.keySet();
			for (Node n : parentNodes) {
				if (0 < distanceLeftToGo){
					depthFirstSearch(n, distanceLeftToGo - 1, subNodeStructure, threshold);
				}
			}
		}

	}

}