package edu.memphis.ccrg.lida._perception.interfaces;

import edu.memphis.ccrg.lida._perception.PamNodeImpl;
import edu.memphis.ccrg.lida._sensoryMemory.SensoryContentImpl;

public interface DetectBehavior {

	void detectAndExcite(PamNodeImpl node, SensoryContentImpl sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
