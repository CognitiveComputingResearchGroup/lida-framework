package edu.memphis.ccrg.lida.example.vision.pam;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public class VisionDetectBehavior implements DetectBehavior{

	private double threshold = 0.0;
	private double excitationAmount = 1.0;

	public void detectAndExcite(PamNode node, SensoryMemoryContent sc) {
		//System.out.println("label " + node.getLabel());
		node.excite(excitationAmount);
		//System.out.println("activation " + node.getActivation());
	}//method

	public void setDetectThreshold(double d) {
		threshold = d;		
	}//method

	public void setExcitationAmount(double d) {
		excitationAmount  = d;
	}//method

}//class
