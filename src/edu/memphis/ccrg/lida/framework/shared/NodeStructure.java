/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
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
 * A NodeStructure holds a collection of Nodes an Links. An implementation of this interface
 * could be used as the "Common Currency" between the Modules of LIDA
 *
 * @author Javier Snaider
 */
public interface NodeStructure {

	public abstract Link addLink(Link l);
	
	public Link addLink(ExtendedId idSource, ExtendedId idSink, LinkCategory type, double activation);

	public abstract void addLinks(Collection<Link> links);
	
	public abstract Node addNode(Node n);

	public abstract Node addNode(Node n, String factoryName);

	public abstract void addNodes(Collection<Node> nodes);

	public abstract NodeStructure copy();

	public abstract void deleteLink(Link l);
	
	public abstract void deleteLinkable(Linkable l);

	public abstract void deleteNode(Node n);
	
	public abstract void clearNodes();

	public abstract Collection<Link> getLinks();

	public abstract Set<Link> getLinks(Linkable l);

	public abstract Set<Link> getLinks(Linkable NorL, LinkCategory type);

	public abstract Set<Link> getLinks(LinkCategory type);

	public abstract Collection<Node> getNodes();

	public abstract void setDefaultLink(String linkClassName);

	public abstract void setDefaultNode(String nodeClassName);

	public abstract Node getNode(int id);

	public abstract Node getNode(ExtendedId ids);

	public abstract Link getLink (ExtendedId ids);

	public abstract Linkable getLinkable (ExtendedId ids);
	
	public abstract void mergeWith (NodeStructure ns);

	public abstract Map<Linkable, Set<Link>> getLinkableMap();

	public abstract boolean containsNode(Node n);

	public abstract boolean containsLink(Link l);

	public abstract int getNodeCount();

	public abstract int getLinkCount();
	
	public abstract int getLinkableCount();
	public abstract Collection<Linkable> getLinkables();
	
	public abstract String getDefaultNodeType();
	public abstract String getDefaultLinkType();
	
	
	//redundant with getLinkableCount
	public abstract String getNodeAndLinkCount();
	
}