package edu.memphis.ccrg.lida.example.genericlida.pam;

import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This is an example of FeatureDetector.
 * The method detect() is domain specific and can call domain 
 * specific methods in SensoryMemory to get specific content 
 * from there. The generic method getContent(String type,Object... parameters) 
 * of SensoryMemory can be used too.  
 * 
 * @author Ryan McCall - Javier Snaider
 *
 */
public class VisionFeatureDetector extends FeatureDetectorImpl {

	public VisionFeatureDetector(PamNodeImpl pNode, SensoryMemory sm, PerceptualAssociativeMemory pam) {
		super(pNode, sm, pam);
	}

	public double detect() {
		return 1.0;
	}
	
}//class
