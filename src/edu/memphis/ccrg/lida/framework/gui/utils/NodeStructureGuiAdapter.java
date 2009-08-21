/**
 * 
 */
package edu.memphis.ccrg.lida.framework.gui.utils;

import java.util.Collection;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.uci.ics.jung.graph.AbstractTypedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * @author Javier
 *
 */
public class NodeStructureGuiAdapter  extends AbstractTypedGraph{
	public NodeStructureGuiAdapter(EdgeType edge_type) {
		super(edge_type);
		// TODO Auto-generated constructor stub
	}

	private NodeStructure ns;
	@Override
	public boolean addEdge(Object arg0, Pair arg1, EdgeType arg2) {
		return false;
	}

	public Object getDest(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Pair getEndpoints(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getInEdges(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getOutEdges(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getPredecessors(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getSource(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getSuccessors(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDest(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSource(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addVertex(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsEdge(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsVertex(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getEdgeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Collection getEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getIncidentEdges(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getNeighbors(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getVertexCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Collection getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeEdge(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeVertex(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
