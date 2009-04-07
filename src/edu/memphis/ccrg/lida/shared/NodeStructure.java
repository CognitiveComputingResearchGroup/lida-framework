package edu.memphis.ccrg.lida.shared;

import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;

/**
 * A NodeStructure holds a collection of Nodes an Links. An implementation of this interface
 * could be used as the "Common Currency" between the Modules of LIDA
 * 
 * 
 * 
 * @author Javier Snaider
 *
 */
public interface NodeStructure extends BroadcastContent {

	public abstract boolean addLink(Link l);

	public abstract void addLinkSet(Set<Link> links);

	public abstract boolean addNode(Node n);

	public abstract void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity);

	public NodeStructure copy();

	public abstract void deleteLink(Link l);
	
	public abstract void deleteLinkable(Linkable l);

	public abstract void deleteNode(Node n);

	public abstract Set<Link> getLinks();

	public abstract Set<Link> getLinks(Linkable l);

	public abstract Set<Link> getLinks(Linkable NorL, LinkType type);

	public abstract Set<Link> getLinks(LinkType type);

	public abstract Set<Node> getNodes();

	public void setDefaultLink(String linkClassName);

	public void setDefaultNode(String nodeClassName);

}