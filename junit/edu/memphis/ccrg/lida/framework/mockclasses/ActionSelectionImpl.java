/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.ExpectationListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class ActionSelectionImpl implements ActionSelection {

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		

	}

	@Override
	public void selectAction() {
		System.out.println("action selected");

	}

	@Override
	public void setTaskSpawner(TaskSpawner taskSpawner) {
		

	}

	@Override
	public void triggerActionSelection() {
		
		selectAction();
	}

	@Override
	public Object getState() {
		
		return null;
	}

	@Override
	public boolean setState(Object content) {
		
		return false;
	}

	@Override
	public void setExpectationListener(ExpectationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPreafferenceListener(PreafferenceListener listener) {
		// TODO Auto-generated method stub
		
	}

}
