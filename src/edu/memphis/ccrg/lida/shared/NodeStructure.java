package edu.memphis.ccrg.lida.shared;

import java.util.Collection;
import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 * A NodeStructure holds a collection of Nodes an Links. An implementation of this interface
 * could be used as the "Common Currency" between the Modules of LIDA
 * 
 * 
 * 
 * @author Javier Snaider
 *
 */
public interface NodeStructure extends BroadcastContent, WorkspaceContent {

	public abstract boolean addLink(Link l);

	public abstract void addLinkSet(Set<Link> links);

	public abstract boolean addNode(Node n);

	public abstract void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity);

	public abstract NodeStructure copy();

	public abstract void deleteLink(Link l);
	
	public abstract void deleteLinkable(Linkable l);

	public abstract void deleteNode(Node n);

	public abstract Collection<Link> getLinks();

	public abstract Set<Link> getLinks(Linkable l);

	public abstract Set<Link> getLinks(Linkable NorL, LinkType type);

	public abstract Set<Link> getLinks(LinkType type);

	public abstract Collection<Node> getNodes();

	public abstract void setDefaultLink(String linkClassName);

	public abstract void setDefaultNode(String nodeClassName);

	public abstract Node findNode(long id);

	public abstract Link findLink (String ids);
	
	public abstract void combineNodeStructure (NodeStructure ns);
	
}