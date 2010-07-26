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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkType;
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
																	            WorkspaceListener{
	
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
	 * TODO: top-down influences 
	 */
	private NodeStructure topDownContent = new NodeStructureImpl();
	
	/**
	 * Current conscious broadcast
	 * TODO: implement learning
	 */
	private NodeStructure broadcastContent = new NodeStructureImpl();
	
	/**
	 * From action selection
	 * TODO: make use of this
	 */
    private NodeStructure preafferantSignal = new NodeStructureImpl();
	
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
	protected NodeFactory nodeFactory = NodeFactory.getInstance();
	
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
		// TODO:
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public synchronized void receivePreafferentSignal(NodeStructure ns) {
		preafferantSignal = ns;
		Collection<Node> nodes = preafferantSignal.getNodes();
		for (Node n : nodes) {n.getId();}
		// TODO: Use preafferent signal
	}

	public void learn() {
		//TODO: learning algorithm 
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {n.getId();}
	}//method

	/**
	 * Called by the task manager every tick
	 */
	public void decayModule(long ticks) {
		pamNodeStructure.decayNodes(ticks);
		//TODO: pamNodeStructure.decayLink(ticks);
	}// method

	/**
	 * Receive activation from feature detectors or other codelets to excite a
	 * PamNode
	 */
	public void receiveActivationBurst(PamNode node, double amount) {
		logger.log(Level.FINE,
				   node.getLabel() + " gets activation burst. Amount: " + amount + ", total activation: " + node.getTotalActivation(),
				   LidaTaskManager.getActualTick());
		ExcitationTask task = new ExcitationTask(node, amount, this, taskSpawner);
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
	public void sendActivationToParentsOf(PamNode pamNode) {
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
		taskSpawner.addTask(task);	
	}

	public void addNodeToPercept(PamNode pamNode) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNode(pamNode);
	}
	public void addLinkToPercept(PamLink l) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveLink(l);
	}
	public void addNodeStructureToPercept(NodeStructure ns) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveNodeStructure(ns);
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
	}// method

	public Collection<FeatureDetector> getFeatureDetectors(){
		return featureDetectors;
	}
	public PamNodeStructure getNodeStructure(){
		return pamNodeStructure;
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
	public PamNode getNode(long id) {
		return (PamNode) pamNodeStructure.getNode(id) ;
	}
	public PamNode addNode(PamNode node) {
		return (PamNode) pamNodeStructure.addNode(node);		
	}
	public Link addNewLink(PamNode source, PamNode sink, LinkType type, double activation) {
		return pamNodeStructure.addLink(source.getIds(),sink.getIds(),type,activation);		
	}

	public Link addNewLink(String sourceId, String sinkId, LinkType type, double activation) {
		return pamNodeStructure.addLink(sourceId,sinkId,type,activation);		
	}
	@Override
	public PamNode addNewNode(String label) {
		PamNode newNode =  (PamNode) nodeFactory.getNode(pamNodeStructure.getDefaultNodeType(), label);
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

}//class