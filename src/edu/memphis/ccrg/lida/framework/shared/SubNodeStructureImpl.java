                                                                     
                                                                     
                                             
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
 *  @author Daqi, Pulin
 */
public class SubNodeStructureImpl extends NodeStructureImpl {

	private int distance;
	
	/*
	 * Nodes contained in subNodeStructure
	 */
	private NodeStructureImpl subNodeStructure;

	/**
	 * @param nodes
	 * @param distance
	 * @return
	 */
	/*TODO: Consider the threshold of nodes' activation in next version*/
	public NodeStructure getSubNodeStructure(Collection<Node> nodes,
			int distance) {
		subNodeStructure = new NodeStructureImpl();

		this.distance = distance;

		// Add nodes to the sub node structure
		for (Node n : nodes) {// scan each node
			deepFirstSearch(n, 0);
		}

		// Add Links to the sub node structure

		for (Linkable l : subNodeStructure.getLinkables()) {
			// Add links for connected Sinks
			if (l.getExtendedId().isNodeId()) {
				Map<Linkable, Link> sinks = getConnectedSinks((Node) l);

				Set<Linkable> connectedSinks = sinks.keySet(); // All the Sinks
																// are nodes
																// here
				for (Linkable l2 : connectedSinks) {
					if (l2.getExtendedId().isNodeId()) {
						if (subNodeStructure.containsLinkable(l2)) {
							// Not checking complex Links, only consider nodes
							// linked to the given node
							subNodeStructure.addDefaultLink(sinks.get(l2));
						}
					}
				}
				// Add links for connected Sources

				Map<Node, Link> sources = getConnectedSources(l);

				Set<Node> connectedSources = sources.keySet(); // All the
																// Sources are
																// nodes here
				for (Node n : connectedSources) {
					if (subNodeStructure.containsLinkable(n)) {
						// Not checking complex Links, only consider nodes
						// linked to the given node
						subNodeStructure.addDefaultLink(sources.get(n));
					}
				}
			}

		}

		// To add leftover complex links.
		for (Linkable l : subNodeStructure.getLinkables()) {
			// Add complex link for every node present in subNodeStructure
			if (l.getExtendedId().isNodeId()) {
				Map<Linkable, Link> sinks = getConnectedSinks((Node) l);

				Collection<Link> linksConnectedToLinkableInSNS = sinks.values();
				for (Link link : linksConnectedToLinkableInSNS) {
					if (!link.isSimpleLink())
						subNodeStructure.addDefaultLink(link);
				}
			}
		}
		return subNodeStructure;

	}

	void deepFirstSearch(Node currentNode, int step) {

		Map<Linkable, Link> subSinks;
		Map<Node, Link> subSources;

		if (step > distance)
			return;    // Lot of unnecessary execution is taking place because of the position of this part of code

		subNodeStructure.addDefaultNode(currentNode);

		// Get all connected Sinks

		subSinks = getConnectedSinks(currentNode);

		Set<Linkable> subLinkables = subSinks.keySet();

		for (Linkable l : subLinkables) {
			if (l.getExtendedId().isNodeId()) {
				deepFirstSearch((Node) l, step + 1);
			}
		}

		// Get all connected Sources

		subSources = getConnectedSources(currentNode);

		Set<Node> parentNodes = subSources.keySet();

		for (Node n : parentNodes) {
			deepFirstSearch(n, step + 1);
		}

	}

}