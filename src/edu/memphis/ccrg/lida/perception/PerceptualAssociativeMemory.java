/*
 * @PerceptualAssociativeMemory.java  2.0  2/2/09
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.perception;

import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.TreeSet;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryListener;
import edu.memphis.ccrg.lida.util.DecayBehavior;
import edu.memphis.ccrg.lida.util.M;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferContent;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferListener;


public class PerceptualAssociativeMemory implements PAMInterface, 
	SensoryListener, EBufferListener, BroadcastListener{
	
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
     * All nodes registered in this PAM
     */
	private Set<Node> nodes; 	
    /**
     * Nodes that receive activation from SM. Key is the node's label.
     */
	public Set<FeatureDetectorInterface> fDetectorNodes;
	private LinkMap linkMap;
	
	private Map<Integer, List<Node>> layerMap;
    private List<Percept> perceptHistory;
    
    //For Intermodule communication
    private List<PAMListener> pamListeners;    
    private SensoryContent sensoryContent;//Shared variable
    private PAMContent pamContent;//Not a shared variable
    private EBufferContent eBufferContent;//Shared variable
    private BroadcastContent broadcastContent;//Shared variable	
      
    public PerceptualAssociativeMemory(){
    	nodes = new HashSet<Node>();
    	fDetectorNodes = new HashSet<FeatureDetectorInterface>();
    	linkMap = new LinkMap();
    	layerMap = new HashMap<Integer, List<Node>>();
    	perceptHistory = new ArrayList<Percept>();
    	
    	pamListeners = new ArrayList<PAMListener>();
    	sensoryContent = new SensoryContent();
    	pamContent = new PAMContent();
    	eBufferContent = new EBufferContent();
    	//broadcastContent = new BroadcastContent();//TODO: write this class 	
    }
    
    //SETTING UP PAM    
    public void setParameters(Map<String, Object> parameters){    	
		Object o = parameters.get("upscale");
		if ((o != null)&& (o instanceof Double)) 
			upscale = (Double)o;
		
		o = parameters.get("downscale");
		if ((o != null)&& (o instanceof Double)) 
			downscale = (Double)o;
		
		o = parameters.get("selectivity");
		if ((o != null)&& (o instanceof Double)) 
			selectivity = (Double)o;   	
    }//public void setParameters(Map<String, Object> parameters)
    
    public void addToPAM(Set<Node> nodesToAdd, Set<Link> linkSet){
    	for(Node n: nodesToAdd){
    		if(n != null){
    			nodes.add(n);
    			if(n instanceof FeatureDetectorInterface)
    				fDetectorNodes.add((FeatureDetectorInterface)n);
    			refresh();
    		}else{
    			System.out.println("Tried to register a null node!");
    		}
    	}///for each node
    	
    	linkMap.addLinkSet(linkSet);
    }//public void addToPAM(Set<Node> nodes, Set<Link> links)   
        
    private void refresh() {
        layerMap = linkMap.getLayerMap();
        
        for(Integer layerDepth:(new TreeSet<Integer>(this.layerMap.keySet()))) 
           for(Node node:this.layerMap.get(layerDepth))
               refreshActivationParameters(node);
           
           
    }//refresh
    
    //INTERMODULE COMMUNICATION
    
    public void addPAMListener(PAMListener pl){
		pamListeners.add(pl);
	}
    
    public synchronized void receiveSense(SensoryContent sc){//SensoryContent    	
    	sensoryContent = sc;    	
    }
    

	public synchronized void receiveEBufferContent(EBufferContent c) {
		eBufferContent = c;		
	}
    	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = bc;		
	}
    
    //public synchronized void receivePreafferentSignal(PreafferentContent pc){
    	//TODO: eventually implement
    //}
	
	//FUNDAMENTAL PAM FUNCTIONS        
    public void sense(){
    	SensoryContent sc = null;
    	synchronized(this){
    		sc = (SensoryContent)sensoryContent.getThis();
    	}
  
    	for(FeatureDetectorInterface n: fDetectorNodes)
    		if(n instanceof FeatureDetectorInterface)
    			n.detect(sc);    			
    	
	      	
    }//public void sense()
        
    /**
     * 
     */
    public void passActivation(){
    	for(Node n: nodes){
    		n.excite(0.0);    		
    		if(!linkMap.isTopNode(n)) {
    			double energy = n.getCurrentActivation()*upscale;
    			Set<Node> parents = linkMap.getParents(n);
    	        for(Node parent: parents)
    	        	parent.excite(energy);
    		}//if not a root node    		
    	}//for each node
    	
    	syncNodeActivation();   
    	downscale = downscale + 1 - 1;
    	//TODO:this is where the episodic buffer activation may come into play    	
    }//public void passActivation
    
    public void refreshActivationParameters(Node n) {
        updateMinActivation(n);
        updateMaxActivation(n);
        updateSelectionThreshold(n);
    }
    
    /**
     * Updates the minimum activation possible for this node.
     * <p>
     * Since this method recursively invokes getMinActivation from its children,
     * it assumes that the children have already been updated.
     */
    private void updateMinActivation(Node n) {
        if(linkMap.isBottomNode(n))
        	n.setMinActivation(n.MIN_NODE_ACTIVATION);
        else{
        	double sumOfChildMinActiv = 0.0;
        	Set<Node> children = linkMap.getChildren(n);
            for(Node child: children)
            	sumOfChildMinActiv += child.getMinActivation();
            
            n.setMinActivation(sumOfChildMinActiv * upscale);            
        }  
    }
	
	private void updateMaxActivation(Node n){
	    if(linkMap.isBottomNode(n))
	        n.setMaxActivation(n.MAX_NODE_ACTIVATION);
	    else{
	    	double sumOfChildMaxActiv = 0.0;
	    	Set<Node> children = linkMap.getChildren(n);
	    	for(Node child: children)
	        	sumOfChildMaxActiv += child.getMaxActivation();
	        
	        n.setMaxActivation(sumOfChildMaxActiv * upscale);       
	    }    
	}//updateMaxActivation
	
	private void updateSelectionThreshold(Node n){
		double min = n.getMinActivation();
		double max = n.getMaxActivation();
		double threshold = selectivity*(max - min) + min;
		n.setSelectionThreshold(threshold);
	}    
    
    public void setExciteBehavior(ExciteBehavior behavior){
    	for(Node n: nodes){
    		n.setExciteBehavior(behavior);
    	}
    }//public void setExciteBehavior   
    
    /**
     * Synchronizes this PAM by updating the percept and percept history. First
     * the percept is cleared, and then all relevant nodes are put in a new
     * percept. Last, the new percept is added to the percept history.
     * @see Node#synchronize()
     */
    private void syncNodeActivation(){
        Percept percept = new Percept();
       
        //this.printNodeActivations();
        
        for(Node node: nodes) {
            node.synchronize();//Needed since excite changes current but not totalActivation
            if(node.isRelevant())//Based on totalActivation
                percept.add(new Node(node));
        }//for        
        
        //System.out.println("size of percept is " + percept.size());
        //percept.print();
        
        pamContent.setNodes(new Percept(percept));        
        perceptHistory.add(new Percept(percept));
    }//private void syncNodeActivation
    
    public void sendPercept(boolean shouldPrint){
    	if(shouldPrint)
    		pamContent.print();
    	    	
    	for(int i = 0; i < pamListeners.size(); i++){
			(pamListeners.get(i)).receivePAMContent(pamContent);
    	}
    }
    
    public void decay() {
        for(Node n: nodes)
        	n.decay();        
    }//decay

	public void setDecayCurve(DecayBehavior c) {
		for(Node n: nodes)
			n.setDecayBehav(c);		
	}
              
	//SIMPLE METHODS
       
    /**
     * returns a linked list of node objects
     * @return Linked list of node objects
     */    
    public Set<Node> getNodes() {
        return nodes;
    }
    
    public LinkMap getLinkMap(){
    	return linkMap;
    }

}//class PAM.java