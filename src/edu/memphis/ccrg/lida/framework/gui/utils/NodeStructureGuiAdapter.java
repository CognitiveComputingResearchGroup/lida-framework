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

	public Linkable getDest(GuiLink arg0) {
		if (arg0.getType() == 'S') // If the GuiLink is type Source
			return arg0.getLink();
		else
			return arg0.getLink().getSink();
	}

	public Pair<Linkable> getEndpoints(GuiLink arg0) {
		if (arg0.getType() == 'S') // If the GuiLink is type Source
			return new Pair<Linkable>(arg0.getLink().getSource(), arg0
					.getLink());
		else
			return new Pair<Linkable>(arg0.getLink(), arg0.getLink().getSink());
	}

	public Collection<GuiLink> getInEdges(Linkable arg0) {
		Set<Link> links = nodeStructure.getLinks(arg0);
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

	public Collection<GuiLink> getOutEdges(Linkable arg0) {
		Set<Link> links = nodeStructure.getLinks(arg0);
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

	public Collection<Linkable> getPredecessors(Linkable arg0) {
		Set<Link> links = nodeStructure.getLinks(arg0);
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

	public Linkable getSource(GuiLink arg0) {
		if (arg0.getType() == 'S') // If the GuiLink is type Source
			return arg0.getLink().getSource();
		else
			return arg0.getLink();
	}

	public Collection<Linkable> getSuccessors(Linkable arg0) {
		Set<Link> links = nodeStructure.getLinks(arg0);
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

	public boolean isDest(Linkable arg0, GuiLink arg1) {
		if (arg1.getType() == 'S') {
			return arg1.getLink().equals(arg0);
		} else {
			return arg1.getLink().getSink().equals(arg0);
		}
	}

	public boolean isSource(Linkable arg0, GuiLink arg1) {
		if (arg1.getType() == 'D') {
			return arg1.getLink().equals(arg0);
		} else {
			return arg1.getLink().getSource().equals(arg0);
		}
	}

	public boolean addVertex(Linkable arg0) {
		return false;
	}

	public boolean containsEdge(GuiLink arg0) {
		return getEdges().contains(arg0);
	}

	public boolean containsVertex(Linkable arg0) {
		if (arg0 instanceof Node) {
			return nodeStructure.hasNode((Node) arg0);
		} else {
			return nodeStructure.hasLink((Link) arg0);
		}
	}

	public int getEdgeCount() {
		return nodeStructure.getLinkCount() * 2;
	}

	public Collection<GuiLink> getEdges() {
		Collection<Link> links = nodeStructure.getLinks();
		Set<GuiLink> ret = new HashSet<GuiLink>();
		for (Link l : links) {
			ret.add(new GuiLink(l, 'S'));
			ret.add(new GuiLink(l, 'D'));
		}
		return ret;
	}

	public Collection<GuiLink> getIncidentEdges(Linkable arg0) {
		Collection<GuiLink> ret = getInEdges(arg0);
		if (ret == null)
			ret = getOutEdges(arg0);
		else
			ret.addAll(getOutEdges(arg0));

		return ret;

	}

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

	public int getVertexCount() {
		return nodeStructure.getLinkableMap().size();
	}

	public Collection<Linkable> getVertices() {
		return nodeStructure.getLinkableMap().keySet();
	}

	public boolean removeEdge(GuiLink arg0) {
		return false;
	}

	public boolean removeVertex(Linkable arg0) {
		return false;
	}
}
