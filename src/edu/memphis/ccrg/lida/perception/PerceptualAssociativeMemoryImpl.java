/*
 * @PerceptualAssociativeMemory.java  2.0  2/2/09
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.perception;

import java.util.Collection;
import java.util.Set; 
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.PamNodeStructure;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

public class PerceptualAssociativeMemoryImpl implements PerceptualAssociativeMemory{

	private List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
	private PamNodeStructure graph = new PamNodeStructure();
    //For percept
    private List<PAMListener> pamListeners = new ArrayList<PAMListener>();  
    private PamNodeStructure percept = new PamNodeStructure();
	private int numNodeInPercept = 0;//for GUI
	//Shared variables
    private SensoryContent sensoryContent = new SensoryContentImpl();
    private BroadcastContent broadcastContent = new NodeStructureImpl();		
    private WorkspaceContent topDownEffects = new NodeStructureImpl();
    private NodeStructure preafferantSignal = new NodeStructureImpl();
    
    /**
     * Need to specify a SensoryContent type.
     * 
     * @param whatKindOfSC
     */
    public PerceptualAssociativeMemoryImpl(SensoryContent whatKindOfSC){
    	sensoryContent = whatKindOfSC;
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
    
    public void addToPAM(Set<Node> nodesToAdd, List<FeatureDetector> featureDetectors, Set<Link> linkSet){
    	this.featureDetectors = featureDetectors;
		graph.addPamNodes(nodesToAdd);
    	graph.addLinks(linkSet);    	
    }//method
    
    //INTERMODULE COMMUNICATION
    public void addPAMListener(PAMListener pl){
		pamListeners.add(pl);
	}
    
    public synchronized void receiveSense(SensoryContent sc){//SensoryContent    	
    	sensoryContent = sc;    	
    }

	public synchronized void receiveWorkspaceContent(WorkspaceContent content) {
		topDownEffects = content;		
	}
    	
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = bc;		
	}
    
    public synchronized void receivePreafferentSignal(NodeStructure ns){
    	//TODO: impl
    }
	
	//FUNDAMENTAL PAM FUNCTIONS        
    public void sense(){    
    	for(FeatureDetector d: featureDetectors)
    		d.detect(sensoryContent);    	
    }//method
        
    /**
     * Pass activation upwards based on the order found in the layerMap
     */
    public void passActivation(){    	
    	graph.passActivation();    	
    	syncNodeActivation();   
    	//TODO:this is where the episodic buffer activation comes into play    	
    }//method
    
    /**
     * Clear the percept's nodes. Go through graph's nodes
     * and add those above threshold to the percept.
     * 
     * TODO: If links aren't Node then this method needs to be 
     * expanded to include links.
     */
    private void syncNodeActivation(){
        percept.clearNodes();
        for(Node n: graph.getNodes()){
        	PamNodeImpl node = (PamNodeImpl)n;
            node.synchronize();//Needed since excite changes current but not totalActivation.
            if(node.isRelevant())//Based on totalActivation
            	percept.addNode(n);
        }//for      
        numNodeInPercept = percept.getNodes().size();     
    }//method
    
    public void sendPercept(){
    	for(int i = 0; i < pamListeners.size(); i++)
			pamListeners.get(i).receivePAMContent(percept);	    	
    }//method
    
    public void decay() {
    	graph.decayNodes();       	
    }//method

	public void setDecayBehavior(DecayBehavior b) {
		graph.setDecayBehavior(b);
	}
	
    public void setExciteBehavior(ExciteBehavior behavior){
    	graph.setExciteBehavior(behavior);
    }//method   

	public List<Object> getGuiContent() {
		List<Object> content = new ArrayList<Object>();
		content.add(numNodeInPercept);
		content.add(((NodeStructureImpl) percept).getLinkCount());
		return content;
	}

}//class PAM.java