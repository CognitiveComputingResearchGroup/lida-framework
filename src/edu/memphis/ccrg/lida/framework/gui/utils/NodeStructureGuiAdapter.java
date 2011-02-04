/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.gui.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.uci.ics.jung.graph.AbstractTypedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * @author Javier Snaider
 * 
 */
public class NodeStructureGuiAdapter extends
		AbstractTypedGraph<Linkable, GuiLink> {

	private NodeStructure nodeStructure;

	/**
	 * @return the nodeStructure
	 */
	public NodeStructure getNodeStructure() {
		return nodeStructure;
	}

	/**
	 * @param nodeStructure
	 *            the nodeStructure to set
	 */
	public void setNodeStructure(NodeStructure nodeStructure) {
		this.nodeStructure = nodeStructure;
	}

	public NodeStructureGuiAdapter(NodeStructure ns) {
		super(EdgeType.DIRECTED);
		this.nodeStructure = ns;
	}

	@Override
	public boolean addEdge(GuiLink arg0, Pair<? extends Linkable> arg1,
			EdgeType arg2) {
		return false;
	}

	@Override
	public Linkable getDest(GuiLink arg0) {
		if (arg0.getType() == 'S') // If the GuiLink is type Source
			return arg0.getLink();
		else
			return arg0.getLink().getSink();
	}

	@Override
	public Pair<Linkable> getEndpoints(GuiLink arg0) {
		if (arg0.getType() == 'S') // If the GuiLink is type Source
			return new Pair<Linkable>(arg0.getLink().getSource(), arg0
					.getLink());
		else
			return new Pair<Linkable>(arg0.getLink(), arg0.getLink().getSink());
	}

	@Override
	public Collection<GuiLink> getInEdges(Linkable arg0) {
		Set<Link> links = nodeStructure.getConnectedLinks(arg0);
		Set<GuiLink> ret = new HashSet<GuiLink>();
		if (links != null) {
			for (Link l : links) {
				if (l.getSink().equals(arg0)) {
					ret.add(new GuiLink(l, 'D'));
				}
			}
		}
		if (arg0 instanceof Link)
			ret.add(new GuiLink((Link) arg0, 'S'));

		return ret;
	}

	@Override
	public Collection<GuiLink> getOutEdges(Linkable arg0) {
		Set<Link> links = nodeStructure.getConnectedLinks(arg0);
		Set<GuiLink> ret = new HashSet<GuiLink>();
		if (links != null) {
			for (Link l : links) {
				if (l.getSource().equals(arg0)) {
					ret.add(new GuiLink(l, 'S'));
				}
			}
		}
		if (arg0 instanceof Link)
			ret.add(new GuiLink((Link) arg0, 'D'));
		return ret;
	}

	@Override
	public Collection<Linkable> getPredecessors(Linkable arg0) {
		Set<Link> links = nodeStructure.getConnectedLinks(arg0);
		Set<Linkable> ret = new HashSet<Linkable>();
		if (links != null) {
			for (Link l : links) {
				if (l.getSink().equals(arg0)) {
					ret.add(l);
				}
				if (arg0 instanceof Link)
					ret.add(((Link) arg0).getSource());
			}
		}
		return ret;
	}

	@Override
	public Linkable getSource(GuiLink arg0) {
		if (arg0.getType() == 'S') // If the GuiLink is type Source
			return arg0.getLink().getSource();
		else
			return arg0.getLink();
	}

	@Override
	public Collection<Linkable> getSuccessors(Linkable arg0) {
		Set<Link> links = nodeStructure.getConnectedLinks(arg0);
		Set<Linkable> ret = new HashSet<Linkable>();
		if (links != null) {
			for (Link l : links) {
				if (l.getSource().equals(arg0)) {
					ret.add(l);
				}
				if (arg0 instanceof Link)
					ret.add(((Link) arg0).getSink());
			}
		}
		return ret;
	}

	@Override
	public boolean isDest(Linkable arg0, GuiLink arg1) {
		if (arg1.getType() == 'S') {
			return arg1.getLink().equals(arg0);
		} else {
			return arg1.getLink().getSink().equals(arg0);
		}
	}

	@Override
	public boolean isSource(Linkable arg0, GuiLink arg1) {
		if (arg1.getType() == 'D') {
			return arg1.getLink().equals(arg0);
		} else {
			return arg1.getLink().getSource().equals(arg0);
		}
	}

	@Override
	public boolean addVertex(Linkable arg0) {
		return false;
	}

	@Override
	public boolean containsEdge(GuiLink arg0) {
		return getEdges().contains(arg0);
	}

	@Override
	public boolean containsVertex(Linkable arg0) {
		if (arg0 instanceof Node) {
			return nodeStructure.containsNode((Node) arg0);
		} else {
			return nodeStructure.containsLink((Link) arg0);
		}
	}

	@Override
	public int getEdgeCount() {
		return nodeStructure.getLinkCount() * 2;
	}

	@Override
	public Collection<GuiLink> getEdges() {
		Collection<Link> links = nodeStructure.getLinks();
		Set<GuiLink> ret = new HashSet<GuiLink>();
		for (Link l : links) {
			ret.add(new GuiLink(l, 'S'));
			ret.add(new GuiLink(l, 'D'));
		}
		return ret;
	}

	@Override
	public Collection<GuiLink> getIncidentEdges(Linkable arg0) {
		Collection<GuiLink> ret = getInEdges(arg0);
		if (ret == null)
			ret = getOutEdges(arg0);
		else
			ret.addAll(getOutEdges(arg0));

		return ret;

	}

	@Override
	public Collection<Linkable> getNeighbors(Linkable arg0) {
		Collection<Linkable> res = getPredecessors(arg0);
		if (res != null)
			res.addAll(getSuccessors(arg0));
		else
			res = getSuccessors(arg0);

		if (res != null && res.isEmpty()) {
			res = null;
		}
		return res;
	}

	@Override
	public int getVertexCount() {
		return nodeStructure.getLinkableMap().size();
	}

	@Override
	public Collection<Linkable> getVertices() {
		return nodeStructure.getLinkableMap().keySet();
	}

	@Override
	public boolean removeEdge(GuiLink arg0) {
		return false;
	}

	@Override
	public boolean removeVertex(Linkable arg0) {
		return false;
	}
}
