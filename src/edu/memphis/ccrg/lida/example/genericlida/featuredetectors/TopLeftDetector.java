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
public class TopLeftDetector extends FeatureDetectorImpl {

	public TopLeftDetector(PamNode n, SensoryMemory sm,
			PerceptualAssociativeMemory pam, LidaTaskManager tm) {
		super(n, sm, pam, tm);
		// TODO Auto-generated constructor stub
	}

	public double detect() {
		double[][] data = (double[][]) sm.getContent("vision");
		
		if (data[0][0] > 0.0) {
			setActivation(getActivation()+.01);
			return 1.0;
			
		}
		return 0.0;
	}
	
	public String toString(){
		return "TopLeftDetector " + getTaskId();
	}
	
}
