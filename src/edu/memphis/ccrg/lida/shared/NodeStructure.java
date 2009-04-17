package edu.memphis.ccrg.lida.shared;

import java.util.Set;
import java.util.Collection;
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

	//CHANGED FROM 'addLinkSet(Set<Link> links)'
	public abstract void addLinks(Collection<Link> links);

	public abstract boolean addNode(Node n);

	//THIS SHOULD NOT HAVE THE DOUBLE PARAMETERS and nodes should be a collection?
	public abstract void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity);

	public abstract NodeStructure copy();

	public abstract void deleteLink(Link l);
	
	public abstract void deleteLinkable(Linkable l);

	public abstract void deleteNode(Node n);

	public abstract Collection<Link> getLinks();

	//THIS return COLLECTION?
	public abstract Set<Link> getLinks(Linkable l);

	public abstract Set<Link> getLinks(Linkable NorL, LinkType type);

	public abstract Set<Link> getLinks(LinkType type);

	public abstract Collection<Node> getNodes();

	public abstract void setDefaultLink(String linkClassName);

	public abstract void setDefaultNode(String nodeClassName);

	//Find is nonstandard
	public abstract Node getNode(long id);

	public abstract Link getLink(String ids);
	
	public abstract void combineNodeStructure (NodeStructure ns);
	
}