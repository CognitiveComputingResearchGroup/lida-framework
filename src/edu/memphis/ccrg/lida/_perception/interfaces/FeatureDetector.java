package edu.memphis.ccrg.lida._perception.interfaces;

import edu.memphis.ccrg.lida._sensoryMemory.SensoryContentImpl;

public interface FeatureDetector {
	public void detect(SensoryContentImpl sm); 
	public void setDetectBehavior(DetectBehavior b);
}
