package edu.memphis.ccrg.lida.perception.interfaces;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;

public interface FeatureDetector {
	public void detect(SensoryContentImpl sm); 
	public void setDetectBehavior(DetectBehavior b);
}
