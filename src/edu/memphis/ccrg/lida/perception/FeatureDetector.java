package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public interface FeatureDetector {
	public void detect(SensoryContent sm); 
	public void setDetectBehavior(DetectBehavior b);
}
