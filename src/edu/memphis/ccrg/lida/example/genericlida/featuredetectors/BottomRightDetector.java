/**
 * 
 */
package edu.memphis.ccrg.lida.example.genericlida.featuredetectors;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * @author Javier Snaider
 * 
 */
public class BottomRightDetector extends FeatureDetectorImpl {

	public BottomRightDetector(PamNode n, SensoryMemory sm,
			PerceptualAssociativeMemory pam, LidaTaskManager tm) {
		super(n, sm, pam, tm);
		// TODO Auto-generated constructor stub
	}

	public BottomRightDetector() {
	}

	public double detect() {
		double[][] data = (double[][]) sm.getContent("vision");
		
		if (data[data.length-1][data[0].length-1] > 0.0) {
			setActivation(getActivation()+.01);
			return 1.0;
			
		}
		return 0.0;
	}
	public String toString(){
		return "FeatureDetector "+ getTaskId();
	}
}
