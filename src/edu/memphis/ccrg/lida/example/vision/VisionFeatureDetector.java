package edu.memphis.ccrg.lida.example.vision;

import edu.memphis.ccrg.lida.perception.FeatureDetector;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;
import edu.memphis.ccrg.lida.util.Printer;

public class VisionFeatureDetector implements FeatureDetector {

	public VisionFeatureDetector(PamNodeImpl pNode, VisionDetectBehavior b) {
		// TODO Auto-generated constructor stub
	}

	public void detect(SensoryContent sm) {
		// TODO Auto-generated method stub
    	Printer.print((double[][])sm.getContent());
	}

	public void setDetectBehavior(DetectBehavior b) {
		// TODO Auto-generated method stub

	}

}
