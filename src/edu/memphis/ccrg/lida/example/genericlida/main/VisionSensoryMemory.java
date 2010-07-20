package edu.memphis.ccrg.lida.example.genericlida.main;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

public class VisionSensoryMemory extends SensoryMemoryImpl{

	private double[][] sensoryContent;

	public VisionSensoryMemory() {
		sensoryContent = new double[5][5];
	}

	@Override
	public void runSensors() {
		sensoryContent = (double[][]) environment.getModuleContent();
	}

	@Override
	public Object getContent(String type, Object... parameters) {
		if ("vision".equalsIgnoreCase(type))
			return sensoryContent;
		return null;
	}

	@Override
	public Object getModuleContent() {
		return getContent("vision");
	}

	@Override
	public void receiveAction(LidaAction a) {
		// TODO Auto-generated method stub
		
	}

}// class