/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.Map;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

public interface SensoryMotorMemory extends LidaModule, SensoryMemoryListener, ActionSelectionListener{

	public abstract void addSensoryMotorListener(SensoryMotorListener l);
	
	public abstract void executeAction(LidaAction a);

	public abstract void setActionMap(Map<Long, LidaAction> actionMap);
	
	public abstract void addAction(long actionId, LidaAction action);
}
