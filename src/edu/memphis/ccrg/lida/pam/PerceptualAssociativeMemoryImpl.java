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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.TaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.framework.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class PerceptualAssociativeMemoryImpl implements	PerceptualAssociativeMemory,  
														BroadcastListener, 
														WorkspaceListener{

	private Logger logger = Logger.getLogger("lida.pam.PerceptualAssociativeMemory");
	
	/**
	 * Contains all of the Node, Links and their connections.
	 */
	private PamNodeStructure pamNodeStructure = new PamNodeStructure();
	
	/**
	 * All of the running featureDetectors should be in this list. 
	 * TODO: Separate featureDetectors from PAM?
	 */
	private List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
	
	/**
	 * Things that are listening for the percept
	 */
	private List<PamListener> pamListeners = new ArrayList<PamListener>();

	/**
	 * 
	 */
	private NodeStructure topDownContent = new NodeStructureImpl();
	private NodeStructure broadcastContent = new NodeStructureImpl();
    private NodeStructure preafferantSignal = new NodeStructureImpl();
	//
	private LidaTaskManager taskManager;
	private TaskSpawner taskSpawner;
	//
	private PropagationBehavior propagationBehavior;
	
	private Properties lidaProperties;
	
	/**
	 * @param taskSpawner the taskSpawner to set
	 */
	public void setTaskSpawner(TaskSpawner taskSpawner) {
		this.taskSpawner = taskSpawner;
	}

	public void setTaskManager(LidaTaskManager tm) {
		taskManager = tm;
	}
	
	public void setPropagationBehavior(PropagationBehavior b){
		propagationBehavior = b;
	}

	public void addNodes(Set<PamNode> nodes) {
		pamNodeStructure.addPamNodes(nodes);
	}
	public void addLinks(Set<Link> links) {
		pamNodeStructure.addLinks(links);
	}

	/**
	 * 
	 */
	public boolean addFeatureDetector(FeatureDetector detector) {
//		Since nodes are copied when added to a NodeStructure, 
//		the node object that the featureDetectors excite must be 
//		updated with the copied node that is in the pamNodeStructure
		long id = detector.getPamNode().getId();
		PamNode node = (PamNode) pamNodeStructure.getNode(id);
		if(node != null){
			detector.setPamNode(node);
			featureDetectors.add(detector);
			//taskManager.addTask(detector);
			return true;
		}else
			logger.log(Level.SEVERE, "Failed to addFeatureDetector. Node " + 
									 detector.getPamNode().getLabel() + 
									 " was not found in pam");
		return false;
	}//method

	// ******INTERMODULE COMMUNICATION******
	public void addPamListener(PamListener pl) {
		pamListeners.add(pl);
	}

	public synchronized void receiveWorkspaceContent(ModuleType originatingBuffer,
													 WorkspaceContent content) {
		// TODO:impl episodic buffer activation into activation passing
		topDownContent = content;
		Collection<Node> nodes = topDownContent.getNodes();
		for (Node n : nodes) {n.getId();}
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public synchronized void receivePreafferentSignal(NodeStructure ns) {
		// TODO:use preafferent signal
		preafferantSignal = ns;
		Collection<Node> nodes = preafferantSignal.getNodes();
		for (Node n : nodes) {n.getId();}
	}

	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {n.getId();}
	}//method

	public void decayPam() {
		pamNodeStructure.decayNodes();
	}// method

	/**
	 * receives activation from feature detectors or other codelets to excite a
	 * PamNode
	 * 
	 * This method can be changed to store the burst and then excite all the
	 * nodes together.
	 */
	public void receiveActivationBurst(PamNode node, double amount) {
		logger.fine(node.getLabel() + " amount " + amount + " tot activ " + node.getTotalActivation());
		ExcitationTask task = new ExcitationTask(node, amount, this, taskSpawner, taskManager);
		taskSpawner.addTask(task);	
	}
	public void receiveActivationBurst(Set<PamNode> nodes, double amount) {
		for(PamNode n: nodes)
			receiveActivationBurst(n, amount);
	}
	
	public void sendActivationToParentsOf(PamNode pamNode) {
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		Set<PamNode> nodes = pamNodeStructure.getParents(pamNode);
		propagateParams.put("upscale", pamNodeStructure.getUpscale());
		propagateParams.put("totalActivation", pamNode.getTotalActivation());//TODO: Abstract out
		double amount = propagationBehavior.getActivationToPropagate(propagateParams);
		this.receiveActivationBurst(nodes, amount);
	}

	public void addNodeToPercept(PamNode pamNode) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNode(pamNode);
	}
	public void addLinkToPercept(Link l) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveLink(l);
	}
	public void addNodeStructureToPercept(NodeStructure ns) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNodeStructure(ns);
	}
	
	public void setDecayBehavior(DecayBehavior b) {
		pamNodeStructure.setNodesDecayBehavior(b);
	}
	public void setExciteBehavior(ExciteBehavior behavior) {
		pamNodeStructure.setNodesExciteBehavior(behavior);
	}// method
	
	/**
     * 
     */
	public void setParameters(Map<String, Object> parameters) {
		Object o = parameters.get("upscale");
		if ((o != null) && (o instanceof Double)) {
			synchronized (this) {
				pamNodeStructure.setUpscale((Double) o);
			}
		}
		o = parameters.get("downscale");
		if ((o != null) && (o instanceof Double))
			synchronized (this) {
				pamNodeStructure.setDownscale((Double) o);
			}
		o = parameters.get("selectivity");
		if ((o != null) && (o instanceof Double)) {
			synchronized (this) {
				pamNodeStructure.setSelectivity((Double) o);
			}
		}
	}// method

	public Collection<FeatureDetector> getFeatureDetectors(){
		return featureDetectors;
	}
	public PamNodeStructure getNodeStructure(){
		return pamNodeStructure;
	}

	public ModuleType getModuleType() {
		return ModuleType.PerceptualAssociativeMemory;
	}

	public void init(Properties lidaProperties) {
		this.lidaProperties=lidaProperties;
		
	}


}//class