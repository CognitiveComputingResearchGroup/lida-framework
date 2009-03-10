package edu.memphis.ccrg.lida.perception.interfaces;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public interface FeatureDetector {
	public void detect(SensoryContent sm); 
	public void setDetectBehavior(DetectBehavior b);
}
