package edu.memphis.ccrg.lida.example.genericLIDA.pam;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public class VisionDetectBehavior implements DetectBehavior{

	private double excitationAmount = 1.0;

	public void detectAndExcite(PamNode node, SensoryMemoryContent sc) {
		node.excite(excitationAmount);
	}//method

}//class
