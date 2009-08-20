package edu.memphis.ccrg.lida.example.genericlida.main;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

public class VisionSensoryMemory extends SensoryMemoryImpl{

	private double[][] sensoryContent;

	public VisionSensoryMemory() {
		sensoryContent = new double[1][1];
	}

	public void processSensors() {
		sensoryContent = ((VisionEnvironment) environment).getEnvironContent();
	}

	public Object getContent(String type, Object... parameters) {
		if ("vision".equalsIgnoreCase(type))
			return sensoryContent;
		
		return null;
	}

	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		// TODO Auto-generated method stub
		
	}
	
}// class