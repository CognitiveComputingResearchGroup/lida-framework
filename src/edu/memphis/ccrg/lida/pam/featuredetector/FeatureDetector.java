package edu.memphis.ccrg.lida.pam.featuredetector;

import java.util.Map;

import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.shared.Activatible;

public interface FeatureDetector extends Activatible {
	
	public abstract double detect(); 
	public abstract PamNode getPamNode();
	public abstract void setNode(PamNode node);
	public abstract void excitePam(double detectionActivation);
	public abstract void executeDetection();
	public abstract void init(Map<String,Object> parameters);
	
}
