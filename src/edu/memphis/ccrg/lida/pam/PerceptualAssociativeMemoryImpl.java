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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;

public class PerceptualAssociativeMemoryImpl extends LidaModuleImpl implements	PerceptualAssociativeMemory,  
																	            BroadcastListener, 
																	            WorkspaceListener, 
																	            PreafferenceListener{
	
	private static Logger logger = Logger.getLogger("lida.pam.PerceptualAssociativeMemory");
	
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
     * Used to spawn the various tasks for PAM
     */
	private TaskSpawner taskSpawner;
	
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
	
	public PerceptualAssociativeMemoryImpl(){
		super(ModuleName.PerceptualAssociativeMemory);
	}
	/**
	 * @param taskSpawner the taskSpawner to set
	 */
	public void setTaskSpawner(TaskSpawner taskSpawner) {
		this.taskSpawner = taskSpawner;
	}
	
	/**
	 * Get this PAM's task spawner
	 */
	public TaskSpawner getTaskSpawner() {
		return taskSpawner;
	}

	/**
	 * Set the propagation behavior for this PAM
	 */
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
	public Set<PamNode> addNodes(Set<PamNode> nodes) {
		return pamNodeStructure.addPamNodes(nodes);
	}
	/**
	 * Adds set of PamLinks to this PAM
	 */
	public Set<PamLink> addLinks(Set<PamLink> links) {
		return pamNodeStructure.addPamLinks(links);
	}

	/**
	 * Adds a feature detector to this PAM
	 */
	public void addFeatureDetector(FeatureDetector detector) {
		featureDetectors.add(detector);
		taskSpawner.addTask(detector);
		logger.log(Level.FINE, "Added feature detector to PAM", LidaTaskManager.getActualTick());	
	}

	// ******INTERMODULE COMMUNICATION******
	public void addPamListener(PamListener pl) {
		pamListeners.add(pl);
	}

	public synchronized void receiveWorkspaceContent(ModuleName originatingBuffer,
													 WorkspaceContent content) {
		topDownContent = content;
		Collection<Node> nodes = topDownContent.getNodes();
		for (Node n : nodes) {n.getId();}
		//  handle workspace content
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public synchronized void receivePreafference(Behavior b) {
		// TODO: Use preafferent signal
	}

	public void learn() {
		// learning algorithm 
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {n.getId();}
	}//method

	/**
	 * Called by the task manager every tick
	 */
	public void decayModule(long ticks) {
		pamNodeStructure.decayLinkables(ticks);
	}// method

	/**
	 * Receive activation from feature detectors or other codelets to excite a
	 * PamNode
	 */
	public void receiveActivationBurst(PamNode node, double amount) {
		logger.log(Level.FINE,
				   node.getLabel() + " gets activation burst. Amount: " + amount + ", total activation: " + node.getTotalActivation(),
				   LidaTaskManager.getActualTick());
		ExcitationTask task = new ExcitationTask(node, amount, excitationTaskTicksPerRun, this, taskSpawner);
		taskSpawner.addTask(task);	
	}

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
	public void sendActivationToParents(PamNode pamNode) {
		//Calculate the amount to propagate
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		propagateParams.put("upscale", pamNodeStructure.getUpscale());
		propagateParams.put("totalActivation", pamNode.getTotalActivation());
		double amountToPropagate = propagationBehavior.getActivationToPropagate(propagateParams);
		
		//Get parents of pamNode and the connecting link
		Map<PamNode, PamLink> parentsAndConnectingLink = pamNodeStructure.getParentsAndConnectingLinks(pamNode);
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
	public void propagateActivation(PamNode source, PamLink link, PamNode sink, double amount){
		logger.log(Level.FINE, "exciting parent: " + sink.getLabel() + " and connecting link " + link.getLabel() + " amount: " + amount, LidaTaskManager.getActualTick());
		PropagationTask task = new PropagationTask(source, link, sink, amount, this, taskSpawner);
		task.setNumberOfTicksPerRun(propagationTaskTicksPerRun);
		taskSpawner.addTask(task);	
	}

	public void addNodeToPercept(PamNode pamNode) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNode(pamNode);
	}
	public void addLinkToPercept(PamLink pamLink) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveLink(pamLink);
	}
	public void addNodeStructureToPercept(NodeStructure nodeStructure) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNodeStructure(nodeStructure);
	}
	
	public void setDecayStrategy(DecayStrategy b) {
		pamNodeStructure.setNodesDecayStrategy(b);
	}
	public void setExciteStrategy(ExciteStrategy behavior) {
		pamNodeStructure.setNodesExciteStrategy(behavior);
	}// method
	
	/**
     * 
     */
	public void init (Map<String,?> parameters){
		Object o = parameters.get("pam.Upscale");
		if (o != null && o instanceof Double){ 
				synchronized (this) {
					pamNodeStructure.setUpscale((Double) o);
				}
		}else{
			logger.warning("Unable to set UPSCALE parameter, using the default in PamNodeStructure");
		}
		
		o = parameters.get("pam.Downscale");
		if (o != null && o instanceof Double){ 
			synchronized (this) {
				pamNodeStructure.setDownscale((Double) o);
			}
		}else{
			logger.warning("Unable to set DOWNSCALE parameter, using the default in PamNodeStructure");
		}
				
		o = parameters.get("pam.Selectivity");
		if (o != null && o instanceof Double){ 
			synchronized (this) {
				pamNodeStructure.setSelectivity((Double) o);
			}
		}else{
			logger.warning("Unable to set Selectivity parameter, using the default in PamNodeStructure");
		} 
		
		o = parameters.get("pam.newNodeType");
		if(o != null && o instanceof String){
			this.setNewNodeType((String) o);
		}else{
			logger.warning("Unable to set new Node type, using the default in PamNodeStructure");
		}
		
		o = parameters.get("pam.newLinkType");
		if(o != null && o instanceof String){
			this.setNewLinkType((String) o);
		}else{
			logger.warning("Unable to set new Link type, using the default in PamNodeStructure");
		}
		
		o = parameters.get("pam.excitationTicksPerRun");
		if(o != null)
			excitationTaskTicksPerRun = (Integer) o;
		
		o = parameters.get("pam.propagationTicksPerRun");
		if(o != null)
			propagationTaskTicksPerRun = (Integer) o;
	}// method
	
	public boolean containsNode(PamNode node){
		return pamNodeStructure.containsNode(node);
	}
	
	public boolean containsLink(PamLink link){
		return pamNodeStructure.containsLink(link);
	}

	public Collection<FeatureDetector> getFeatureDetectors(){
		return featureDetectors;
	}
	
	public Collection<Node> getNodes(){
		Collection<Node> pamNodes = pamNodeStructure.getNodes();
		Collection<Node> nodes = new ArrayList<Node>();
		for(Node pamNode: pamNodes)
			nodes.add(factory.getNode(pamNode));
		return Collections.unmodifiableCollection(nodes);
	}

	public ModuleName getModuleName() {
		return ModuleName.PerceptualAssociativeMemory;
	}

	public Object getModuleContent() {
		return pamNodeStructure;
	}
	public void addListener(ModuleListener listener) {
		if (listener instanceof PamListener){
			addPamListener((PamListener)listener);
		}
	}
	public Node getNode(long id) {
		return factory.getNode(pamNodeStructure.getNode(id));
	}
	public PamNode addNode(PamNode node) {
		return (PamNode) pamNodeStructure.addNode(node);		
	}
	public Link addNewLink(PamNode source, PamNode sink, LinkCategory type, double activation) {
		return pamNodeStructure.addLink(source.getIds(), sink.getIds(), type, activation);		
	}

	public Link addNewLink(String sourceId, String sinkId, LinkCategory type, double activation) {
		return pamNodeStructure.addLink(sourceId, sinkId, type, activation);		
	}
	@Override
	public PamNode addNewNode(String label) {
		PamNode newNode =  (PamNode) factory.getNode(pamNodeStructure.getDefaultNodeType(), label);
		newNode = (PamNode) pamNodeStructure.addNode(newNode);
		return newNode;
	}
	@Override
	public void setNewNodeType(String type) {
		pamNodeStructure.setDefaultNode(type);
	}
	
	@Override
	public void setNewLinkType(String type) {
		pamNodeStructure.setDefaultLink(type);
	}

        public Object getState() {
            return pamNodeStructure;
        }
        public boolean setState(Object content) {
            if (content instanceof PamNodeStructure) {
                pamNodeStructure = (PamNodeStructure)content;
                return true;
            }
            return false;
        }
}//class