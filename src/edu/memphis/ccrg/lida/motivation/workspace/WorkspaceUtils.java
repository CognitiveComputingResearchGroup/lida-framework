package edu.memphis.ccrg.lida.motivation.workspace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.motivation.shared.FeelingNodeImpl;

public class WorkspaceUtils {

	/**
	 * Returns the earliest (based on temporal links) and maximally activated event node in the
	 * specified {@link NodeStructure}.
	 */
	public static Node getFirstEventNode(NodeStructure ns, LinkCategory temporalCategory){
		Collection<Node> temporalSources = new HashSet<Node>();
		Collection<Linkable> temporalSinks = new HashSet<Linkable>();
		for(Link l: ns.getLinks()){
			if(l.getCategory().equals(temporalCategory)){
				temporalSources.add(l.getSource());
				temporalSinks.add(l.getSink());
			}
		}
		Collection<Node> firstEventNodes = new ArrayList<Node>();
		for(Node n: temporalSources){
			if(!temporalSinks.contains(n)){ //First in the chain
				firstEventNodes.add(n);
			}
		}
		Node result = null;
		if(firstEventNodes.isEmpty()){
			//No temporal link are in the NodeStructure, get the max.
			result = getMaxEventNode(ns.getNodes()); 
		}else{
			//There may be multiple nodes occurring first in a chain
			result = getMaxEventNode(firstEventNodes); 
		}
		return result;
	}
	
	/**
	 * Returns the maximally activated non-feeling node among the specific Collection.
	 */
	public static Node getMaxEventNode(Collection<Node> nodes) {
		double maxActivation = 0.0;
		Node maxEventNode = null;
		for (Node n: nodes) {
			if (!(n instanceof FeelingNodeImpl)) {
				if (n.getActivation() > maxActivation) {
					maxActivation = n.getActivation();
					maxEventNode = n;
				}
			}
		}
		return maxEventNode;
	}
}
