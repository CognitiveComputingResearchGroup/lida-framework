package edu.memphis.ccrg.lida.perception.featureDetector;

import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.interfaces.DetectBehavior;
import edu.memphis.ccrg.lida.perception.interfaces.FeatureDetector;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.M;

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
    	if(sc.equals(null)){
    		M.p("Tried to detect null SensoryContent");
    		return;
    	}
    	
    	double amountToExcite = detectBehav.getExcitation(pamNode.getLabel(), sc);
//    	M.p(pamNode.getLabel() + " exciting this much: " + amountToExcite);
//    	
    	pamNode.excite(amountToExcite);
    	pamNode.synchronize();  
    	//M.p(pamNode.toActivationString());
//    	M.p(" result of detecting " + pamNode.getLabel() + " "  + pamNode.getTotalActivation());
    }    

	public void setDetectBehavior(DetectBehavior b){
    	detectBehav = b;
    }
	
	public PamNodeImpl getNode(){return pamNode;}
	
}
