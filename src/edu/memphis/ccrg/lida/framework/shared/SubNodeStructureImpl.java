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
	 * @param distance The farthest distance between specified nodes and 
	 * its neighbor nodes
	 * @return A sub NodeStructure which involves specified and all 
	 * satisfied neighbor nodes
	 */
	public  NodeStructure getSubNodeStructure(Collection<Node> nodes,
			int distance) {
			return getSubNodeStructure(nodes,distance,0.0);
	}

	/**
	 * @param nodes Set of specified nodes in NodeStructure
	 * @param distance The farthest distance between specified nodes and
	 * its neighbor nodes
	 * @param threshold responds to lower bound of activation
	 * @return A sub NodeStructure which involves specified and all 
	 * satisfied neighbor nodes
	 */
	public  NodeStructure getSubNodeStructure(Collection<Node> nodes,
			int distance, double threshold) {
		
		if (nodes == null || nodes.isEmpty()){
			logger.log(Level.WARNING, "Collection of specified nodes should not be empty.",
					TaskManager.getCurrentTick());
			return null;
		}
		if (distance < 0){
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
		if (distance > getLinkCount()){
			distance = getLinkCount();
		}

		NodeStructure subNodeStructure = new NodeStructureImpl();
		for (Node n : nodes) {
			//Add nodes to the sub node structure and scan from each node
			if (threshold == 0){
				depthFirstSearchWithoutThreshold(n, distance, subNodeStructure);
			} else {
				depthFirstSearchWithThreshold(n, distance, subNodeStructure, threshold);
			}
		}
		//	Add Links to the sub node structure
		for (Node subNodes : subNodeStructure.getNodes()) {
				
				/* 
				 * Add links for connected Sources
				 */
				Map<Node, Link> sources = getConnectedSources(subNodes);

				/*
				 * All the Sinks are nodes here
				 */
				Set<Node> connectedSources = sources.keySet(); 
				
				for (Node n : connectedSources) {
					if (subNodeStructure.containsNode(n)) {
						subNodeStructure.addDefaultLink(sources.get(n));
					}
				}
		}

		//To add leftover complex links.
		for (Node subNodes : subNodeStructure.getNodes()) {
			// Add complex link for every node present in subNodeStructure
				Map<Linkable, Link> sinks = getConnectedSinks(subNodes);

				Collection<Link> linksConnectedToLinkableInSNS = sinks.values();
				for (Link link : linksConnectedToLinkableInSNS) {
					if (!link.isSimpleLink())
						subNodeStructure.addDefaultLink(link);
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
	 * It involves specified nodes and all neighbor nodes whose distance
	 * from one of specified nodes is not bigger than farthest distance 
	 * coming from arguments. Also it involves all links between these 
	 * above nodes.
	 */
	private void depthFirstSearchWithoutThreshold(Node currentNode, int distanceLeftToGo, NodeStructure subNodeStructure) {
		if (containsNode(currentNode)){
			subNodeStructure.addDefaultNode(currentNode);
		}else{
			return;
		}

		/*
		 *  Get all connected Sinks
		 */
		Map<Linkable, Link> subSinks = getConnectedSinks(currentNode);
		Set<Linkable> subLinkables = subSinks.keySet();
		for (Linkable l : subLinkables) {
			if (l instanceof Node) {
				if (0 < distanceLeftToGo){
					depthFirstSearchWithoutThreshold((Node) l, distanceLeftToGo - 1, subNodeStructure);
				}
			}
		}

		/*
		 *  Get all connected Sources
		 */
		Map<Node, Link> subSources = getConnectedSources(currentNode);
		Set<Node> parentNodes = subSources.keySet();
		for (Node n : parentNodes) {
			if (0 < distanceLeftToGo){
				depthFirstSearchWithoutThreshold(n, distanceLeftToGo - 1, subNodeStructure);
			}
		}
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
	private void depthFirstSearchWithThreshold(Node currentNode, int distanceLeftToGo, 
			NodeStructure subNodeStructure, double threshold) {
		if ((containsNode(currentNode)) &&(currentNode.getActivation() >= threshold)){
			subNodeStructure.addDefaultNode(currentNode);
		}else{
			return;
		}

		/*
		 *  Get all connected Sinks
		 */
		Map<Linkable, Link> subSinks = getConnectedSinks(currentNode);
		Set<Linkable> subLinkables = subSinks.keySet();
		for (Linkable l : subLinkables) {
			if (l instanceof Node) {
				if (0 < distanceLeftToGo){
					depthFirstSearchWithThreshold((Node) l, distanceLeftToGo - 1, subNodeStructure, threshold);
				}
			}
		}

		/*
		 *  Get all connected Sources
		 */
		Map<Node, Link> subSources = getConnectedSources(currentNode);
		Set<Node> parentNodes = subSources.keySet();
		for (Node n : parentNodes) {
			if (0 < distanceLeftToGo){
				depthFirstSearchWithThreshold(n, distanceLeftToGo - 1, subNodeStructure, threshold);
			}
		}
	}

}