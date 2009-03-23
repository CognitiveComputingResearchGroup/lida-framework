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
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.perception.featureDetector.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.perception.interfaces.PAMListener;
import edu.memphis.ccrg.lida.perception.interfaces.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryListener;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferContent;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferListener;


public class PerceptualAssociativeMemoryImpl implements PerceptualAssociativeMemory, 
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
	public Set<FeatureDetectorImpl> featureDetectors;
	private GraphImpl graph;
    
    //For Intermodule communication
    private List<PAMListener> pamListeners;    
    private SensoryContentImpl sensoryContent;//Shared variable
    private PAMContentImpl pamContent;//Not a shared variable
    private EBufferContent eBufferContent;//Shared variable
    private BroadcastContent broadcastContent;//Shared variable	
      
    public PerceptualAssociativeMemoryImpl(){
    	featureDetectors = new HashSet<FeatureDetectorImpl>();
    	graph = new GraphImpl(upscale, selectivity);
    	
    	pamListeners = new ArrayList<PAMListener>();
    	sensoryContent = new SensoryContentImpl();
    	pamContent = new PAMContentImpl();
    	eBufferContent = new EBufferContent();
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
    
    public void addToPAM(Set<PamNodeImpl> nodesToAdd, Set<FeatureDetectorImpl> featureDetectors, Set<LinkImpl> linkSet){
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
    	SensoryContentImpl sc = null;
    	synchronized(this){
    		sc = (SensoryContentImpl)sensoryContent.getThis();
    	}
  
    	for(FeatureDetectorImpl d: featureDetectors)
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
        Percept percept = new Percept();
        Set<PamNodeImpl> nodes = graph.getNodes();
        for(PamNodeImpl node: nodes){
            node.synchronize();//Needed since excite changes current but not totalActivation.
            if(node.isRelevant())//Based on totalActivation
                percept.add(new PamNodeImpl(node));
        }//for      
        pamContent.setNodes(new Percept(percept));        
    }//private void syncNodeActivation
    
    public void sendPercept(){
    	for(int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receivePAMContent(pamContent);
    }
    
    public void decay() {
    	Set<PamNodeImpl> nodes = graph.getNodes();
        for(PamNodeImpl n: nodes)
        	n.decay();        
    }//decay

	public void setDecayCurve(DecayBehavior c) {
		Set<PamNodeImpl> nodes = graph.getNodes();
		for(PamNodeImpl n: nodes)
			n.setDecayBehav(c);		
	}
	
    public void setExciteBehavior(ExciteBehavior behavior){
    	Set<PamNodeImpl> nodes = graph.getNodes();
    	for(PamNodeImpl n: nodes){
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