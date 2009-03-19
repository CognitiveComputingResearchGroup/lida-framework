package edu.memphis.ccrg.lida.perception.interfaces;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public interface DetectBehavior {

	void detectAndExcite(PamNode node, SensoryContent sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
