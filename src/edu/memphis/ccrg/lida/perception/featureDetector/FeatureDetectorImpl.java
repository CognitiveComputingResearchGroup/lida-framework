package edu.memphis.ccrg.lida.perception.featureDetector;

import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.interfaces.DetectBehavior;
import edu.memphis.ccrg.lida.perception.interfaces.FeatureDetector;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.util.Misc;

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

    public void detect(SensoryContentImpl sc){    	
    	if(sc.equals(null)){
    		Misc.p("Tried to detect null SensoryContent");
    		return;
    	}
    	
    	detectBehav.detectAndExcite(pamNode, sc);
    }    

	public void setDetectBehavior(DetectBehavior b){
    	detectBehav = b;
    }
	
	public PamNodeImpl getNode(){return pamNode;}
	
}
