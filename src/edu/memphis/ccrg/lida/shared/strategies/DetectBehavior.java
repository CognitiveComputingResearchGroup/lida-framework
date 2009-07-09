package edu.memphis.ccrg.lida.shared.strategies;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;

public interface DetectBehavior {

	void detectAndExcite(PamNode node, SensoryMemoryContent sc);	

}
