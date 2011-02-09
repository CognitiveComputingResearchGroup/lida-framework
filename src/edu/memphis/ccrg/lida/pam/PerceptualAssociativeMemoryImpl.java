/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.UnmodifiableNodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.tasks.ExcitationTask;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceListener;

/**
 * Default implementation of {@link PerceptualAssociativeMemory}.  Module essentially concerned with PamNode and PamLinks, source of meaning in LIDA, 
 * how they are activated and how they pass activation among themselves.
 * 
 * @author Ryan J. McCall
 */
public class PerceptualAssociativeMemoryImpl extends LidaModuleImpl implements
		PerceptualAssociativeMemory, BroadcastListener, WorkspaceListener,
		PreafferenceListener {

	private static final Logger logger = Logger
			.getLogger(PerceptualAssociativeMemoryImpl.class.getCanonicalName());

	/*
	 * Contains all of the Node, Links and their connections.
	 */
	private NodeStructure nodeStructure;

	/*
	 * Modules listening for the current percept
	 */
	private List<PamListener> pamListeners;

	/*
	 * How PAM calculates the amount of activation to propagate
	 */
	private PropagationBehavior propagationBehavior;

	/*
	 * To create new node and links
	 */
	private LidaElementFactory factory = LidaElementFactory.getInstance();

	private static final int DEFAULT_EXCITATION_TASK_TICKS = 1;
	private int excitationTaskTicksPerRun = DEFAULT_EXCITATION_TASK_TICKS;

	private static final int DEFAULT_PROPAGATION_TASK_TICKS = 1;
	private int propagationTaskTicksPerRun = DEFAULT_PROPAGATION_TASK_TICKS;

	private static final double DEFAULT_PERCEPT_THRESHOLD = 0.7;
	private static double perceptThreshold = DEFAULT_PERCEPT_THRESHOLD;

	private static final double DEFAULT_UPSCALE_FACTOR = 0.9;
	private double upscaleFactor = DEFAULT_UPSCALE_FACTOR;

	private static final double DEFAULT_DOWNSCALE_FACTOR = 0.5;
	private double downscaleFactor = DEFAULT_DOWNSCALE_FACTOR;

	/**
	 * Default constructor.
	 */
	public PerceptualAssociativeMemoryImpl() {
		super(ModuleName.PerceptualAssociativeMemory);
		nodeStructure = new NodeStructureImpl(PamNodeImpl.class.getSimpleName(), PamLinkImpl.class.getSimpleName());
		pamListeners = new ArrayList<PamListener>();
		propagationBehavior = new UpscalePropagationBehavior();
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#init()
	 */
	@Override
	public void init() {
		setDefaultNodeType((String) getParam("pam.newNodeType", PamNodeImpl.class.getSimpleName()));
		setDefaultLinkType((String) getParam("pam.newLinkType", PamLinkImpl.class.getSimpleName()));
		
		upscaleFactor=(Double)getParam("pam.Upscale",DEFAULT_UPSCALE_FACTOR);
		downscaleFactor=(Double)getParam("pam.Downscale",DEFAULT_DOWNSCALE_FACTOR);
		perceptThreshold=(Double)getParam("pam.Selectivity",
				DEFAULT_PERCEPT_THRESHOLD);
		excitationTaskTicksPerRun = (Integer) getParam(
				"pam.excitationTicksPerRun", DEFAULT_EXCITATION_TASK_TICKS);
		propagationTaskTicksPerRun = (Integer) getParam(
				"pam.propagationTicksPerRun", DEFAULT_PROPAGATION_TASK_TICKS);
	}

	/*
	 * Set the propagation behavior for this PAM.
	 *
	 * @param b the new propagation behavior
	 */
	@Override
	public void setPropagationBehavior(PropagationBehavior b) {
		propagationBehavior = b;
	}

	/*
	 * Get the propagation behavior for this PAM.
	 *
	 * @return the propagation behavior
	 */
	public PropagationBehavior getPropagationBehavior() {
		return propagationBehavior;
	}

	/*
	 *
	 * Adds set of PamNodes to this PAM.
	 *
	 * @param nodes the nodes
	 * @return the Sets
	 */
	@Override
	public Set<PamNode> addDefaultNodes(Set<? extends Node> nodes) {
		Set<PamNode> copiedNodes = new HashSet<PamNode>();
		for (Node l : nodes)
			copiedNodes.add((PamNode) nodeStructure.addDefaultNode(l));
		return copiedNodes;
	}

	/*
	 * Adds set of PamLinks to this PAM.
	 *
	 * @param links the links
	 * @return the Sets
	 */
	@Override
	public Set<PamLink> addDefaultLinks(Set<? extends Link> links) {
		Set<PamLink> copiedLinks = new HashSet<PamLink>();
		for (Link l : links)
			copiedLinks.add((PamLink) nodeStructure.addDefaultLink(l));
		return copiedLinks;
	}

	/*
	 * Adds a feature detector to this PAM.
	 *
	 * @param detector the detector
	 */
	@Override
	public void addFeatureDetector(FeatureDetector detector) {
		taskSpawner.addTask(detector);
		logger.log(Level.FINE, "Added feature detector to PAM",
				LidaTaskManager.getCurrentTick());
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addPamListener(edu.memphis.ccrg.lida.pam.PamListener)
	 */
	@Override
	public void addPamListener(PamListener pl) {
		pamListeners.add(pl);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.workspace.main.WorkspaceListener#receiveWorkspaceContent(edu.memphis.ccrg.lida.framework.ModuleName, edu.memphis.ccrg.lida.workspace.main.WorkspaceContent)
	 */
	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		NodeStructure ns = (NodeStructure) bc;
		taskSpawner.addTask(new ProcessBroadcastTask(ns.copy()));
	}
	private class ProcessBroadcastTask extends LidaTaskImpl {
		private NodeStructure broadcast;
		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}
		@Override
		protected void runThisLidaTask() {
			learn((BroadcastContent) broadcast);
			setTaskStatus(LidaTaskStatus.FINISHED);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.globalworkspace.BroadcastListener#receiveBroadcast(edu.memphis.ccrg.lida.globalworkspace.BroadcastContent)
	 */
	@Override
	public synchronized void receiveWorkspaceContent(ModuleName originatingBuffer, WorkspaceContent content) {
//		NodeStructure ns = (NodeStructure) content;
//		LidaTask t = new FooTask(ns.copy());
//		taskSpawner.addTask(t);
		//TODO Task
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener#receivePreafference(edu.memphis.ccrg.lida.framework.shared.NodeStructure, edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public synchronized void receivePreafference(NodeStructure addList, NodeStructure deleteList) {
		//TODO task to use preafferent signal
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.globalworkspace.BroadcastListener#learn(edu.memphis.ccrg.lida.globalworkspace.BroadcastContent)
	 */
	@Override
	public void learn(BroadcastContent bc) {
		NodeStructure ns = (NodeStructure) bc;
		//TODO learning algorithm
		Collection<Node> nodes = ns.getNodes();
		for (Node n : nodes) {
			n.getId();
		}
	}

	/*
	 * Called by the task manager every tick.
	 *
	 * @param ticks the ticks
	 */
	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		nodeStructure.decayNodeStructure(ticks);
	}

	/*
	 * Receive activation from feature detectors or other codelets to excite a
	 * PamNode.
	 *
	 * @param node the node
	 * @param amount the amount
	 */
	@Override
	public void receiveActivationBurst(PamNode node, double amount) {
		logger.log(Level.FINE, node.getLabel()
				+ " gets activation burst. Amount: " + amount
				+ ", total activation: " + node.getTotalActivation(),
				LidaTaskManager.getCurrentTick());
		ExcitationTask task = new ExcitationTask(node, amount,
				excitationTaskTicksPerRun, this, taskSpawner);
		taskSpawner.addTask(task);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#receiveActivationBurst(java.util.Set, double)
	 */
	@Override
	public void receiveActivationBurst(Set<PamNode> nodes, double amount) {
		for (PamNode n : nodes){
			receiveActivationBurst(n, amount);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivationToParents(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public void propagateActivationToParents(PamNode pn) {
		// Calculate the amount to propagate
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		propagateParams.put("upscale", upscaleFactor);
		propagateParams.put("totalActivation", pn.getTotalActivation());
		double amountToPropagate = propagationBehavior
				.getActivationToPropagate(propagateParams);

		// Get parents of pamNode and the connecting link
		Map<Node, Link> parentLinkMap = nodeStructure.getParentLinkMap(pn);
		for (Node parent : parentLinkMap.keySet()) {
			// Excite the connecting link and the parent
			propagateActivation(pn, (PamNode)parent, (PamLink)parentLinkMap.get(parent), amountToPropagate);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivation(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamLink, double)
	 */
	@Override
	public void propagateActivation(PamNode source, PamNode sink, PamLink link,
			double amount) {
		logger.log(Level.FINE, "exciting parent: " + sink.getLabel()
				+ " and connecting link " + link.getLabel() + " amount: "
				+ amount, LidaTaskManager.getCurrentTick());
		PropagationTask task = new PropagationTask(source, link, sink, amount,
				this, taskSpawner);
		task.setTicksPerStep(propagationTaskTicksPerRun);
		taskSpawner.addTask(task);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNodeStructureToPercept(edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public void addNodeStructureToPercept(NodeStructure ns) {
		for (int i = 0; i < pamListeners.size(); i++){
			pamListeners.get(i).receivePercept(ns);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public boolean containsNode(Node node) {
		return nodeStructure.containsNode(node);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsNode(ExtendedId id) {
		return nodeStructure.containsNode(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.pam.PamLink)
	 */
	@Override
	public boolean containsLink(Link link) {
		return nodeStructure.containsLink(link);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLink(ExtendedId id) {
		return nodeStructure.containsLink(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNodes()
	 */
	@Override
	public Collection<Node> getPamNodes() {
		Collection<Node> pamNodes = nodeStructure.getNodes();
		Collection<Node> copiedNodes = new ArrayList<Node>();
		for (Node pamNode : pamNodes){
			copiedNodes.add(factory.getNode(pamNode));
		}
		return Collections.unmodifiableCollection(copiedNodes);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#getModuleContent(java.lang.Object[])
	 */
	@Override
	public Object getModuleContent(Object... params) {
		return new UnmodifiableNodeStructureImpl(nodeStructure);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
	 */
	@Override
	public void addListener(ModuleListener l) {
		if (l instanceof PamListener) {
			addPamListener((PamListener) l);
		}else{
			logger.log(Level.WARNING, "Cannot add listener type " + l.toString() + " to this module.", LidaTaskManager.getCurrentTick());
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNode(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public PamNode addDefaultNode(Node n) {
		return (PamNode) nodeStructure.addDefaultNode(n);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewLink(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double)
	 */
	@Override
	public Link addNewLink(Node src, Node snk, LinkCategory cat, double activ) {
		return nodeStructure.addLink(src.getExtendedId(), snk.getExtendedId(), cat, activ);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double)
	 */
	@Override
	public Link addNewLink(ExtendedId srcId, ExtendedId sinkId,
			LinkCategory cat, double activ) {
		return nodeStructure.addLink(srcId, sinkId, cat, activ);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewNode(java.lang.String)
	 */
	@Override
	public PamNode addNewNode(String label) {
		PamNode newNode = (PamNode) factory.getNode(nodeStructure.getDefaultNodeType(), label);
		if(newNode != null){
			newNode = (PamNode) nodeStructure.addDefaultNode(newNode);
			return newNode;
		}else{
			logger.log(Level.WARNING, "Was unable to get node labeled " + label + " of type " + nodeStructure.getDefaultNodeType(), LidaTaskManager.getCurrentTick());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewNode(java.lang.String, java.lang.String)
	 */
	@Override
	public PamNode addNewNode(String type, String label) {
		Node newNode = factory.getNode(type, label);
		if (newNode != null) {
			if(newNode instanceof PamNode){
				return (PamNode) nodeStructure.addNode(newNode, type);
			}else{
				logger.log(Level.WARNING, "Cannot add non-PamNode nodes to PAM.  Node " + label + " not added", LidaTaskManager.getCurrentTick());
				return null;
			}
		}else{
			logger.log(Level.WARNING, "Was unable to create node " + label + " of type " + type, LidaTaskManager.getCurrentTick());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setNewNodeType(java.lang.String)
	 */
	@Override
	public void setDefaultNodeType(String type) {
		nodeStructure.setDefaultNode(type);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setNewLinkType(java.lang.String)
	 */
	@Override
	public void setDefaultLinkType(String type) {
		nodeStructure.setDefaultLink(type);
	}

	public static double getPerceptThreshold() {
		return perceptThreshold;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setPerceptThreshold(double)
	 */
	@Override
	public void setPerceptThreshold(double t) {
		if(t < 1.0){
			PerceptualAssociativeMemoryImpl.perceptThreshold = t;
		}else{
			logger.log(Level.WARNING, "Cannot set percept threshold to 1.0 or higher.  Percept threshold will not be modified.", LidaTaskManager.getCurrentTick());
		}
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#isOverPerceptThreshold(edu.memphis.ccrg.lida.pam.PamLinkable)
	 */
	@Override
	public boolean isOverPerceptThreshold(PamLinkable l) {
		logger.log(Level.FINEST, l.getTotalActivation() +" >? " + perceptThreshold, LidaTaskManager.getCurrentTick());		
		return l.getTotalActivation() > perceptThreshold;
	}

	@Override
	public double getUpscaleFactor() {
		return upscaleFactor;
	}
	@Override
	public void setUpscaleFactor(double f) {
		upscaleFactor = f;		
	}

	@Override
	public double getDownscaleFactor() {
		return downscaleFactor;
	}
	@Override
	public void setDownscaleFactor(double f) {
		downscaleFactor = f;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Link getPamLink(ExtendedId eid) {
		return factory.getLink(nodeStructure.getLink(eid));
	}
	

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Node getPamNode(ExtendedId eid) {
		return factory.getNode(nodeStructure.getNode(eid));
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNode(int)
	 */
	@Override
	public Node getPamNode(int id) {
		return factory.getNode(nodeStructure.getNode(id));
	}

	@Override
	public Object getState() {
		return null;
	}

	@Override
	public boolean setState(Object content) {
		return false;
	}
	
}