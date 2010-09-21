/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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
		
	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// TODO Auto-generated method stub
		return false;
	}

}// class