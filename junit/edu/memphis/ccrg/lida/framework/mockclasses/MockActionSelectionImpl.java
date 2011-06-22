/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;

public class MockActionSelectionImpl extends FrameworkModuleImpl implements ActionSelection {
	
	private static final Logger logger = Logger.getLogger(MockActionSelectionImpl.class.getCanonicalName());

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		

	}

	@Override
	public Action selectAction() {
		System.out.println("action selected at tick: " + TaskManager.getCurrentTick());
		logger.log(Level.FINE,"Action Selection Performed at tick: {0}",TaskManager.getCurrentTick());
		return null;
	}

	@Override
	public void init() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					selectAction();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
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
	public void addPreafferenceListener(PreafferenceListener listener) {
		
		
	}

	@Override
	public void decayModule(long ticks) {
		// not implemented
		
	}

	@Override
	public void learn(BroadcastContent content) {
		// not implemented
		
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		// not implemented
		
	}

	@Override
	public Object getModuleContent(Object... params) {
		
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		
		
	}

}
