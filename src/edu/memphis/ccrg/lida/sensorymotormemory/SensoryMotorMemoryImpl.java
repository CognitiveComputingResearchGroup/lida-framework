package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.ArrayList;
import java.util.List;


public class SensoryMotorMemoryImpl implements SensoryMotorMemory{

	private List<SensoryMotorListener> listeners = new ArrayList<SensoryMotorListener>();

	public void addSensoryMotorListener(SensoryMotorListener l) {
		listeners.add(l);		
	}

}
