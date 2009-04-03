/*
 * @PerceptualAssociativeMemory.java  2.0  2/2/09
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida._perception;

import java.util.Set; 
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import edu.memphis.ccrg.lida._perception.interfaces.FeatureDetector;
import edu.memphis.ccrg.lida._perception.interfaces.PAMListener;
import edu.memphis.ccrg.lida._perception.interfaces.PAMContent;
import edu.memphis.ccrg.lida._perception.interfaces.PamNode;
import edu.memphis.ccrg.lida._perception.interfaces.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida._sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class PerceptualAssociativeMemoryImpl implements PerceptualAssociativeMemory{
	
    /**
     * proportion of activation spread to parents
     */
    private double upscale = 0.5;     
    /**
     * proportion of activation spread to children
     */
    private double downscale = 0.5;
    /**
     * selectivity for thresholding, % of max activation 
     */
    private double selectivity = 0.9;
    /**
     * Nodes that receive activation from SM. Key is the node's label.
     */
	public Set<FeatureDetector> featureDetectors;
	private GraphImpl graph;
	
	private DecayBehavior decayBehavior = new LinearDecayCurve();
    
    //For Intermodule communication
    private List<PAMListener> pamListeners;    
    private SensoryContentImpl sensoryContent;//Shared variable
    private PAMContent pamContent;//Not a shared variable
    private BroadcastContent broadcastContent;//Shared variables	
	private WorkspaceContent workspaceContent;
      
    public PerceptualAssociativeMemoryImpl(){
    	featureDetectors = new HashSet<FeatureDetector>();
    	graph = new GraphImpl(upscale, selectivity);
    	
    	pamListeners = new ArrayList<PAMListener>();
    	sensoryContent = new SensoryContentImpl();
    	pamContent = new PAMContentImpl();
    	//broadcastContent = new BroadcastContent();//TODO: write this class 	
    }
    
    //SETTING UP PAM    
    public void setParameters(Map<String, Object> parameters){    	
		Object o = parameters.get("upscale");
		if((o != null)&& (o instanceof Double)) {
			synchronized(this){
				upscale = (Double)o;
			}
		}
		//TODO: Graph has its own parameters?
		o = parameters.get("downscale");
		if((o != null)&& (o instanceof Double)) 
			synchronized(this){
				downscale = (Double)o;
			}		
		o = parameters.get("selectivity");
		if ((o != null)&& (o instanceof Double)){
			synchronized(this){
				selectivity = (Double)o;   	
				graph.setSelectivity((Double)o);
			}
		}
    }//public void setParameters(Map<String, Object> parameters)
    
    public void addToPAM(Set<Node> nodesToAdd, Set<FeatureDetector> featureDetectors, Set<Link> linkSet){
    	this.featureDetectors = featureDetectors;
    	graph.addNodes(nodesToAdd);
    	graph.addLinkSet(linkSet);    	
    	
    	//graph.printLinkMap();
    }//public void addToPAM(Set<Node> nodes, Set<Link> links)   
       
    
    //INTERMODULE COMMUNICATION
    
    public void addPAMListener(PAMListener pl){
		pamListeners.add(pl);
	}
    
    public synchronized void receiveSense(SensoryContentImpl sc){//SensoryContent    	
    	sensoryContent = sc;    	
    }

	public synchronized void receiveWorkspaceContent(WorkspaceContent content) {
		workspaceContent = content;		
	}
    	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = bc;		
	}
    
    //public synchronized void receivePreafferentSignal(PreafferentContent pc){
    	//TODO: eventually implement
    //}
	
	//FUNDAMENTAL PAM FUNCTIONS        
    public void sense(){    	
    	SensoryContentImpl sc = null;
    	synchronized(this){
    		sc = (SensoryContentImpl)sensoryContent.getThis();
    	}
  
    	for(FeatureDetector d: featureDetectors)
    		d.detect(sc);  	     
    }//public void sense()
        
    /**
     * 
     */
    public void passActivation(){    	
    	Map<Integer, Set<Node>> layerMap = graph.createLayerMap();
    	int layers = layerMap.keySet().size();
      	for(int i = 0; i < layers - 1; i++){
      		Set<Node> layerLinkables = layerMap.get(i);
      		for(Node n: layerLinkables){
      			double currentActivation = n.getCurrentActivation();
      	
      			Set<Node> parents = graph.getParents(n);
      			for(Node parent: parents){      	
      				double energy = currentActivation * upscale;      				
      				if(parent instanceof Node)
      					((Node)parent).excite(energy);
      			}//for each parent
      		}//for each linkable in a given layer
      	}//for each layer
    	
    	syncNodeActivation();   
    	//TODO:this is where the episodic buffer activation may come into play    	
    }//public void passActivation
    
    /**
     * Synchronizes this PAM by updating the percept and percept history. First
     * the percept is cleared, and then all relevant nodes are put in a new
     * percept. Last, the new percept is added to the percept history.
     * @see PamNodeImpl#synchronize()
     */
    private void syncNodeActivation(){
        GraphImpl newGraph = new GraphImpl(upscale, selectivity);
        Set<Node> nodes = graph.getNodes();
        for(Node node: nodes){
            node.synchronize();//Needed since excite changes current but not totalActivation.
            if(node.isRelevant()){//Based on totalActivation
            	System.out.println("a nodes is relevant adding to graph: " + node.getLabel());
                /*System.out.println("was able to add it " +*/
            	newGraph.addNode(node);
            	
            	//);
            }
        }//for      

        //TODO: this isn't a complete graph copy. want to get the links passed on for now. 
        newGraph.addLinkSet(graph.getLinks());        
        pamContent.setContent((NodeStructure)newGraph);
    }//private void syncNodeActivation
    
    public void sendPercept(){
    	if(!pamContent.isEmpty())
	    	for(int i = 0; i < pamListeners.size(); i++)
				pamListeners.get(i).receivePAMContent(pamContent);
    }
    
    public void decay() {
    	Set<Node> nodes = graph.getNodes();
        for(Node n: nodes){
        	if(n instanceof PamNode){
        		PamNode temp = (PamNode)n;
        		temp.decay(decayBehavior);   
        	}
        }
    }//decay

	public void setDecayCurve(DecayBehavior c) {
		decayBehavior = c;
	}
	
    public void setExciteBehavior(ExciteBehavior behavior){
    	Set<Node> nodes = graph.getNodes();
    	for(Node n: nodes){
    		n.setExciteBehavior(behavior);
    	}
    }//public void setExciteBehavior   
    
    public double getUpscale(){
    	return upscale;
    }
    
    public double getDownscale(){
    	return downscale;
    }
    
    public double getSelectivity(){
    	return selectivity;
    }

	public int getNodeCount() {
		return graph.getNodes().size();
	}

	public int getLinkCount() {
		return graph.getLinkCount();
	}

}//class PAM.java