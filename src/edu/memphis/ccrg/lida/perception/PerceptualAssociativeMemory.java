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
     * Nodes that receive activation from SM. Key is the node's label.
     */
	public Set<FeatureDetector> featureDetectors;
	private Graph graph;
	
	private Map<Integer, List<Node>> layerMap;
    private List<Percept> perceptHistory;
    
    //For Intermodule communication
    private List<PAMListener> pamListeners;    
    private SensoryContent sensoryContent;//Shared variable
    private PAMContent pamContent;//Not a shared variable
    private EBufferContent eBufferContent;//Shared variable
    private BroadcastContent broadcastContent;//Shared variable	
      
    public PerceptualAssociativeMemory(){
    	featureDetectors = new HashSet<FeatureDetector>();
    	graph = new Graph(upscale, selectivity);
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
		if ((o != null)&& (o instanceof Double)) {
			upscale = (Double)o;
		}
		//TODO: Graph has its own parameters?
		o = parameters.get("downscale");
		if ((o != null)&& (o instanceof Double)) 
			downscale = (Double)o;
		
		o = parameters.get("selectivity");
		if ((o != null)&& (o instanceof Double)){
			selectivity = (Double)o;   	
			graph.setSelectivity((Double)o);
		}
    }//public void setParameters(Map<String, Object> parameters)
    
    public void addToPAM(Set<Node> nodesToAdd, Set<Link> linkSet){
    	graph.addNodes(nodesToAdd);
    	graph.addLinkSet(linkSet);
    	for(Node n: nodesToAdd){
    		if(n != null && n instanceof FeatureDetector){
    		//	featureDetectors.add(n);
    		}
    	}///for each node 
    	
    }//public void addToPAM(Set<Node> nodes, Set<Link> links)   
       
    
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
    	//System.out.println("num nodes is " + nodes.size());
    	
    	SensoryContent sc = null;
    	synchronized(this){
    		sc = (SensoryContent)sensoryContent.getThis();
    	}
  
    	//for(FeatureDetectorInterface n: fDetectorNodes)
    	//	if(n instanceof FeatureDetectorInterface)
    	//		n.detect(sc);    			
    	
	      	
    }//public void sense()
        
    /**
     * 
     */
    public void passActivation(){
    	Set<Node> nodes = graph.getNodes();
//    	M.p("AREdsfs " + nodes.size());
    	for(Node n: nodes){
    		n.excite(0.0);    
    		boolean isTopNode = graph.isTopNode(n);
    		M.p(n.getLabel() + " is a top node " + isTopNode);
    		if(!isTopNode) {
    			double energy = n.getCurrentActivation() * upscale;
    			M.p("the energy set to my parents is " + energy + " up " + upscale);
    			
    			Set<Node> parents = graph.getParents(n);   			
//    			M.p(" I have " + parents.size() + " parents.");
    			
    			if(parents != null)
    				for(Node parent: parents)
    					parent.excite(energy);
    		}//if not a root node    		
    	}//for each node
    	
    	syncNodeActivation();   
    	downscale = downscale + 1 - 1;
    	//TODO:this is where the episodic buffer activation may come into play    	
    }//public void passActivation
    
    public void setExciteBehavior(ExciteBehavior behavior){
    	Set<Node> nodes = graph.getNodes();
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
        Set<Node> nodes = graph.getNodes();
        for(Node node: nodes) {
            node.synchronize();//Needed since excite changes current but not totalActivation
//            M.p( " after synching " + node.getTotalActivation());
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
    	Set<Node> nodes = graph.getNodes();
        for(Node n: nodes)
        	n.decay();        
    }//decay

	public void setDecayCurve(DecayBehavior c) {
		Set<Node> nodes = graph.getNodes();
		for(Node n: nodes)
			n.setDecayBehav(c);		
	}
              
	//SIMPLE METHODS
       
    /**
     * returns a linked list of node objects
     * @return Linked list of node objects
     */    
    public Set<Node> getNodes() {
        return graph.getNodes();
    }
    
    public Graph getLinkMap(){
    	return graph;
    }

}//class PAM.java