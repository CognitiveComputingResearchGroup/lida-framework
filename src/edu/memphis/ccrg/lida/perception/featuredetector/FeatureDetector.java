package edu.memphis.ccrg.lida.perception.featuredetector;

import java.util.Map;
import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.shared.Activatible;

public interface FeatureDetector extends Activatible {
	
	public abstract double detect(); 
	public abstract PamNode getPamNode();
	public abstract void setNode(PamNode node);
	public abstract void burstPam(double detectionActivation, PamNode pNode, PerceptualAssociativeMemory pam);
	public abstract void executeDetection();
	public abstract void init(Map<String,Object> parameters);
	
}
