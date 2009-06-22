package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public interface FeatureDetector {
	public abstract void detect(SensoryMemoryContent sm); 
	public abstract void setDetectBehavior(DetectBehavior b);
	public abstract PamNode getPamNode();
	public abstract void setNode(PamNode node);
}
