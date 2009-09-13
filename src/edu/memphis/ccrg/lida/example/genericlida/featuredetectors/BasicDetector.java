package edu.memphis.ccrg.lida.example.genericlida.featuredetectors;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
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
public class BasicDetector extends FeatureDetectorImpl {

	public BasicDetector(PamNodeImpl pNode, SensoryMemory sm, 
								 PerceptualAssociativeMemory pam, 
								 LidaTaskManager tm) {
		super(pNode, sm, pam, tm);
	}

	public double detect() {
		return 1.0;
	}//method

}//class
