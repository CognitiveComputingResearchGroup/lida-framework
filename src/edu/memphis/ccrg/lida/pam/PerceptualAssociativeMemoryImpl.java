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

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.tasks.ExcitationTask;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class PerceptualAssociativeMemoryImpl extends LidaModuleImpl implements	PerceptualAssociativeMemory,  
																	            BroadcastListener, 
																	            WorkspaceListener, 
																	            PreafferenceListener{
	
	private static Logger logger = Logger.getLogger(PerceptualAssociativeMemoryImpl.class.getCanonicalName());
	
	/**
	 * Contains all of the Node, Links and their connections.
	 */
	private PamNodeStructureImpl pamNodeStructure = new PamNodeStructureImpl();
	
	/**
	 * All of the running featureDetectors should be in this list. 
	 */
	private List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
	
	/**
	 * Modules listening for the current Percept
	 */
	private List<PamListener> pamListeners = new ArrayList<PamListener>();

	/**
	 * Workspace content
	 */
	private NodeStructure topDownContent = new NodeStructureImpl();
	
	/**
	 * Current conscious broadcast
	 */
	private NodeStructure broadcastContent = new NodeStructureImpl();

	/**
	 * How PAM calculates the amount of activation to propagate
	 */
	private PropagationBehavior propagationBehavior;

	/**
	 * To create new node and links
	 */
	protected NodeFactory factory = NodeFactory.getInstance();

	/**
	 * 
	 */
	private int excitationTaskTicksPerRun = 1;
	
	/**
	 * 
	 */
	private int propagationTaskTicksPerRun = 1;
	
	private static final double DEFAULT_PERCEPT_THRESHOLD = 0.7; 
	private static double perceptThreshold;
	
	private static final double DEFAULT_UPSCALE_FACTOR = 0.9;
	private static double upscaleFactor;
	
	private static final double DEFAULT_DOWNSCALE_FACTOR = 0.5;
	private static double downscaleFactor;
	
	public PerceptualAssociativeMemoryImpl(){
		super(ModuleName.PerceptualAssociativeMemory);
	}

	/**
	 * Set the propagation behavior for this PAM
	 */
	@Override
	public void setPropagationBehavior(PropagationBehavior b){
		propagationBehavior = b;
	}
	
	/**
	 * Get the propagation behavior for this PAM
	 */
	public PropagationBehavior getPropagationBehavior(){
		return propagationBehavior;
	}

	/**
	 * Adds set of PamNodes to this PAM
	 */
	@Override
	public Set<PamNode> addNodes(Set<PamNode> nodes) {
		Set<PamNode> returnedLinks = new HashSet<PamNode>();
		for(PamNode l: nodes)
			returnedLinks.add((PamNode) pamNodeStructure.addNode(l));
		return returnedLinks;
	}
	/**
	 * Adds set of PamLinks to this PAM
	 */
	@Override
	public Set<PamLink> addLinks(Set<PamLink> links) {
		Set<PamLink> returnedLinks = new HashSet<PamLink>();
		for(PamLink l: links)
			returnedLinks.add((PamLink) pamNodeStructure.addLink(l));
		return returnedLinks;
	}

	/**
	 * Adds a feature detector to this PAM
	 */
	@Override
	public void addFeatureDetector(FeatureDetector detector) {
		featureDetectors.add(detector);
		taskSpawner.addTask(detector);
		logger.log(Level.FINE, "Added feature detector to PAM", LidaTaskManager.getActualTick());	
	}

	// ******INTERMODULE COMMUNICATION******
	@Override
	public void addPamListener(PamListener pl) {
		pamListeners.add(pl);
	}

	@Override
	public synchronized void receiveWorkspaceContent(ModuleName originatingBuffer,
													 WorkspaceContent content) {
		topDownContent = content;
		Collection<Node> nodes = topDownContent.getNodes();
		for (Node n : nodes) {n.getId();}
		//  handle workspace content
	}

	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = ((NodeStructure) bc).copy();
	}

	@Override
	public synchronized void receivePreafference(Collection<Node> addList, Collection<Node> deleteList) {
		// Use preafferent signal
	}

	@Override
	public void learn() {
		// learning algorithm 
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {n.getId();}
	}//method

	/**
	 * Called by the task manager every tick
	 */
	@Override
	public void decayModule(long ticks) {
		pamNodeStructure.decayLinkables(ticks);
	}// method

	/**
	 * Receive activation from feature detectors or other codelets to excite a
	 * PamNode
	 */
	@Override
	public void receiveActivationBurst(PamNode node, double amount) {
		logger.log(Level.FINE,
				   node.getLabel() + " gets activation burst. Amount: " + amount + ", total activation: " + node.getTotalActivation(),
				   LidaTaskManager.getActualTick());
		ExcitationTask task = new ExcitationTask(node, amount, excitationTaskTicksPerRun, this, taskSpawner);
		taskSpawner.addTask(task);	
	}

	@Override
	public void receiveActivationBurst(Set<PamNode> nodes, double amount) {
		for(PamNode n: nodes)
			receiveActivationBurst(n, amount);
	}
	
	/**
	 * 
	 * Called by Excitation tasks after their node has been excited.
	 * Based on PAM's propagation behavior, propagate activation from 'pamNode' 
	 * to its parents and to the connecting links.
	 * 
	 */
	@Override
	public void sendActivationToParents(PamNode pamNode) {
		//Calculate the amount to propagate
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		propagateParams.put("upscale", upscaleFactor);
		propagateParams.put("totalActivation", pamNode.getTotalActivation());
		double amountToPropagate = propagationBehavior.getActivationToPropagate(propagateParams);
		
		//Get parents of pamNode and the connecting link
		Map<PamNode, PamLink> parentsAndConnectingLink = pamNodeStructure.getParentsAndConnectingLinksOf(pamNode);
		for(PamNode parent: parentsAndConnectingLink.keySet()){
			PamLink connectingLink = parentsAndConnectingLink.get(parent);
			//Excite the connecting link and the parent
			propagateActivation(pamNode, connectingLink, parent, amountToPropagate);
		}
	}//method
	
	/**
	 * Propagates activation 'amount', which originates from 'source', to node 'sink'.  
	 * Also give activation to 'link' which connects 'source' to 'sink'.
	 */
	@Override
	public void propagateActivation(PamNode source, PamLink link, PamNode sink, double amount){
		logger.log(Level.FINE, "exciting parent: " + sink.getLabel() + " and connecting link " + link.getLabel() + " amount: " + amount, LidaTaskManager.getActualTick());
		PropagationTask task = new PropagationTask(source, link, sink, amount, this, taskSpawner);
		task.setNumberOfTicksPerRun(propagationTaskTicksPerRun);
		taskSpawner.addTask(task);	
	}

	@Override
	public void addNodeToPercept(PamNode pamNode) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNode(pamNode);
	}
	@Override
	public void addLinkToPercept(PamLink pamLink) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveLink(pamLink);
	}
	@Override
	public void addNodeStructureToPercept(NodeStructure nodeStructure) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNodeStructure(nodeStructure);
	}
	
	@Override
	public void setDecayStrategy(DecayStrategy b) {
		pamNodeStructure.setNodesDecayStrategy(b);
	}
	@Override
	public void setExciteStrategy(ExciteStrategy behavior) {
		pamNodeStructure.setNodesExciteStrategy(behavior);
	}// method
	
	@Override
	public void init (){
		upscaleFactor = (Double) getParam("pam.Upscale", DEFAULT_UPSCALE_FACTOR);
		downscaleFactor = (Double) getParam("pam.Downscale", DEFAULT_DOWNSCALE_FACTOR);
		perceptThreshold = (Double) getParam("pam.Selectivity", DEFAULT_PERCEPT_THRESHOLD);				
		setNewNodeType((String) getParam("pam.newNodeType", "PamNodeImpl"));
		setNewLinkType((String) getParam("pam.newLinkType", "PamLinkImpl"));
		excitationTaskTicksPerRun = (Integer) getParam("pam.excitationTicksPerRun", 1);
		propagationTaskTicksPerRun = (Integer) getParam("pam.propagationTicksPerRun", 1);
	}// method
	
	@Override
	public boolean containsNode(PamNode node){
		return pamNodeStructure.containsNode(node);
	}
	
	@Override
	public boolean containsLink(PamLink link){
		return pamNodeStructure.containsLink(link);
	}

	@Override
	public Collection<FeatureDetector> getFeatureDetectors(){
		return featureDetectors;
	}
	
	@Override
	public Collection<Node> getNodes(){
		Collection<Node> pamNodes = pamNodeStructure.getNodes();
		Collection<Node> nodes = new ArrayList<Node>();
		for(Node pamNode: pamNodes)
			nodes.add(factory.getNode(pamNode));
		return Collections.unmodifiableCollection(nodes);
	}

	@Override
	public Object getModuleContent(Object... params) {
		return pamNodeStructure;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof PamListener){
			addPamListener((PamListener)listener);
		}
	}
	@Override
	public Node getNode(int id) {
		return factory.getNode(pamNodeStructure.getNode(id));
	}
	@Override
	public PamNode addNode(PamNode node) {
		return (PamNode) pamNodeStructure.addNode(node);		
	}
	@Override
	public Link addNewLink(PamNode source, PamNode sink, LinkCategory type, double activation) {
		return pamNodeStructure.addLink(source.getExtendedId(), sink.getExtendedId(), type, activation);		
	}

	@Override
	public Link addNewLink(ExtendedId sourceId, ExtendedId sinkId, LinkCategory type, double activation) {
		return pamNodeStructure.addLink(sourceId, sinkId, type, activation);		
	}
	@Override
	public PamNode addNewNode(String label) {
		PamNode newNode =  (PamNode) factory.getNode(pamNodeStructure.getDefaultNodeType(), label);
		newNode = (PamNode) pamNodeStructure.addNode(newNode);
		return newNode;
	}
	
	@Override
	public PamNode addNewNode(String pamNodeType, String label){
		PamNode newNode =  (PamNode) factory.getNode(pamNodeType, label);
		if(newNode == null){
			return null;
		}else{
			newNode = (PamNode) pamNodeStructure.addNode(newNode);
			return newNode;
		}
	}
	
	@Override
	public void setNewNodeType(String type) {
		pamNodeStructure.setDefaultNode(type);
	}
	
	@Override
	public void setNewLinkType(String type) {
		pamNodeStructure.setDefaultLink(type);
	}
	
	public static double getPerceptThreshold(){
		return perceptThreshold;
	}
	
	public static double getUpscaleFactor(){
		return upscaleFactor;
	}
	
	public static double getDownscaleFactory(){
		return downscaleFactor;
	}
	
	@Override
	public boolean isOverPerceptThreshold(PamLinkable l){
		return l.getTotalActivation() > perceptThreshold;
	}

	@Override
	public Object getState() {
        return pamNodeStructure;
    }
    @Override
	public boolean setState(Object content) {
        if (content instanceof PamNodeStructureImpl) {
            pamNodeStructure = (PamNodeStructureImpl)content;
            return true;
        }
        return false;
    }
}//class