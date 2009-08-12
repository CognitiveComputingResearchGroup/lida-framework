/*
 * @PerceptualAssociativeMemory.java  2.0  2/2/09
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class PerceptualAssociativeMemoryImpl implements	PerceptualAssociativeMemory, 
														GuiContentProvider, 
														BroadcastListener, 
														WorkspaceListener{

	private PamNodeStructure graph = new PamNodeStructure();
	private List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
	private NodeStructureImpl percept = new NodeStructureImpl();
	private List<PamListener> pamListeners = new ArrayList<PamListener>();
	// Shared variables
	// private NodeStructure topDownContent = new NodeStructureImpl();
	private NodeStructure broadcastContent = new NodeStructureImpl();
	// private NodeStructure preafferantSignal = new NodeStructureImpl();
	// for GUI
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<Object> guiContent = new ArrayList<Object>();

	/**
     * 
     */
	public void setParameters(Map<String, Object> parameters) {
		Object o = parameters.get("upscale");
		if ((o != null) && (o instanceof Double)) {
			synchronized (this) {
				graph.setUpscale((Double) o);
			}
		}
		o = parameters.get("downscale");
		if ((o != null) && (o instanceof Double))
			synchronized (this) {
				graph.setDownscale((Double) o);
			}
		o = parameters.get("selectivity");
		if ((o != null) && (o instanceof Double)) {
			synchronized (this) {
				graph.setSelectivity((Double) o);
			}
		}
	}// method

	public void addToPAM(Set<PamNode> nodes, List<FeatureDetector> ftDetectors,
						 Set<Link> links) {
		featureDetectors = ftDetectors;
		graph.addPamNodes(nodes);
		graph.addLinks(links);

		for (FeatureDetector fd : featureDetectors) {
			long id = fd.getPamNode().getId();
			fd.setNode((PamNode) graph.getNode(id));
		}
	}// method

	// ******INTERMODULE COMMUNICATION******
	public void addPAMListener(PamListener pl) {
		pamListeners.add(pl);
	}

	public synchronized void receiveWorkspaceContent(Module originatingBuffer,
													 NodeStructure content) {
		// topDownContent = content;
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public synchronized void receivePreafferentSignal(NodeStructure ns) {
		// preafferantSignal = ns;
	}

	// ******FUNDAMENTAL PAM FUNCTIONS******
	public void detectSensoryMemoryContent() {
		for (FeatureDetector d : featureDetectors)
			d.executeDetection();
	}// method

	/**
	 * receives activation from feature detectors or other codelets to excite a
	 * PamNode
	 * 
	 * This method can be changed to store the burst and then excite all the
	 * nodes together.
	 */
	public void receiveActivationBurst(PamNode node, double activation) {
		node.excite(activation);
	}

	/**
	 * Pass activation upwards based on the order found in the layerMap
	 */
	public void propogateActivation() {
		Set<PamNode> bottomNodes = new HashSet<PamNode>();
		
		for (FeatureDetector fd : featureDetectors)
			bottomNodes.add(fd.getPamNode());

		graph.passActivationUpward(bottomNodes);
		updatePercept();
		// TODO:impl episodic buffer activation into activation passing
		// TODO:use preafferent signal
	}// method

	/**
	 * Clear the percept's nodes. Go through graph's nodes and add those above
	 * threshold to the percept.
	 * 
	 * TODO: If links aren't Node then this method needs to be expanded to
	 * include links.
	 */
	private void updatePercept() {
		percept.clearNodes();
		//System.out.println(graph.getNodeCount());
		for (Node n : graph.getNodes()) {
			PamNodeImpl node = (PamNodeImpl) n;
			if (node.isRelevant())// Based on totalActivation
				percept.addNode(node);
		}// for
		
		// This is a good place to update guiContent
		guiContent.clear();
		guiContent.add(percept.getNodeCount());
		guiContent.add(percept.getLinkCount());
	}// method

	public void sendOutPercept() {
		NodeStructure copy = new NodeStructureImpl(percept);
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receivePamContent(copy);
	}// method

	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			// TODO:
			n.getId();
		}
	}

	public void decayPam() {
		graph.decayNodes();
	}// method

	public void setDecayBehavior(DecayBehavior b) {
		graph.setNodesDecayBehavior(b);
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		graph.setNodesExciteBehavior(behavior);
	}// method

	/**
	 * returns a PamNode from the PAM
	 */
	public PamNode getPamNode(long id) {
		return (PamNode) graph.getNode(id);
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					Module.perceptualAssociativeMemory, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}
	}//method

}// class PAM.java