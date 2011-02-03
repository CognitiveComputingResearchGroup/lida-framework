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
	private PamNodeStructure pamNodeStructure;

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
	private double perceptThreshold = DEFAULT_PERCEPT_THRESHOLD;

	private static final double DEFAULT_UPSCALE_FACTOR = 0.9;
	private double upscaleFactor = DEFAULT_UPSCALE_FACTOR;

	private static final double DEFAULT_DOWNSCALE_FACTOR = 0.5;
	private double downscaleFactor = DEFAULT_DOWNSCALE_FACTOR;

	/**
	 * Instantiates a new perceptual associative memory impl.
	 */
	public PerceptualAssociativeMemoryImpl() {
		super(ModuleName.PerceptualAssociativeMemory);
		pamNodeStructure = new PamNodeStructureImpl();
		pamListeners = new ArrayList<PamListener>();
		propagationBehavior = new UpscalePropagationBehavior();
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

	/**
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
	public Set<PamNode> addNodes(Set<PamNode> nodes) {
		Set<PamNode> copiedNodes = new HashSet<PamNode>();
		for (PamNode l : nodes)
			copiedNodes.add((PamNode) pamNodeStructure.addNode(l));
		return copiedNodes;
	}

	/*
	 * Adds set of PamLinks to this PAM.
	 *
	 * @param links the links
	 * @return the Sets
	 */
	@Override
	public Set<PamLink> addLinks(Set<PamLink> links) {
		Set<PamLink> copiedLinks = new HashSet<PamLink>();
		for (PamLink l : links)
			copiedLinks.add((PamLink) pamNodeStructure.addLink(l));
		return copiedLinks;
	}

	/**
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
	public void learn(BroadcastContent content) {
		NodeStructure ns = (NodeStructure) content;
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
		pamNodeStructure.decayNodeStructure(ticks);
	}

	/**
	 * Receive activation from feature detectors or other codelets to excite a
	 * PamNode.
	 *
	 * @param node the node
	 * @param amount the amount
	 */
	@Override
	//TODO synchronize?????
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
		for (PamNode n : nodes)
			receiveActivationBurst(n, amount);
	}

	/**
	 * Called by Excitation tasks after their node has been excited. Based on
	 * PAM's propagation behavior, propagate activation from 'pamNode' to its
	 * parents and to the connecting links.
	 *
	 * @param pamNode the pam node
	 */
	@Override
	public void propagateActivationToParents(PamNode pamNode) {
		// Calculate the amount to propagate
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		propagateParams.put("upscale", upscaleFactor);
		propagateParams.put("totalActivation", pamNode.getTotalActivation());
		double amountToPropagate = propagationBehavior
				.getActivationToPropagate(propagateParams);

		// Get parents of pamNode and the connecting link
		Map<PamNode, PamLink> parentLinkMap = pamNodeStructure.getParentsWithLinks(pamNode);
		for (PamNode parent : parentLinkMap.keySet()) {
			// Excite the connecting link and the parent
			propagateActivation(pamNode, parentLinkMap.get(parent), parent, amountToPropagate);
		}
	}

	/**
	 * Propagates activation 'amount', which originates from 'source', to node
	 * 'sink'. Also give activation to 'link' which connects 'source' to 'sink'.
	 *
	 * @param source the source
	 * @param link the link
	 * @param sink the sink
	 * @param amount the amount
	 */
	@Override
	public void propagateActivation(PamNode source, PamLink link, PamNode sink,
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
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNodeToPercept(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public void addNodeToPercept(PamNode pamNode) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNode(pamNode);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addLinkToPercept(edu.memphis.ccrg.lida.pam.PamLink)
	 */
	@Override
	public void addLinkToPercept(PamLink pamLink) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveLink(pamLink);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNodeStructureToPercept(edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public void addNodeStructureToPercept(NodeStructure nodeStructure) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNodeStructure(nodeStructure);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#init()
	 */
	@Override
	public void init() {
		//TODO ACK
		setNewNodeType((String) getParam("pam.newNodeType", PamNodeImpl.class.getSimpleName()));
		setNewLinkType((String) getParam("pam.newLinkType", PamLinkImpl.class.getSimpleName()));
		
		upscaleFactor = (Double) getParam("pam.Upscale", DEFAULT_UPSCALE_FACTOR);
		downscaleFactor = (Double) getParam("pam.Downscale",
				DEFAULT_DOWNSCALE_FACTOR);
		perceptThreshold = (Double) getParam("pam.Selectivity",
				DEFAULT_PERCEPT_THRESHOLD);
		excitationTaskTicksPerRun = (Integer) getParam(
				"pam.excitationTicksPerRun", DEFAULT_EXCITATION_TASK_TICKS);
		propagationTaskTicksPerRun = (Integer) getParam(
				"pam.propagationTicksPerRun", DEFAULT_PROPAGATION_TASK_TICKS);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public boolean containsNode(PamNode node) {
		return pamNodeStructure.containsNode(node);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsNode(ExtendedId id) {
		return pamNodeStructure.containsNode(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.pam.PamLink)
	 */
	@Override
	public boolean containsLink(PamLink link) {
		return pamNodeStructure.containsLink(link);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLink(ExtendedId id) {
		return pamNodeStructure.containsLink(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNodes()
	 */
	@Override
	public Collection<Node> getPamNodes() {
		Collection<Node> pamNodes = pamNodeStructure.getNodes();
		Collection<Node> copiedNodes = new ArrayList<Node>();
		for (Node pamNode : pamNodes)
			copiedNodes.add(factory.getNode(pamNode));
		return Collections.unmodifiableCollection(copiedNodes);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#getModuleContent(java.lang.Object[])
	 */
	@Override
	public Object getModuleContent(Object... params) {
		//TODO is this bad?
		return pamNodeStructure;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
	 */
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof PamListener) {
			addPamListener((PamListener) listener);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNode(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public PamNode addNode(PamNode node) {
		return (PamNode) pamNodeStructure.addNode(node);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewLink(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double)
	 */
	@Override
	public Link addNewLink(PamNode source, PamNode sink, LinkCategory type,
			double activation) {
		return pamNodeStructure.addLink(source.getExtendedId(),
				sink.getExtendedId(), type, activation);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double)
	 */
	@Override
	public Link addNewLink(ExtendedId sourceId, ExtendedId sinkId,
			LinkCategory type, double activation) {
		return pamNodeStructure.addLink(sourceId, sinkId, type, activation);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewNode(java.lang.String)
	 */
	@Override
	public PamNode addNewNode(String label) {
		PamNode newNode = (PamNode) factory.getNode(pamNodeStructure.getDefaultNodeType(), label);
		if(newNode != null){
			newNode = (PamNode) pamNodeStructure.addNode(newNode);
			return newNode;
		}else{
			logger.log(Level.WARNING, "Was unable to get node labeled " + label + " of type " + pamNodeStructure.getDefaultNodeType(), LidaTaskManager.getCurrentTick());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewNode(java.lang.String, java.lang.String)
	 */
	@Override
	public PamNode addNewNode(String pamNodeType, String label) {
		PamNode newNode = (PamNode) factory.getNode(pamNodeType, label);
		if (newNode != null) {
			newNode = (PamNode) pamNodeStructure.addNode(newNode);
			return newNode;
		}else{
			logger.log(Level.WARNING, "Was unable to get node labeled " + label + " of type " + pamNodeType, LidaTaskManager.getCurrentTick());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setNewNodeType(java.lang.String)
	 */
	@Override
	public void setNewNodeType(String type) {
		pamNodeStructure.setDefaultNode(type);
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setNewLinkType(java.lang.String)
	 */
	@Override
	public void setNewLinkType(String type) {
		pamNodeStructure.setDefaultLink(type);
	}

	@Override
	public double getPerceptThreshold() {
		return perceptThreshold;
	}
	@Override
	public void setPerceptThreshold(double t) {
		perceptThreshold = t;		
	}
	@Override
	public boolean isOverPerceptThreshold(PamLinkable l) {
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
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#isOverPerceptThreshold(edu.memphis.ccrg.lida.pam.PamLinkable)
	 */
	@Override
	public Link getPamLink(ExtendedId id) {
		return factory.getLink(pamNodeStructure.getLink(id));
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.dao.Saveable#getState()
	 */
	@Override
	public Node getPamNode(int id) {
		return factory.getNode(pamNodeStructure.getNode(id));
	}
	
	@Override
	public Object getState() {
		return pamNodeStructure;
	}

	//TODO REMOVE???????????????????????????????????????????
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.dao.Saveable#setState(java.lang.Object)
	 */
	@Override
	public boolean setState(Object content) {
		if (content instanceof PamNodeStructureImpl) {
			pamNodeStructure = (PamNodeStructureImpl) content;
			return true;
		}
		return false;
	}

	@Override
	public Node getPamNode(ExtendedId id) {
		// TODO Auto-generated method stub
		return null;
	}	
}