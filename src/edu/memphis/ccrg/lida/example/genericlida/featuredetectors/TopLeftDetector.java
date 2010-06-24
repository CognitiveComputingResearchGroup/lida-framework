/**
 * 
 */
package edu.memphis.ccrg.lida.example.genericlida.featuredetectors;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
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
			PerceptualAssociativeMemory pam) {
		super(n, sm, pam);
		// TODO Auto-generated constructor stub
	}

	public double detect() {
		double[][] data = (double[][]) sm.getContent("vision");

		if (data != null && data[0][0] > 0.0) {
			//this.excite(0.01);
			return 1.0;
		}
		return 0.0;
	}

	@Override
	public void detectMultipleNodes() {
		// TODO Auto-generated method stub
		
	}
	
}
