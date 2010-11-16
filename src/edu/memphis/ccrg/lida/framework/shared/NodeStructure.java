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

	//Add
	
	public Link addLink(Link l);
	
	public Link addLink(ExtendedId idSource, ExtendedId idSink, LinkCategory type, double activation);

	public void addLinks(Collection<Link> links);
	
	public Node addNode(Node n);

	public Node addNode(Node n, String factoryName);

	public void addNodes(Collection<Node> nodes);
	
	//Delete
	public void removeLink(Link l);
	
	public void removeNode(Node n);
	
	public void removeLinkable(Linkable l);
	
//	//TODO maybe a general clear but this method is not used and potentially problematic if 
//	public void clearNodes();
	
	//Contains
	public boolean containsNode(Node n);

	public boolean containsLink(Link l);
	
	//Other methods
	public void mergeWith(NodeStructure ns);
	
	public NodeStructure copy();
	
	//Gets
	
	//Links
	public Link getLink(ExtendedId ids);
	
	public Collection<Link> getLinks();

	public Set<Link> getLinks(Linkable l);

	public Set<Link> getLinks(LinkCategory type);
	
	public Set<Link> getLinks(Linkable NorL, LinkCategory type);

	//Nodes
	public Node getNode(int id);

	public Node getNode(ExtendedId ids);
	
	public Collection<Node> getNodes();

	//Linkables
	public Linkable getLinkable (ExtendedId ids);

	public Collection<Linkable> getLinkables();
	
	public Map<Linkable, Set<Link>> getLinkableMap();

	public int getNodeCount();

	public int getLinkCount();
	
	public int getLinkableCount();
	
	//Node & Link type
	public String getDefaultNodeType();
	
	public String getDefaultLinkType();
	
	public void setDefaultLink(String linkClassName);

	public void setDefaultNode(String nodeClassName);
	
}