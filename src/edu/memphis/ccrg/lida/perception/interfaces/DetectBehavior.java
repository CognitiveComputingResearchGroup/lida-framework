package edu.memphis.ccrg.lida.perception.interfaces;

import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;

public interface DetectBehavior {

	void detectAndExcite(PamNodeImpl node, SensoryContentImpl sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
