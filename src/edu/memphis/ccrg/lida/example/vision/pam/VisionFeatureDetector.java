package edu.memphis.ccrg.lida.example.vision.pam;

import edu.memphis.ccrg.lida.perception.FeatureDetector;
import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;

public class VisionFeatureDetector implements FeatureDetector {

	private DetectBehavior behavior;
	private PamNodeImpl node;

	public VisionFeatureDetector(PamNodeImpl pNode, VisionDetectBehavior b) {
		node = pNode;
		behavior = b;
	}

	public void detect(SensoryMemoryContent sc) {
		behavior.detectAndExcite(node, sc);
    	//Printer.print((double[][])sc.getContent());
	}

	public void setDetectBehavior(DetectBehavior b) {
		behavior = b;
	}

	public PamNode getPamNode() {
		return node;
	}

	public void setNode(PamNode node) {
		this.node = (PamNodeImpl) node;
	}

}//class
