package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.M;

public class PAMFeatureDetector implements FeatureDetector{

	public DetectBehavior detectBehav;
	private Node pamNode;
	
	public PAMFeatureDetector(PAMFeatureDetector n){
		this.detectBehav = n.detectBehav;
		pamNode = n.pamNode;
	}

    public void detect(SensoryContent sc){    	
    	if(sc.equals(null)){
    		M.p("Tried to detect null SensoryContent");
    		return;
    	}
    	
    	double amountToExcite = detectBehav.getExcitation(pamNode.getLabel(), sc);
    	M.p(pamNode.getLabel() + " exciting this much: " + amountToExcite);
    	
    	pamNode.excite(amountToExcite);
    	pamNode.synchronize();    	
    	M.p(" result of detecting " + pamNode.getLabel() + " "  + pamNode.getTotalActivation());
    }    

	public void setDetectBehavior(DetectBehavior b){
    	detectBehav = b;
    }
	
}
