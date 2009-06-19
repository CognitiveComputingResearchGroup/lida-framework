package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public interface FeatureDetector {
	public abstract void detect(SensoryContent sm); 
	public abstract void setDetectBehavior(DetectBehavior b);
	public abstract PamNode getPamNode();
}
