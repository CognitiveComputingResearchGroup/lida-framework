package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public class FeatureDetectorImpl implements FeatureDetector{

	public DetectBehavior detectBehav;
	private PamNodeImpl pamNode;
	
	public FeatureDetectorImpl(PamNodeImpl n, DetectBehavior b){
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
	
	public PamNodeImpl getNode(){
		return pamNode;
	}

}//class
