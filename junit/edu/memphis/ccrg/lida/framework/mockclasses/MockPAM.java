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
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PropagationStrategy;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;

/**
 * @author Javier Snaider
 *
 */
public class MockPAM extends FrameworkModuleImpl implements PerceptualAssociativeMemory {

	private static double perceptThreshold = 0.0;


	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModule#getModuleContent(java.lang.Object[])
	 */
	@Override
	public Object getModuleContent(Object... params) {
		// not implemented
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModule#decayModule(long)
	 */
	@Override
	public void decayModule(long ticks) {
		// not implemented

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
	 */
	@Override
	public void addListener(ModuleListener listener) {
		// not implemented

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializable#init()
	 */
	@Override
	public void init() {
		// not implemented

	}


	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultNode(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public PamNode addDefaultNode(Node node) {
		// not implemented
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultNodes(java.util.Set)
	 */
	@Override
	public Set<PamNode> addDefaultNodes(Set<? extends Node> nodes) {
		// not implemented
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultLinks(java.util.Set)
	 */
	@Override
	public Set<PamLink> addDefaultLinks(Set<? extends Link> links) {
		// not implemented
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addFeatureDetector(edu.memphis.ccrg.lida.pam.tasks.FeatureDetector)
	 */
	@Override
	public void addPerceptualAlgorithm(DetectionAlgorithm fd) {
		// not implemented

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addPamListener(edu.memphis.ccrg.lida.pam.PamListener)
	 */
	@Override
	public void addPamListener(PamListener pl) {
		// not implemented

	}

	@Override
	public void setPropagationStrategy(PropagationStrategy b) {
		// not implemented

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivation(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamLink, double)
	 */
	@Override
	public void propagateActivation(PamNode source, PamNode sink, PamLink link,
			double amount) {
		// not implemented

	}

	/* (non-Javadoc)
	 * Just for test matter
	 */
	private PamNode pmNode;
	
	public PamNode testGetSink(){
		return pmNode;
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivationToParents(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public void propagateActivationToParents(PamNode pamNode) {
		pmNode = pamNode;

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNodeStructureToPercept(edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public void addNodeStructureToPercept(NodeStructure ns) {
		percept = ns;
	}

	private NodeStructure percept;
	
	public NodeStructure getCurrentTestPercept(){
		return percept;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public boolean containsNode(Node node) {
		// not implemented
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsNode(ExtendedId id) {
		// not implemented
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.framework.shared.Link)
	 */
	@Override
	public boolean containsLink(Link link) {
		// not implemented
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLink(ExtendedId id) {
		// not implemented
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setPerceptThreshold(double)
	 */
	@Override
	public void setPerceptThreshold(double t) {
		MockPAM.perceptThreshold =t;

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setUpscaleFactor(double)
	 */
	@Override
	public void setUpscaleFactor(double f) {
		// not implemented

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getUpscaleFactor()
	 */
	@Override
	public double getUpscaleFactor() {
		// not implemented
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setDownscaleFactor(double)
	 */
	@Override
	public void setDownscaleFactor(double f) {
		// not implemented

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getDownscaleFactor()
	 */
	@Override
	public double getDownscaleFactor() {
		// not implemented
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#isOverPerceptThreshold(edu.memphis.ccrg.lida.pam.PamLinkable)
	 */
	@Override
	public boolean isOverPerceptThreshold(PamLinkable l) {
		// not implemented
		return l.getTotalActivation()>= MockPAM.perceptThreshold;

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
	public void receiveActivationBurst(PamLinkable nodeId, double amount) {
	}

	@Override
	public void receiveActivationBurst(Set<PamLinkable> nodeIds, double amount) {
	}

	@Override
	public PropagationStrategy getPropagationStrategy() {
		return null;
	}
}
