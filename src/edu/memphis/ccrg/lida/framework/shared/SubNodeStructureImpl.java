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

/**
 * Finds and returns a sub NodeStructure that contains all Nodes
 * connected to specified Nodes after given distance.
 * 
 * TODO: Move whole contents of this class to NodeStructureImpl after
 * review them.
 * 
 *  @author Daqi, Pulin
 */

public class SubNodeStructureImpl extends NodeStructureImpl {

	/*
	 * The farthest distance between specified nodes and their 
	 * neighbor nodes. 
	 * This distance and specified nodes will be passed through arguments.
	 */
	private int distance;
	
	/*
	 * Nodes contained in subNodeStructure. 
	 * It involves specified nodes and all neighbor nodes whose distance
	 * from one of specified nodes is not bigger than farthest distance 
	 * coming from arguments. Also it involves all links between these 
	 * above nodes.
	 */
	private NodeStructureImpl subNodeStructure;

	/**
	 * @param nodes Set of specified nodes in NodeStructure
	 * @param distance The farthest distance between specified nodes and 
	 * its neighbor nodes
	 * @return A sub NodeStructure which involves specified and all 
	 * satisfied neighbor nodes
	 */
	/*
	 * TODO: Consider the threshold of nodes' activation in next version
	 */
	public NodeStructure getSubNodeStructure(Collection<Node> nodes,
			int distance) {
		subNodeStructure = new NodeStructureImpl();

		this.distance = distance;

		/*
		 *  Add nodes to the sub node structure
		 *  Scan each node
		 */
		for (Node n : nodes) {
			deepFirstSearch(n, 0);
		}

		/*
		 *  Add Links to the sub node structure
		 */
		for (Linkable subLinkable : subNodeStructure.getLinkables()) {
			/*
			 *  Add links for connected Sinks
			 */
			if (subLinkable.getExtendedId().isNodeId()) {
				Map<Linkable, Link> sinks = getConnectedSinks((Node) subLinkable);

				Set<Linkable> connectedSinks = sinks.keySet();
				
				for (Linkable linkable : connectedSinks) {
					if (linkable.getExtendedId().isNodeId()) {
						if (subNodeStructure.containsLinkable(linkable)) {
							/*
							 * Not checking complex Links, only consider nodes
							 * linked to the given node
							 */
							subNodeStructure.addDefaultLink(sinks.get(linkable));
						}
					}
				}
				/* 
				 * Add links for connected Sources
				 */

				Map<Node, Link> sources = getConnectedSources(subLinkable);

				/*
				 * All the Sinks are nodes here
				 */
				Set<Node> connectedSources = sources.keySet(); 
				
				for (Node n : connectedSources) {
					if (subNodeStructure.containsLinkable(n)) {
						subNodeStructure.addDefaultLink(sources.get(n));
					}
				}
			}

		}

		/*
		 *  To add leftover complex links.
		 */
		for (Linkable linkable : subNodeStructure.getLinkables()) {
			// Add complex link for every node present in subNodeStructure
			if (linkable.getExtendedId().isNodeId()) {
				Map<Linkable, Link> sinks = getConnectedSinks((Node) linkable);

				Collection<Link> linksConnectedToLinkableInSNS = sinks.values();
				for (Link link : linksConnectedToLinkableInSNS) {
					if (!link.isSimpleLink())
						subNodeStructure.addDefaultLink(link);
				}
			}
		}
		return subNodeStructure;

	}

	/**
	 * @param currentNode One specified node that be considered as neighbor nodes
	 * or specified nodes in sub NodeStructure 
	 * @param step The distance between specified nodes and this current Node
	 */
	void deepFirstSearch(Node currentNode, int step) {

		Map<Linkable, Link> subSinks;
		Map<Node, Link> subSources;

		if (step > distance)
			/*
			 * TODO: Lot of unnecessary execution is taking place because of the position of this part of code
			 */
			return;

		subNodeStructure.addDefaultNode(currentNode);

		/*
		 *  Get all connected Sinks
		 */

		subSinks = getConnectedSinks(currentNode);

		Set<Linkable> subLinkables = subSinks.keySet();

		for (Linkable l : subLinkables) {
			if (l.getExtendedId().isNodeId()) {
				deepFirstSearch((Node) l, step + 1);
			}
		}

		/*
		 *  Get all connected Sources
		 */

		subSources = getConnectedSources(currentNode);

		Set<Node> parentNodes = subSources.keySet();

		for (Node n : parentNodes) {
			deepFirstSearch(n, step + 1);
		}

	}

}