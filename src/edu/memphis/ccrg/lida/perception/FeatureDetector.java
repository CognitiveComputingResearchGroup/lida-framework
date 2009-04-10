package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.wumpusWorld_sensoryMemory.SensoryContentImpl;

public interface FeatureDetector {
	public void detect(SensoryContentImpl sm); 
	public void setDetectBehavior(DetectBehavior b);
}
