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

//import edu.memphis.ccrg.main.BroadcastContent;
//import edu.memphis.ccrg.main.BroadcastListener;
import edu.memphis.ccrg.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.sensoryMemory.SensoryListener;
import edu.memphis.ccrg.util.DecayCurve;


public class PerceptualAssociativeMemory implements PAMInterface, SensoryListener, WorkspaceListener/*, /*BroadcastListener*/{
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
    private WMtoPAMContent wkspContent; //Shared variable
  //  private BroadcastContent broadcastContent = null;//TODO:remove null, //Shared variable
      
    public PerceptualAssociativeMemory(){
    	nodes = new HashSet<Node>();
    	pfdNodes = new HashMap<String, Node>();
    	linkMap = new LinkMap();
    	layerMap = new HashMap<Integer, ArrayList<Node>>();
    	perceptHistory = new ArrayList<Percept>();
    	
    	pamListeners = new ArrayList<PAMListener>();
    	sensoryContent = new SensoryContent();
    	pamContent = new PAMContent();
    	wkspContent = new WMtoPAMContent();
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
    	
    	Node bottom  = new Node(102000000, baseActivation,
				currentActivation, "bottom", upScale, selectivity, pfdType);
    	
    	Node abstr = new Node(103000000, baseActivation,
				currentActivation, "abstr", upScale, selectivity, regType);
    	
    	Node top = new Node(104000000, baseActivation,
				currentActivation, "top", upScale, selectivity, regType);
    	
    	abstr.addChild(bottom);
    	top.addChild(abstr);
   
    	boolean isPFD = true;
    	boolean isNotPFD = false;
    	register(bottom, isPFD);
    	register(abstr, isNotPFD);
    	register(top, isNotPFD);
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
 
    public synchronized void receiveWMContent(WMtoPAMContent wkspContent){
    	this.wkspContent = wkspContent;
    }    
    
//	public synchronized void receiveBroadcast(BroadcastContent bc) {
//		broadcastContent = bc;		
//	}
    
    //public synchronized void receivePreafferentSignal(PreafferentContent pc){
    	//TODO: eventually implement
    //}
        
    public void sense(){
    	int[] senseData = new int[5];    	
    	synchronized(this){
    		senseData = (int[])sensoryContent.getContent();
    	}
    	
    	if(senseData[0] == 1){
    		pfdNodes.get("bump").excite(1.0);
    	}else if(senseData[1] == 1){
    		pfdNodes.get("glitter").excite(1.0);
    	}else if(senseData[1] == 2){
    		pfdNodes.get("breeze").excite(1.0);
    	}else if(senseData[1] == 3){
    		pfdNodes.get("stench").excite(1.0);
    	}else if(senseData[1] == 4){
    		pfdNodes.get("scream").excite(1.0);
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
    	//TODO:this is where the wksp activation would come into play
    	
    }//public void passActivation
    
    public void setExciteBehavior(ExciteBehavior behavior){
    	for(Node n: nodes){
    		n.setExciteBehavior(behavior);
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
        
        for(Node node: nodes) {
            node.synchronize();//Needed since excite changes current but not totalActivation
            if(node.isRelevant())//Based on totalActivation
                percept.add(new Node(node));
        }//for        
        
        pamContent.setNodes(percept);        
        perceptHistory.add(percept);
    }
    
    public void sendPercept(){
    	for(int i = 0; i < pamListeners.size(); i++)
			(pamListeners.get(i)).receivePAMContent(pamContent);
    }
    
    public void decay() {
        for(Node n: nodes)
        	n.decay();        
    }//decay

	public void setDecayCurve(DecayCurve c) {
		for(Node n: nodes)
			n.setDecayCurve(c);		
	}
              
    public void addBroadcastListener(PAMListener pl){
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