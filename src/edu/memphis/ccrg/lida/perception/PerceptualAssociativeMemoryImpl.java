/*
 * @PerceptualAssociativeMemory.java  2.0  2/2/09
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.perception;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set; 
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import edu.memphis.ccrg.lida.framework.BroadcastLearner;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContentImpl;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;

public class PerceptualAssociativeMemoryImpl implements PerceptualAssociativeMemory, GuiContentProvider, BroadcastLearner{
	
	private PamNodeStructure graph = new PamNodeStructure();
	private List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
	private NodeStructureImpl percept = new NodeStructureImpl();
    private List<PAMListener> pamListeners = new ArrayList<PAMListener>();  
	//Shared variables
    private SensoryMemoryContent sensoryMemoryContent = new SensoryMemoryContentImpl();	
    private NodeStructure topDownContent = new NodeStructureImpl();
    private NodeStructure broadcastContent = new NodeStructureImpl();	
    private NodeStructure preafferantSignal = new NodeStructureImpl();
    //for GUI
    private List<FrameworkGui> guiList = new ArrayList<FrameworkGui>();
	private List<Object> guiContent = new ArrayList<Object>();
  
    /**
     * Need to specify a SensoryContent type.
     * 
     * @param kindOfSensoryContent
     */
    public PerceptualAssociativeMemoryImpl(SensoryMemoryContent kindOfSensoryContent){
    	sensoryMemoryContent = kindOfSensoryContent;
    }

    /**
     * 
     */
    public void setParameters(Map<String, Object> parameters){    	
		Object o = parameters.get("upscale");
		if((o != null)&& (o instanceof Double)) {
			synchronized(this){
				graph.setUpscale((Double)o);				
			}
		}
		o = parameters.get("downscale");
		if((o != null)&& (o instanceof Double)) 
			synchronized(this){
				graph.setDownscale((Double)o);
			}		
		o = parameters.get("selectivity");
		if ((o != null)&& (o instanceof Double)){
			synchronized(this){
				graph.setSelectivity((Double)o);   	
			}
		}
    }//method
    
    public void addToPAM(Set<PamNode> nodes, List<FeatureDetector> ftDetectors, Set<Link> links){
    	featureDetectors = ftDetectors;
		graph.addPamNodes(nodes);
    	graph.addLinks(links);  
    	
    	for(FeatureDetector fd: featureDetectors){
    		long id = fd.getPamNode().getId();
    		fd.setNode((PamNode) graph.getNode(id));
    	}
    	
    }//method
    
    //******INTERMODULE COMMUNICATION******
    public void addPAMListener(PAMListener pl){
		pamListeners.add(pl);
	}   

	public void addFrameworkGui(FrameworkGui listener) {
		guiList.add(listener);		
	}
  
    public synchronized void receiveSensoryMemoryContent(SensoryMemoryContent sc){//SensoryContent    	
    	sensoryMemoryContent = sc;    	
    }

	public synchronized void receiveWorkspaceContent(NodeStructure content, int originatingBuffer) {
		topDownContent = content;		
	}
    	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;		
	}
    
    public synchronized void receivePreafferentSignal(NodeStructure ns){
    	preafferantSignal = ns;
    }
	
	//******FUNDAMENTAL PAM FUNCTIONS******        
    public void detectSensoryMemoryContent(){    
    	for(FeatureDetector d: featureDetectors)
    		d.detect(sensoryMemoryContent);    	
    }//method
        
    /**
     * Pass activation upwards based on the order found in the layerMap
     */
    public void propogateActivation(){    
    	Set<PamNode> bottomNodes = new HashSet<PamNode>();
		for(FeatureDetector fd: featureDetectors)
			bottomNodes.add(fd.getPamNode());
		
    	graph.passActivationUpward(bottomNodes);    	
    	updatePercept();   
    	//TODO:impl episodic buffer activation into activation passing  
    	//TODO:use preafferent signal
    }//method
    
    /**
     * Clear the percept's nodes. Go through graph's nodes
     * and add those above threshold to the percept.
     * 
     * TODO: If links aren't Node then this method needs to be 
     * expanded to include links.
     */
    private void updatePercept(){
        percept.clearNodes();
        for(Node n: graph.getNodes()){
        	PamNodeImpl node = (PamNodeImpl)n;
            if(node.isRelevant())//Based on totalActivation
            	percept.addNode(node);
        }//for  
        //This is a good place to update guiContent
        guiContent.clear();
        guiContent.add(percept.getNodeCount());
        guiContent.add(percept.getLinkCount());
    }//method
    
    public void sendOutPercept(){
    	NodeStructure copy = new NodeStructureImpl(percept);
    	for(int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receivePAMContent(copy);	    	
    }//method

	public void sendGuiContent() {
		for(FrameworkGui fg: guiList)
			fg.receiveGuiContent(FrameworkGui.FROM_PAM, guiContent);
	}

	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for(Node n: nodes){
			//TODO:
			n.getId();
		}
	}
    
    public void decayPAM() {
    	graph.decayNodes();       	
    }//method

	public void setDecayBehavior(DecayBehavior b) {
		graph.setNodesDecayBehavior(b);
	}
	
    public void setExciteBehavior(ExciteBehavior behavior){
    	graph.setNodesExciteBehavior(behavior);
    }//method   

}//class PAM.java