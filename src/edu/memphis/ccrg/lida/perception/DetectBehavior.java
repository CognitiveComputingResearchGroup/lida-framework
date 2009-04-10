package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.wumpusWorld._perception.PamNodeImpl;
import edu.memphis.ccrg.lida.wumpusWorld_sensoryMemory.SensoryContentImpl;

public interface DetectBehavior {

	void detectAndExcite(PamNodeImpl node, SensoryContentImpl sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
