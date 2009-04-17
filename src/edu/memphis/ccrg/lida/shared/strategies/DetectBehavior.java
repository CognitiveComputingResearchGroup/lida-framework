package edu.memphis.ccrg.lida.shared.strategies;

import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImplW;

public interface DetectBehavior {

	void detectAndExcite(PamNodeImplW node, SensoryContentImpl sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
