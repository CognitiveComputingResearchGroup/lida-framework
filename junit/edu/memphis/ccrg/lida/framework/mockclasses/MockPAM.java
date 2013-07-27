/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Collection;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.ns.Linkable;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.pam.ns.PamLink;
import edu.memphis.ccrg.lida.pam.ns.PamLinkable;
import edu.memphis.ccrg.lida.pam.ns.PamListener;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNS;
import edu.memphis.ccrg.lida.pam.ns.PropagationStrategy;
import edu.memphis.ccrg.lida.pam.ns.tasks.DetectionAlgorithm;

/**
 * @author Javier Snaider
 * 
 */
public class MockPAM extends FrameworkModuleImpl implements
		PerceptualAssociativeMemoryNS {

	private static double perceptThreshold = 0.0;

	@Override
	public Object getModuleContent(Object... params) {
		// not implemented
		return null;
	}

	@Override
	public void decayModule(long ticks) {
		// not implemented

	}

	@Override
	public PamNode addDefaultNode(Node node) {
		// not implemented
		return null;
	}

	@Override
	public Set<PamNode> addDefaultNodes(Set<? extends Node> nodes) {
		// not implemented
		return null;
	}

	@Override
	public Set<PamLink> addDefaultLinks(Set<? extends Link> links) {
		// not implemented
		return null;
	}

	@Override
	public void addDetectionAlgorithm(DetectionAlgorithm fd) {
		// not implemented

	}

	@Override
	public void addPamListener(PamListener pl) {
		// not implemented

	}

	@Override
	public void setPropagationStrategy(PropagationStrategy b) {
		// not implemented

	}

	public PamNode pmNode;

	@Override
	public void propagateActivationToParents(PamNode pamNode) {
		pmNode = pamNode;
	}

	@Override
	public void addToPercept(NodeStructure ns) {
		nsPercept = ns;
	}

	public NodeStructure nsPercept;

	@Override
	public boolean containsNode(Node node) {
		// not implemented
		return false;
	}

	@Override
	public boolean containsNode(ExtendedId id) {
		// not implemented
		return false;
	}

	@Override
	public boolean containsLink(Link link) {
		// not implemented
		return false;
	}

	@Override
	public boolean containsLink(ExtendedId id) {
		// not implemented
		return false;
	}

	@Override
	public void setPerceptThreshold(double t) {
		MockPAM.perceptThreshold = t;

	}

	@Override
	public void setUpscaleFactor(double f) {
		upscaleFactor = f;
	}

	public double upscaleFactor;

	@Override
	public double getUpscaleFactor() {
		return upscaleFactor;
	}

	@Override
	public void setDownscaleFactor(double f) {
		// not implemented

	}

	@Override
	public double getDownscaleFactor() {
		// not implemented
		return 0;
	}

	@Override
	public boolean isOverPerceptThreshold(PamLinkable l) {
		// not implemented
		return l.getTotalActivation() >= MockPAM.perceptThreshold;

	}

	@Override
	public Node getNode(int id) {
		Node n = new PamNodeImpl();
		n.setId(id);
		return n;
	}

	@Override
	public Node getNode(ExtendedId id) {
		// not implemented
		return null;
	}

	@Override
	public Link getLink(ExtendedId id) {
		// not implemented
		return null;
	}

	@Override
	public Collection<Node> getNodes() {
		return null;
	}

	@Override
	public Collection<Link> getLinks() {
		return null;
	}

	@Override
	public FrameworkModule getSubmodule(String name) {
		return null;
	}

	@Override
	public PamLink addDefaultLink(Link link) {
		return null;
	}

	@Override
	public LinkCategory addLinkCategory(LinkCategory cat) {
		return null;
	}

	@Override
	public Collection<LinkCategory> getLinkCategories() {
		return null;
	}

	@Override
	public LinkCategory getLinkCategory(int id) {
		return null;
	}

	@Override
	public void receiveExcitation(PamLinkable nodeId, double amount) {
	}

	@Override
	public void receiveExcitation(Set<PamLinkable> nodeIds, double amount) {
	}

	@Override
	public PropagationStrategy getPropagationStrategy() {
		return null;
	}

	public Link linkPercept;

	@Override
	public void addToPercept(Link l) {
		linkPercept = l;
	}

	public Node nodePercept;

	@Override
	public void addToPercept(Node n) {
		nodePercept = n;
	}

	@Override
	public Node getNode(String label) {
		return null;
	}

	@Override
	public PamLink addDefaultLink(Node src, Linkable snk, LinkCategory cat) {
		return null;
	}

	@Override
	public PamNode addDefaultNode(String label) {
		return null;
	}

	@Override
	public PamLink addLink(String type, Node src, Linkable snk, LinkCategory cat) {
		return null;
	}

	@Override
	public PamNode addNode(String type, String label) {
		return null;
	}
}
