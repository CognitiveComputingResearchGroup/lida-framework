package edu.memphis.ccrg.lida.shared.strategies;

import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImpl;

public interface DetectBehavior {

	void detectAndExcite(PamNodeImpl node, SensoryContentImpl sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
