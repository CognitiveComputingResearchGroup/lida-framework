/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.UnmodifiableNodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.ExcitationTask;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceListener;

/**
 * Default implementation of {@link PerceptualAssociativeMemory}. Module
 * essentially concerned with PamNode and PamLinks, source of meaning in LIDA,
 * how they are activated and how they pass activation among themselves.
 * 
 * @author Ryan J. McCall
 */
public class PerceptualAssociativeMemoryImpl extends FrameworkModuleImpl
		implements PerceptualAssociativeMemory, BroadcastListener,
		WorkspaceListener, PreafferenceListener {

	private static final String DEFAULT_NONDECAYING_PAMNODE = "NoDecayPamNode";

	private static final Logger logger = Logger
			.getLogger(PerceptualAssociativeMemoryImpl.class.getCanonicalName());

	/*
	 * Modules listening for the current percept
	 */
	private List<PamListener> pamListeners;

	/**
	 * A {@link NodeStructure} which contains all of the {@link PamNode}, {@link PamLink} and their connections.
	 */
	protected PamNodeStructure pamNodeStructure;

	/*
	 * How PAM calculates the amount of activation to propagate
	 */
	private PropagationStrategy propagationStrategy;

	/*
	 * To create new node and links
	 */
	private static ElementFactory factory = ElementFactory.getInstance();

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

	private Map<Integer, LinkCategory> linkCategories = new HashMap<Integer, LinkCategory>();

	/**
	 * If using this variable in a non-standard LIDA application (e.g. testing)
	 * note that it is not initialized until constructor runs.
	 */
	public static LinkCategory NONE = (PamNode) factory.getNode(
			DEFAULT_NONDECAYING_PAMNODE, "None");

	/**
	 * If using this variable in a non-standard LIDA application (e.g. testing)
	 * note that it is not initialized until constructor runs.
	 */
	public static LinkCategory LATERAL = (PamNode) factory.getNode(
			DEFAULT_NONDECAYING_PAMNODE, "Lateral");

	/**
	 * If using this variable in a non-standard LIDA application (e.g. testing)
	 * note that it is not initialized until constructor runs.
	 */
	public static LinkCategory MEMBERSHIP = (PamNode) factory.getNode(
			DEFAULT_NONDECAYING_PAMNODE, "Membership");

	/**
	 * If using this variable in a non-standard LIDA application (e.g. testing)
	 * note that it is not initialized until constructor runs.
	 */
	public static LinkCategory FEATURE = (PamNode) factory.getNode(
			DEFAULT_NONDECAYING_PAMNODE, "Feature");

	/**
	 * Default constructor.
	 */
	public PerceptualAssociativeMemoryImpl() {
		pamListeners = new ArrayList<PamListener>();
		propagationStrategy = new UpscalePropagationStrategy();
		pamNodeStructure = new PamNodeStructure("PamNodeImpl", "PamLinkImpl");

		addInternalLinkCategory(NONE);
		addInternalLinkCategory(LATERAL);
		addInternalLinkCategory(MEMBERSHIP);
		addInternalLinkCategory(FEATURE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#init()
	 */
	@Override
	public void init() {
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

	@Override
	public void setPropagationStrategy(PropagationStrategy b) {
		propagationStrategy = b;
	}

	@Override
	public PropagationStrategy getPropagationStrategy() {
		return propagationStrategy;
	}
	
	/**
	 * @return the excitationTaskTicksPerRun
	 */
	public int getExcitationTaskTicksPerRun() {
		return excitationTaskTicksPerRun;
	}

	/**
	 * @return the propagationTaskTicksPerRun
	 */
	public int getPropagationTaskTicksPerRun() {
		return propagationTaskTicksPerRun;
	}

	/*
	 * 
	 * Adds set of PamNodes to this PAM.
	 * 
	 * @param nodes the nodes
	 * 
	 * @return the Sets
	 */
	@Override
	public Set<PamNode> addDefaultNodes(Set<? extends Node> nodes) {
		if (nodes == null) {
			return null;
		}
		Set<PamNode> storedNodes = new HashSet<PamNode>();
		for (Node n : nodes) {
			storedNodes.add(addDefaultNode(n));
		}
		return storedNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNode(edu.memphis
	 * .ccrg.lida.pam.PamNode)
	 */
	@Override
	public PamNode addDefaultNode(Node n) {
		if (n == null) {
			return null;
		}
		return (PamNode) pamNodeStructure.addDefaultNode(n);
	}

	/*
	 * Adds set of PamLinks to this PAM.
	 * 
	 * @param links the links
	 * 
	 * @return the Sets
	 */
	@Override
	public Set<PamLink> addDefaultLinks(Set<? extends Link> links) {
		if (links == null) {
			return null;
		}
		Set<PamLink> copiedLinks = new HashSet<PamLink>();
		for (Link l : links) {
			copiedLinks.add(addDefaultLink(l));
		}
		return copiedLinks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultLink(
	 * edu.memphis.ccrg.lida.framework.shared.Link)
	 */
	@Override
	public PamLink addDefaultLink(Link link) {
		if (link == null) {
			return null;
		}
		return (PamLink) pamNodeStructure.addDefaultLink(link);
	}

	/*
	 * Adds a feature detector to this PAM.
	 * 
	 * @param detector the detector
	 */
	@Override
	public void addPerceptualAlgorithm(DetectionAlgorithm detector) {
		for (PamLinkable pl : detector.getPamLinkables()) {
			if (!pamNodeStructure.containsLinkable(pl)) {
				logger.log(Level.WARNING, "Adding detection algorithm {1} but, detector's pam linkable {2} is not in PAM.",
						new Object[]{ TaskManager.getCurrentTick(),detector,pl});
			}
		}

		taskSpawner.addTask(detector);
		logger.log(Level.FINE, "Added feature detector to PAM", TaskManager
				.getCurrentTick());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addPamListener(
	 * edu.memphis.ccrg.lida.pam.PamListener)
	 */
	@Override
	public void addPamListener(PamListener pl) {
		pamListeners.add(pl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.memphis.ccrg.lida.workspace.main.WorkspaceListener#
	 * receiveWorkspaceContent(edu.memphis.ccrg.lida.framework.ModuleName,
	 * edu.memphis.ccrg.lida.workspace.main.WorkspaceContent)
	 */
	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		NodeStructure ns = (NodeStructure) bc;
		taskSpawner.addTask(new ProcessBroadcastTask(ns.copy()));
	}

	private class ProcessBroadcastTask extends FrameworkTaskImpl {
		private NodeStructure broadcast;

		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}

		@Override
		protected void runThisFrameworkTask() {
			learn((BroadcastContent) broadcast);
			setTaskStatus(TaskStatus.FINISHED);
		}

		@Override
		public String toString() {
			return PerceptualAssociativeMemoryImpl.class.getSimpleName()
					+ "Broadcast";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.globalworkspace.BroadcastListener#receiveBroadcast
	 * (edu.memphis.ccrg.lida.globalworkspace.BroadcastContent)
	 */
	@Override
	public synchronized void receiveWorkspaceContent(
			ModuleName originatingBuffer, WorkspaceContent content) {
		// NodeStructure ns = (NodeStructure) content;
		// FrameworkTask t = new FooTask(ns.copy());
		// taskSpawner.addTask(t);
		// TODO Task
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.
	 * PreafferenceListener
	 * #receivePreafference(edu.memphis.ccrg.lida.framework.shared
	 * .NodeStructure, edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public synchronized void receivePreafference(NodeStructure addList,
			NodeStructure deleteList) {
		// TODO task to use preafferent signal
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.globalworkspace.BroadcastListener#learn(edu.memphis
	 * .ccrg.lida.globalworkspace.BroadcastContent)
	 */
	@Override
	public void learn(BroadcastContent bc) {
		NodeStructure ns = (NodeStructure) bc;
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
		pamNodeStructure.decayNodeStructure(ticks);
	}

	/*
	 * Receive activation from feature detectors or other codelets to excite a
	 * PamNode.
	 * 
	 * @param node the node
	 * 
	 * @param amount the amount
	 */
	@Override
	public void receiveActivationBurst(PamLinkable pl, double amount) {
		if (pl instanceof PamLink) {
			logger.log(Level.WARNING, "Does not support pam links yet",
					TaskManager.getCurrentTick());
			return;
		}

		PamNode linkable = (PamNode) pamNodeStructure.getNode(pl
				.getExtendedId());
		if (linkable != null) {
			logger.log(Level.FINEST, "{1} gets activation burst. Amount: {2}",
					new Object[]{TaskManager.getCurrentTick(),linkable,amount});
			ExcitationTask task = new ExcitationTask(linkable, amount,
					excitationTaskTicksPerRun, this, taskSpawner);
			taskSpawner.addTask(task);
		} else {
			logger.log(Level.WARNING, "Cannot find pamnode: {1}",
					new Object[]{TaskManager.getCurrentTick(),linkable});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#receiveActivationBurst
	 * (java.util.Set, double)
	 */
	@Override
	public void receiveActivationBurst(Set<PamLinkable> linkableIds,
			double amount) {
		for (PamLinkable linkable : linkableIds) {
			receiveActivationBurst(linkable, amount);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#
	 * propagateActivationToParents(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public void propagateActivationToParents(PamNode pn) {
		// Calculate the amount to propagate
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		propagateParams.put("upscale", upscaleFactor);
		propagateParams.put("totalActivation", pn.getTotalActivation());
		double amountToPropagate = propagationStrategy
				.getActivationToPropagate(propagateParams);

		// Get parents of pamNode and the connecting link
		Map<Linkable, Link> parentLinkMap = pamNodeStructure
				.getConnectedSinks(pn);
		for (Linkable parent : parentLinkMap.keySet()) {
			// Excite the connecting link and the parent
			if (parent instanceof PamNode) {
				propagateActivation(pn, (PamNode) parent,
						(PamLink) parentLinkMap.get(parent), amountToPropagate);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivation
	 * (edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamNode,
	 * edu.memphis.ccrg.lida.pam.PamLink, double)
	 */
	@Override
	public void propagateActivation(PamNode source, PamNode sink, PamLink link,
			double amount) {
		if(logger.isLoggable(Level.FINEST)){
			logger.log(Level.FINEST, "exciting parent: {1} and connecting link {2} amount: {3}",new Object[]{ TaskManager.getCurrentTick(),sink,link,amount});
		}
		PropagationTask task = new PropagationTask(source, link, sink, amount,
				this, taskSpawner);
		
		task.setTicksPerStep(propagationTaskTicksPerRun);
		taskSpawner.addTask(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#
	 * addNodeStructureToPercept
	 * (edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public void addNodeStructureToPercept(NodeStructure ns) {
		for (PamListener pl : pamListeners) {
			pl.receivePercept(ns);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu
	 * .memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public boolean containsNode(Node node) {
		return pamNodeStructure.containsNode(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu
	 * .memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsNode(ExtendedId id) {
		return pamNodeStructure.containsNode(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu
	 * .memphis.ccrg.lida.pam.PamLink)
	 */
	@Override
	public boolean containsLink(Link link) {
		return pamNodeStructure.containsLink(link);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu
	 * .memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLink(ExtendedId id) {
		return pamNodeStructure.containsLink(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNodes()
	 */
	@Override
	public Collection<Node> getNodes() {
		return pamNodeStructure.getNodes();
	}

	@Override
	public Collection<Link> getLinks() {
		return pamNodeStructure.getLinks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#getModuleContent(
	 * java. lang.Object[])
	 */
	@Override
	public Object getModuleContent(Object... params) {
		return new UnmodifiableNodeStructureImpl(
				(NodeStructureImpl) pamNodeStructure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModule#addListener(edu.memphis
	 * .ccrg .lida.framework.ModuleListener)
	 */
	@Override
	public void addListener(ModuleListener l) {
		if (l instanceof PamListener) {
			addPamListener((PamListener) l);
		} else {
			logger.log(Level.WARNING, "Cannot add listener type {1} to this module.", 
					new Object[]{TaskManager.getCurrentTick(),l});
		}
	}

	/**
	 * Returns the perceptThreshold
	 * 
	 * @return threshold for a {@link PamLinkable} to be instantiated into a
	 *         percept
	 */
	public static double getPerceptThreshold() {
		return perceptThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setPerceptThreshold
	 * (double)
	 */
	@Override
	public void setPerceptThreshold(double t) {
		if (t >= 0.0 && t <= 1.0) {
			PerceptualAssociativeMemoryImpl.perceptThreshold = t;
		} else {
			logger
					.log(
							Level.WARNING,
							"Percept threshold must in range [0.0, 1.0]. Threshold will not be modified.",
							TaskManager.getCurrentTick());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#isOverPerceptThreshold
	 * (edu.memphis.ccrg.lida.pam.PamLinkable)
	 */
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
		if (f < 0.0) {
			upscaleFactor = 0.0;
		} else if (f > 1.0) {
			upscaleFactor = 1.0;
		} else {
			upscaleFactor = f;
		}
	}

	@Override
	public double getDownscaleFactor() {
		return downscaleFactor;
	}

	@Override
	public void setDownscaleFactor(double f) {
		if (f < 0.0) {
			downscaleFactor = 0.0;
		} else if (f > 1.0) {
			downscaleFactor = 1.0;
		} else {
			downscaleFactor = f;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamLink(edu.
	 * memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Link getLink(ExtendedId eid) {
		return pamNodeStructure.getLink(eid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNode(edu.
	 * memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Node getNode(ExtendedId eid) {
		return pamNodeStructure.getNode(eid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNode(int)
	 */
	@Override
	public Node getNode(int id) {
		return pamNodeStructure.getNode(id);
	}

	@Override
	public Collection<LinkCategory> getLinkCategories() {
		return Collections.unmodifiableCollection(linkCategories.values());
	}

	@Override
	public LinkCategory getLinkCategory(int id) {
		return linkCategories.get(id);
	}

	@Override
	public LinkCategory addLinkCategory(LinkCategory cat) {
		if (cat instanceof PamNode) {
			cat = (LinkCategory) pamNodeStructure.addNode((Node) cat,
					DEFAULT_NONDECAYING_PAMNODE);
			linkCategories.put(cat.getId(), cat);
			return cat;
		}
		return null;
	}

	private LinkCategory addInternalLinkCategory(LinkCategory cat) {
		if (cat instanceof PamNode) {
			cat = (LinkCategory) pamNodeStructure.addNode((Node) cat, false);
			linkCategories.put(cat.getId(), cat);
			return cat;
		}
		return null;
	}

	/**
	 * Internal implementation of {@link NodeStructureImpl}
	 * Allows {@link Node} to be added without copying them.
	 */
	protected static class PamNodeStructure extends NodeStructureImpl {
		/**
		 * 
		 * @param nodeType Default node type
		 * @param linkType Default link type
		 */
		public PamNodeStructure(String nodeType, String linkType) {
			super(nodeType, linkType);
		}

		@Override
		public Node addNode(Node n, boolean copy) {
			return super.addNode(n, copy);
		}
	}
}