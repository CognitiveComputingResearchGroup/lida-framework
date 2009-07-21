package edu.memphis.ccrg.lida.example.genericLIDA.environSensoryMem;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.sensorymotorautomatism.SensoryMotorListener;

public class VisionSensoryMemory implements SensoryMemory, SensoryMotorListener {

	private double[][] sensoryContent;
	private Environment environment;

	public VisionSensoryMemory(Environment environment) {
		this.environment = environment;
	}

	public void processSensors() {
		sensoryContent = ((VisionEnvironment) environment).getEnvironContent();
	}

	public Object getContent(String type, Object... parameters) {
		if ("vision".equalsIgnoreCase(type))
			return sensoryContent;
		
		return null;
	}
	
}// class