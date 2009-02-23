/*
 * @PerceptualAssociativeMemory.java  2.0  2/2/09
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.perception;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Set;

import edu.memphis.ccrg.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.sensoryMemory.SensoryListener;
import edu.memphis.ccrg.util.DecayCurve;
import edu.memphis.ccrg.workspace.episodicBuffer.EBufferListener;
import edu.memphis.ccrg.workspace.episodicBuffer.EBufferContent;


public class PerceptualAssociativeMemory implements PAMInterface, 
	SensoryListener, EBufferListener, BroadcastListener{
	//Parameters
    /**
     * proportion of activation spread to parents
     */
    private double upScale = 0.5;     
    /**
     * proportion of activation spread to children
     */
    private double downScale = 0.5;
    /**
     * selectivity for thresholding, % of max activation 
     */
    private double selectivity = 0.9; 
    
    //Main fields
    /**
     * All nodes registered in this PAM
     */
	private Set<Node> nodes; 	
    /**
     * Nodes that receive activation from SM. Key is the node's label.
     */
	private Map<String, Node> pfdNodes;
	private LinkMap linkMap;
	private Map<Integer, ArrayList<Node>> layerMap;
    private List<Percept> perceptHistory;
    
    //For Intermodule communication
    private List<PAMListener> pamListeners;    
    private SensoryContent sensoryContent;//Shared variable
    private PAMContent pamContent;//Not a shared variable
    private BroadcastContent broadcastContent;//Shared variable
	private EBufferContent eBufferContent;//Shared variable
      
    public PerceptualAssociativeMemory(){
    	nodes = new HashSet<Node>();
    	pfdNodes = new HashMap<String, Node>();
    	linkMap = new LinkMap();
    	layerMap = new HashMap<Integer, ArrayList<Node>>();
    	perceptHistory = new ArrayList<Percept>();
    	
    	pamListeners = new ArrayList<PAMListener>();
    	sensoryContent = new SensoryContent();
    	pamContent = new PAMContent();
    	eBufferContent = new EBufferContent();
    	//broadcastContent = new BroadcastContent(); 	
    }
    
    /**register, refresh, buildLayerMap: used when adding a new node to this object
     * 
     * the network should be ready to run after this method finishes
     * 
     */
    public void initPAM(){
    	//Read in slipnet from file    	       
        //  defaults used when creating the nodes below
    	double baseActivation = 0.0;
    	double currentActivation = 0.0;	
    	int pfdType = 0;
    	int regType = 1;
    	
    	Node breeze  = new Node(1, baseActivation, currentActivation, 
    						"breeze", upScale, selectivity, pfdType);
    	
    	Node pit = new Node(2, baseActivation, currentActivation, 
    						"pit", upScale, selectivity, regType);
    
    	pit.addChild(breeze);
   
    	boolean isPFD = true;
    	boolean isNotPFD = false;
    	register(breeze, isPFD);
    	register(pit, isNotPFD);
    	
    	//System.out.println(nodes.size() + " 3242394 " + pfdNodes.size());
    }//public void initPAM()
    
    public void addToPAM(Set<Node> nodes, Set<Link> linkSet){
    	for(Node n: nodes){
    		nodes.add(new Node(n));
    		if(n.isPFDNode())
    			this.pfdNodes.put(n.getLabel(), new Node(n));
    	}
    	
    	for(Link l: linkSet){
    		linkMap.addLink(new Link(l));//TODO: ask Javier about this
    	}
    	
    	//linkMap.add(nodes, links);
    }//public void addToPAM(Set<Node> nodes, Set<Link> links)   
    
    /**
     * 
     * 
     */
    public void register(Node node, boolean isPFD){
        if(node != null){
            nodes.add(node);  
            if(isPFD)
            	pfdNodes.put(node.getLabel(), node);
            refresh();// next method below
        }else
            System.out.println("Tried to register a null node!");
    }//register
        
    private void refresh() {
        for(Node n:nodes)  //It is imperative that the three refresh operations appear in this order.
            n.updateLayerDepth();
        
        buildLayerMap();
        
        for(Integer layerDepth:(new TreeSet<Integer>(this.layerMap.keySet()))) 
            for(Node node:this.layerMap.get(layerDepth)) 
                node.refreshActivationParameters();
           
    }//refresh
    
    private void buildLayerMap(){
        layerMap.clear();
        
        for(Node node:nodes){
            Integer layerDepth = new Integer(node.getLayerDepth());
            ArrayList<Node> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new ArrayList<Node>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);//this isn't necessary since it will be overwritten and nothing is done w/ this
        }
 
    }//buildLayerMap
    
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
        
    public void sense(){
    	int[] senseData = new int[5];    	
    	synchronized(this){
    		senseData = (int[])sensoryContent.getContent();
    	}
    	
    	//System.out.println("PAM2: " + senseData[0] + " " + senseData[1] + " " + senseData[2] + " " + senseData[3] + " " + senseData[4] + " ");
    	
    	if(senseData[0] == 1){
    		//pfdNodes.get("bump").excite(1.0);
    	}else if(senseData[1] == 1){
    		//pfdNodes.get("glitter").excite(1.0);
    	}else if(senseData[2] == 1){
    		pfdNodes.get("breeze").excite(0.79);
    	}else if(senseData[3] == 1){
    		//pfdNodes.get("stench").excite(1.0);
    	}else if(senseData[4] == 1){
    		//pfdNodes.get("scream").excite(1.0);
    	}//else		      	
    }//public void sense()
    
//	//if want to show starting activation
//	if(PAMDriver.SHOW_STARTING_ACTIVATION){
//		pln("Node activation after sensing (activations of 0.0 omitted)");
//		for(Node n: nodes){
//			double curActiv = n.getCurrentActivation();
//			if(curActiv > 0.0)
//				pln("Node: " + n.getLabel() + ", Activ: " + rnd(curActiv));
//		}//for each node 
//		pln("");
//	}//if 	
        
    /**
     * 
     */
    public void passActivation(){
    	for(Node n: nodes)
    		n.excite(0.0);
    	syncNodeActivation();   
    	//TODO:this is where the episodic buffer activation would come into play
    	
    }//public void passActivation
    
    public void setExciteBehavior(ExciteBehavior behavior){
    	for(Node n: nodes){
    		n.setExciteBehavior(behavior);
    	}
    }    
    
    public void printNodeActivations(){
    	for(Node n: nodes){
    		n.printActivation();
    	}
    }
    
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
    }
    
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

	public void setDecayCurve(DecayCurve c) {
		for(Node n: nodes)
			n.setDecayCurve(c);		
	}
              
    public void addPAMListener(PAMListener pl){
		pamListeners.add(pl);
	}
   
    public void setParameters(Map<String, Object> parameters){    	
		Object o = parameters.get("upscale");
		if ((o != null)&& (o instanceof Double)) 
			upScale = (Double)o;
		
		o = parameters.get("downscale");
		if ((o != null)&& (o instanceof Double)) 
			downScale = (Double)o;
		
		o = parameters.get("selectivity");
		if ((o != null)&& (o instanceof Double)) 
			selectivity = (Double)o;   	
    }//public void setParameters(Map<String, Object> parameters)
    
    /**
     * returns a linked list of node objects
     * @return Linked list of node objects
     */    
    public Set<Node> getNodes() {
        return nodes;
    }
    
    public LinkMap getLinks(){
    	return linkMap;
    }

    /**
     * returns a layer map
     * @return layer map
     */
    public Map<Integer, ArrayList<Node>> getLayerMap() {
        return layerMap;
    }
    
    public void pln(String s){  //saves typing
    	System.out.println(s);
    }
    
    public double rnd(double d){    //rounds a double to the nearest 100th
    	return Math.round(d*100.0)/100.0;
    }

}//class PAM.java