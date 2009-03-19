package edu.memphis.ccrg.lida.perception.featureDetector;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.perception.interfaces.DetectBehavior;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.Misc;

public class WumpusDetectBehavior implements DetectBehavior {
	
	private double detectThreshold = 0.5;
	private double excitation = 0.8;

	private Map<String, Integer> codeMap = new HashMap<String, Integer>();
	
	public WumpusDetectBehavior(Map<String, Integer> codeMap){
		this.codeMap = codeMap;
	}

	public double getExcitation(String nodeLabel, SensoryContent sc){
		char[][][] senseData = (char[][][])sc.getContent();
		//System.out.println("Detect: ");
		
		//Misc.print(senseData);
		
		Integer posToCheck = codeMap.get(nodeLabel);
		//if(!posToCheck.equals(null))			
			
				//return excitation;		
	    return 0.0;
	}

	public void setDetectThreshold(double d) {
		detectThreshold = d;		
	}

	public void setExcitationAmount(double d) {
		excitation = d;		
	}
	
}//class WumpusDetectBehavior
