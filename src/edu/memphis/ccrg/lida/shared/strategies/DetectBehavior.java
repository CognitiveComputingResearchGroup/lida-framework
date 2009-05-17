package edu.memphis.ccrg.lida.shared.strategies;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public interface DetectBehavior {

	void detectAndExcite(PamNode node, SensoryContent sc);	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
