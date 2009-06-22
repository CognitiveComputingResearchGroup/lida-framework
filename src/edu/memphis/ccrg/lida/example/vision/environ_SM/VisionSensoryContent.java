package edu.memphis.ccrg.lida.example.vision.environ_SM;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;

public class VisionSensoryContent implements SensoryMemoryContent {

	private double[][] image = new double[1][1];
	
	public Object getContent() {
		return image;
	}

	public void setContent(Object o) {
		image = (double[][])o;
	}

}
