/**
 * 
 */
package edu.memphis.ccrg.lida.example.genericlida.featuredetectors;

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
							   PerceptualAssociativeMemory pam) {
		super(n, sm, pam);
	}

	public double detect() {
		double[][] data = (double[][]) sm.getContent("vision");
		
		if (data != null && data[data.length-1][data[0].length-1] > 0.0) {
			this.excite(0.01);
			return 1.0;
		}
		return 0.0;
	}
	
	public String toString(){
		return "BottomRightDetector " + getTaskId();
	}
 
}
