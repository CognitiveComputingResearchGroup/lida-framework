package edu.memphis.ccrg.lida.perception.featureDetector;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.interfaces.DetectBehavior;
import edu.memphis.ccrg.lida.perception.interfaces.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.Misc;

public class WumpusDetectBehavior implements DetectBehavior {
	
	private double detectThreshold = 0.5;
	private double defaultExcitation = 0.8;

	private Map<String, Integer> codeMap = new HashMap<String, Integer>();
	
	public WumpusDetectBehavior(Map<String, Integer> codeMap){
		this.codeMap = codeMap;
	}

	public void detectAndExcite(PamNode node, SensoryContent sc){
		char[][][] senseData = (char[][][])sc.getContent();
		
		String nodeLabel = node.getLabel();
		Integer posToCheck = codeMap.get(nodeLabel);//want this 1-4		
		System.out.println("Detecting for " + nodeLabel + "\n");
		
		if(posToCheck != null){
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
						 if(whatIsThere == 'A' || whatIsThere == 'V' || whatIsThere == '<' || whatIsThere == '>'){					
							node.excite(defaultExcitation);
							//Store the i, j info in node!!!TODO:
							
							//node.synchronize(); //TODO: synchronize activation or not??
							Misc.p(nodeLabel + " " + Misc.rnd(node.getCurrentActivation()));
						 }
					}else if(senseData[i][j][posToCheck] == whatToLookFor){
						//M.p(pamNode.getLabel() + " exciting this much: " + amountToExcite);
						node.excite(defaultExcitation);
						//Store the i, j info in node!!!
						
						//node.synchronize(); //TODO: synchronize activation or not??
						Misc.p(nodeLabel + " " + Misc.rnd(node.getCurrentActivation()));
						//M.p(" result of detecting " + pamNode.getLabel() + " "  + pamNode.getTotalActivation());
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
