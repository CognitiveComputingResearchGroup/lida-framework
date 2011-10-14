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

	public  NodeStructure getSubNodeStructure(Collection<Node> nodes,
			int distance) {
			return getSubNodeStructure(nodes,distance,0);
	}

	public  NodeStructure getSubNodeStructure(Collection<Node> nodes,
			int distance, double threshold) {
		/*
		 * Collection of specified nodes should not be empty
		 * 
		 */
		
		if (nodes.isEmpty()){
			System.out.printf("\nCollection of specified nodes should not be empty.");
			return null;
		}
		
		
		/*
		 * Desired distance should not be negative
		 */
		if (distance < 0){
			System.out.printf("\nDesired distance should not be negative! Try again.");
			return null;
		}
		
		subNodeStructure = new NodeStructureImpl();

		/*
		 * Distance should be not bigger than number of all links.
		 */
		if (distance > getLinkCount()){
			this.distance = getLinkCount();
		}else{
			this.distance = distance;
		}

		/*
		 *  Add nodes to the sub node structure,
		 *  and scan from each node
		 */
		for (Node n : nodes) {
			depthFirstSearch(n, 0);
		}

		/*
		 *  Add Links to the sub node structure
		 */
		for (Node subNodes : subNodeStructure.getNodes()) {

				/*
				 *  Add links for connected Sinks
				 */
				Map<Linkable, Link> sinks = getConnectedSinks(subNodes);

				Set<Linkable> connectedSinks = sinks.keySet();
				
				for (Linkable linkable : connectedSinks) {
					/*
					 * Not checking complex Links, only consider nodes
					 * linked to the given node
					 */
					if (linkable instanceof Node) {
						if (subNodeStructure.containsLinkable(linkable)) {
							subNodeStructure.addDefaultLink(sinks.get(linkable));
						}
					}
				}
				
				/* 
				 * Add links for connected Sources
				 */
				Map<Node, Link> sources = getConnectedSources(subNodes);

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

		/*
		 *  To add leftover complex links.
		 */
		for (Node subNodes : subNodeStructure.getNodes()) {
			// Add complex link for every node present in subNodeStructure
				Map<Linkable, Link> sinks = getConnectedSinks(subNodes);

				Collection<Link> linksConnectedToLinkableInSNS = sinks.values();
				for (Link link : linksConnectedToLinkableInSNS) {
					if (!link.isSimpleLink())
						subNodeStructure.addDefaultLink(link);
				}
		}
		
		for (Node n:subNodeStructure.getNodes())
			if(n.getActivation()<threshold)
				subNodeStructure.removeNode(n);
		
		return subNodeStructure;

	}
	
	/**
	 * @param currentNode One specified node that be considered as neighbor nodes
	 * or specified nodes in sub NodeStructure 
	 * @param step The distance between specified nodes and this current Node
	 */
	void depthFirstSearch(Node currentNode, int step) {

		Map<Linkable, Link> subSinks;
		Map<Node, Link> subSources;

		if (containsNode(currentNode)){
			subNodeStructure.addDefaultNode(currentNode);
		}else{
			return;
		}

		/*
		 *  Get all connected Sinks
		 */

		subSinks = getConnectedSinks(currentNode);

		Set<Linkable> subLinkables = subSinks.keySet();

		for (Linkable l : subLinkables) {
			if (l instanceof Node) {
				if (step < distance){
					depthFirstSearch((Node) l, step + 1);
				}
			}
		}

		/*
		 *  Get all connected Sources
		 */
		
		subSources = getConnectedSources(currentNode);

		Set<Node> parentNodes = subSources.keySet();

		for (Node n : parentNodes) {
			if (step < distance){
				depthFirstSearch(n, step + 1);
			}
		}

	}

}