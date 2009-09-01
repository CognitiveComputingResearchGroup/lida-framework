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
public class NodeStructureGuiAdapter extends AbstractTypedGraph<Linkable, Link> {

	private NodeStructure nc;

	public NodeStructureGuiAdapter(NodeStructure nc) {
		super(EdgeType.DIRECTED);
		this.nc=nc;
	}

	@Override
	public boolean addEdge(Link arg0, Pair<? extends Linkable> arg1, EdgeType arg2) {
		return false;
	}

	public Linkable getDest(Link arg0) {
		return arg0.getSink();
	}

	public Pair<Linkable> getEndpoints(Link arg0) {
		return new Pair<Linkable>(arg0.getSink(), arg0.getSource());
	}

	public Collection<Link> getInEdges(Linkable arg0) {
		Set<Link> links = nc.getLinks(arg0);
		Set<Link> ret = new HashSet<Link>();
		for (Link l : links) {
			if (l.getSink().equals(arg0)) {
				ret.add(l);
			}
		}
		return ret;
	}

	public Collection<Link> getOutEdges(Linkable arg0) {
		Set<Link> links = nc.getLinks(arg0);
		Set<Link> ret = new HashSet<Link>();
		for (Link l : links) {
			if (l.getSource().equals(arg0)) {
				ret.add(l);
			}
		}
		return ret;
	}

	public Collection<Linkable> getPredecessors(Linkable arg0) {
		Set<Link> links = nc.getLinks(arg0);
		Set<Linkable> ret = new HashSet<Linkable>();
		for (Link l : links) {
			if (l.getSink().equals(arg0)) {
				ret.add(l.getSource());
			}
		}
		return ret;
	}

	public Linkable getSource(Link arg0) {
		return arg0.getSource();
	}

	public Collection<Linkable> getSuccessors(Linkable arg0) {
		Set<Link> links = nc.getLinks(arg0);
		Set<Linkable> ret = new HashSet<Linkable>();
		for (Link l : links) {
			if (l.getSource().equals(arg0)) {
				ret.add(l.getSink());
			}
		}
		return ret;
	}

	public boolean isDest(Linkable arg0, Link arg1) {
		return (arg1.getSink()).equals(arg0);
	}

	public boolean isSource(Linkable arg0, Link arg1) {
		return (arg1.getSource()).equals(arg0);
	}

	public boolean addVertex(Linkable arg0) {
		return false;
	}

	public boolean containsEdge(Link arg0) {
		return nc.hasLink(arg0);
	}

	public boolean containsVertex(Linkable arg0) {
		if (arg0 instanceof Node) {
			return nc.hasNode((Node) arg0);
		} else {
			return nc.hasLink((Link) arg0);
		}
	}

	public int getEdgeCount() {
		return nc.getLinkCount();
	}

	public Collection<Link> getEdges() {
		return nc.getLinks();
	}

	public Collection<Link> getIncidentEdges(Linkable arg0) {
		return nc.getLinks(arg0);
	}

	public Collection<Linkable> getNeighbors(Linkable arg0) {
		Collection<Linkable> res = getPredecessors(arg0);
		res.addAll(getSuccessors(arg0));
		if (res.isEmpty()){
			res=null;
		}
		return res;
	}

	public int getVertexCount() {
		return nc.getLinkableMap().size();
	}

	public Collection<Linkable> getVertices() {
		 return nc.getLinkableMap().keySet();
	}

	public boolean removeEdge(Link arg0) {
		return false;
	}

	public boolean removeVertex(Linkable arg0) {
		return false;
	}
}
