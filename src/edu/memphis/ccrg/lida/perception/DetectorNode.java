package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.M;

public class DetectorNode extends Node implements FeatureDetectorInterface{

	public DetectBehavior detectBehav;
	
	public DetectorNode(DetectorNode n){
		super(n);
		this.detectBehav = n.detectBehav;
	}

	public DetectorNode(long id, double bla, double ca, String label, 
						int type, DetectBehavior detBehav){
		super(id, bla, ca, label, type);
		detectBehav = detBehav;
	}

    public void detect(SensoryContent sc){    	
    	if(sc.equals(null)){
    		M.p("Tried to detect null SensoryContent");
    		return;
    	}
    	
    	double amountToExcite = detectBehav.getExcitation(super.getLabel(), sc);
    	super.excite(amountToExcite);
    	super.synchronize();    	
    }    

	public void setDetectBehavior(DetectBehavior b){
    	detectBehav = b;
    }
	
}
