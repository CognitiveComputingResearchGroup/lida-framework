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
	/**
	 * 
	 */
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
	private TaskSpawner taskSpawner;
	//
	private PropagationBehavior propagationBehavior;
	
	/**
	 * Type of new nodes created by this PAM
	 */
	private String newNodeType = "PamNodeImpl";
	
	private NodeFactory nodeFactory = NodeFactory.getInstance();

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
	 * set the propagation behavior for this pam
	 */
	public void setPropagationBehavior(PropagationBehavior b){
		propagationBehavior = b;
	}

	public Set<PamNode> addNodes(Set<PamNode> nodes) {
		return pamNodeStructure.addPamNodes(nodes);
	}
	public void addLinks(Set<Link> links) {
		pamNodeStructure.addLinks(links);
	}

	/**
	 * Feature detectors should be added after the nodes they excite are added
	 */
	public void addFeatureDetector(FeatureDetector detector) {
		featureDetectors.add(detector);
		taskSpawner.addTask(detector);
		logger.log(Level.FINE, "Added feature detector to PAM", LidaTaskManager.getActualTick());	
	}//method

	// ******INTERMODULE COMMUNICATION******
	public void addPamListener(PamListener pl) {
		pamListeners.add(pl);
	}

	public synchronized void receiveWorkspaceContent(ModuleName originatingBuffer,
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

	public void decayModule(long ticks) {
		pamNodeStructure.decayNodes(ticks);
	}// method

	/**
	 * receives activation from feature detectors or other codelets to excite a
	 * PamNode
	 * 
	 * This method can be changed to store the burst and then excite all the
	 * nodes together.
	 */
	public void receiveActivationBurst(PamNode node, double amount) {
		logger.log(Level.FINE,node.getLabel() + " gets activation burst. Amount: " + amount + "- total activation: " +
						node.getTotalActivation(),LidaTaskManager.getActualTick());
		ExcitationTask task = new ExcitationTask(node, amount, this, taskSpawner);
		taskSpawner.addTask(task);	
	}
	public void receiveActivationBurst(Set<PamNode> nodes, double amount) {
		for(PamNode n: nodes)
			receiveActivationBurst(n, amount);
	}
	
	/**
	 * Propagates the activation of 'pamNode' to its parents based on PAM's 
	 * propagation behavior. 
	 * 
	 */
	public void sendActivationToParentsOf(PamNode pamNode) {
		Map<String, Object> propagateParams = new HashMap<String, Object>();
		Set<PamNode> nodes = pamNodeStructure.getParents(pamNode);
		propagateParams.put("upscale", pamNodeStructure.getUpscale());
		propagateParams.put("totalActivation", pamNode.getTotalActivation());
		double amount = propagationBehavior.getActivationToPropagate(propagateParams);
		this.receiveActivationBurst(nodes, amount);
	}

	public void addNodeToPercept(PamNode pamNode) {
		for (int i = 0; i < pamListeners.size(); i++){
			pamListeners.get(i).receiveNode(pamNode);
		//	System.out.println(pamListeners.get(i).toString());
		}
	}
	public void addLinkToPercept(Link l) {
		for (int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receiveLink(l);
	}
	public void addNodeStructureToPercept(NodeStructure ns) {
		for (int i = 0; i < pamListeners.size(); i++){
			pamListeners.get(i).receiveNodeStructure(ns);
			//System.out.println(pamListeners.get(i).toString());
		}
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
	public void setParameters(Map<String, ?> parameters) {
		Object o = parameters.get("pam.Upscale");
		if (o != null) {
			if(o instanceof String){
				double d = Double.parseDouble((String) o);
				synchronized (this) {
					pamNodeStructure.setUpscale(d);
				}
			}
			else if (o instanceof Double){ 
				synchronized (this) {
					pamNodeStructure.setUpscale((Double) o);
				}
			}
			else{
				logger.warning("Unable to set UPSCALE parameter, using the default in PamNodeStructure");
			}
		}
		o = parameters.get("pam.Downscale");
		if (o != null){
			if(o instanceof String){
				double d = Double.parseDouble((String) o);
				synchronized (this) {
					pamNodeStructure.setDownscale(d);
				}
			}
			else if (o instanceof Double){ 
				synchronized (this) {
					pamNodeStructure.setDownscale((Double) o);
				}
			}
			else{
				logger.warning("Unable to set DOWNSCALE parameter, using the default in PamNodeStructure");
			}
		}
				
		o = parameters.get("pam.Selectivity");
		if (o != null){
			if(o instanceof String){
				double d = Double.parseDouble((String) o);
				synchronized (this) {
					pamNodeStructure.setSelectivity(d);
				}
			}
			else if (o instanceof Double){ 
				synchronized (this) {
					pamNodeStructure.setSelectivity((Double) o);
				}
			}
			else{
				logger.warning("Unable to set Selectivity parameter, using the default in PamNodeStructure");
			} 
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
		return (PamNode)pamNodeStructure.getNode(id) ;
	}
	public PamNode addNode(PamNode node) {
		return (PamNode) pamNodeStructure.addNode(node);		
	}
	public void addLink(PamNode source, PamNode sink, LinkType type, double activation) {
		pamNodeStructure.addLink(source.getIds(),sink.getIds(),type,activation);		
	}

	public void addLink(String sourceId, String sinkId, LinkType type, double activation) {
		pamNodeStructure.addLink(sourceId,sinkId,type,activation);		
	}
	@Override
	public PamNode addNewNode(String label) {
		PamNode newNode =  (PamNode) nodeFactory.getNode(newNodeType, label);
		newNode = (PamNode) pamNodeStructure.addNode(newNode);
		return newNode;
	}
	@Override
	public String getNewNodeType() {
		return this.newNodeType;
	}
	@Override
	public void setNewNodeType(String type) {
		newNodeType = type;
	}
	@Override
	public void exciteAndConnect(PamNode sourceNode, PamNode sinkNode, double excitationAmount) {
		logger.log(Level.FINEST, "exciting and connecting " + sourceNode.getLabel() + " to " + sinkNode.getLabel(), LidaTaskManager.getActualTick());
		Link l = nodeFactory.getLink(sourceNode, sinkNode, LinkType.GROUNDING, 1.0);
		ExciteAndConnectTask task = new ExciteAndConnectTask(sourceNode, sinkNode, l, excitationAmount, this, taskSpawner);
		taskSpawner.addTask(task);	
	}

}//class