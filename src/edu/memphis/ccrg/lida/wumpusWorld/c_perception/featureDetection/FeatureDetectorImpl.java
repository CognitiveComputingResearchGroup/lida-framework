package edu.memphis.ccrg.lida.wumpusWorld.c_perception.featureDetection;

import edu.memphis.ccrg.lida.perception.FeatureDetector;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;

public class FeatureDetectorImpl implements FeatureDetector{

	public DetectBehavior detectBehav;
	private RyanPamNode pamNode;
	
	public FeatureDetectorImpl(RyanPamNode n, DetectBehavior b){
		detectBehav = b;
		pamNode = n;
	}
	
	public FeatureDetectorImpl(FeatureDetectorImpl n){
		this.detectBehav = n.detectBehav;
		pamNode = n.pamNode;
	}

    public void detect(SensoryContent sc){ 
    	detectBehav.detectAndExcite(pamNode, sc);
    }    

	public void setDetectBehavior(DetectBehavior b){
    	detectBehav = b;
    }
	
	public PamNodeImpl getNode(){return pamNode;}

	
}
