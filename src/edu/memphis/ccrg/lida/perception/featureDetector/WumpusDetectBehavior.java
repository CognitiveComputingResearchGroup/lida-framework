package edu.memphis.ccrg.lida.perception.featureDetector;

import java.util.Map;
import java.util.HashMap;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.interfaces.DetectBehavior;
import edu.memphis.ccrg.lida.perception.interfaces.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.util.Misc;

public class WumpusDetectBehavior implements DetectBehavior {
	
	private double detectThreshold = 0.5;
	private double defaultExcitation = 0.8;

	private Map<String, Integer> codeMap = new HashMap<String, Integer>();
	
	public WumpusDetectBehavior(Map<String, Integer> codeMap){
		this.codeMap = codeMap;
	}

	public void detectAndExcite(PamNodeImpl node, SensoryContentImpl sc){
		String nodeLabel = node.getLabel();
		Integer posToCheck = codeMap.get(nodeLabel);//an integer 0-3		
		char[][][] senseData = (char[][][])sc.getContent();
		
		if(posToCheck != null){
			node.clearAllWWLocations();			//clear old references
			
			char whatToLookFor = ' ';
			if(posToCheck == 0)
				whatToLookFor = 'P';
			else if(posToCheck == 1)
				whatToLookFor = 'W';
			else if(posToCheck == 2)
				whatToLookFor = 'G';
			else if(posToCheck == 3)
				whatToLookFor = 'A';
			
			for(int i = 0; i < senseData.length; i++){
				for(int j = 0; j < senseData[0].length; j++){					
					if(whatToLookFor == 'A'){
						 char whatIsThere = senseData[i][j][posToCheck];
						 if(whatIsThere == 'A' || whatIsThere == 'V' || 
							whatIsThere == '<' || whatIsThere == '>'){					
							node.excite(defaultExcitation);
							node.addNewWWLocation(i, j);
						 }
					}else if(senseData[i][j][posToCheck] == whatToLookFor){
						node.excite(defaultExcitation);
						node.addNewWWLocation(i, j);					
					}
				}//for
			}//for
		}//if node is in the map	
	}//detectAndExcite

	public void setDetectThreshold(double d) {
		detectThreshold = d;		
	}

	public void setExcitationAmount(double d) {
		defaultExcitation = d;		
	}
	
}//class WumpusDetectBehavior
