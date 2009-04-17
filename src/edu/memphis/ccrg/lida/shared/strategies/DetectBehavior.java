package edu.memphis.ccrg.lida.shared.strategies;

import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;

public interface DetectBehavior {

	void detectAndExcite(RyanPamNode node, SensoryContentImpl sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
