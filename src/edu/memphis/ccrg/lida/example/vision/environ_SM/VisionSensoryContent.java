package edu.memphis.ccrg.lida.example.vision.environ_SM;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public class VisionSensoryContent implements SensoryContent {

	private double[][] image = new double[1][1];
	
	public Object getContent() {
		return image;
	}

	public void setContent(Object o) {
		image = (double[][])o;
	}

}
