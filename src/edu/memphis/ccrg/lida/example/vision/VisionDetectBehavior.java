package edu.memphis.ccrg.lida.example.vision;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public class VisionDetectBehavior implements DetectBehavior{

	private double threshold = 0.0;
	private double excitationAmount = 1.0;

	public void detectAndExcite(PamNode node, SensoryContent sc) {
		node.excite(excitationAmount);
	}//method

	public void setDetectThreshold(double d) {
		threshold = d;		
	}//method

	public void setExcitationAmount(double d) {
		excitationAmount  = d;
	}//method

}//class
