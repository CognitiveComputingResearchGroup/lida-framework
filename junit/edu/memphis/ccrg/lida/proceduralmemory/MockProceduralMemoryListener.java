/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.actionselection.Behavior;

public class MockProceduralMemoryListener implements ProceduralMemoryListener {

	public List<Behavior> behaviors = new ArrayList<Behavior>();
	public int timesCalled;

	@Override
	public void receiveBehavior(Behavior b) {
		timesCalled++;
		behaviors.add(b);
	}

}
