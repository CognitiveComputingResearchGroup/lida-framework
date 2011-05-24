/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.environment;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

/**
 * Abstract implementation of {@link Environment}
 * Environments should not be a listener of anything besides GUIs.  Rather, SensoryMemory and SensoryMotorMemory should
 * add environments as associated modules in the XML configuration file. 
 * @author Ryan J. McCall
 */
public abstract class EnvironmentImpl extends FrameworkModuleImpl implements Environment, GuiEventProvider{
	
	private List<FrameworkGuiEventListener> listeners = new ArrayList<FrameworkGuiEventListener>();

	public EnvironmentImpl(){
	}
	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener){
		listeners.add(listener);
	}
		
	/*
	 * A Gui Event provider may want to send different kinds of events at different
	 * times, so the event to be sent is passed as a parameter.
	 * @param evt {@link FrameworkGuiEvent}
	 */
	@Override
	public void sendEventToGui(FrameworkGuiEvent evt){
		for(FrameworkGuiEventListener l: listeners){
			l.receiveFrameworkGuiEvent(evt);
		}
	}
	
	@Override
	public void addListener(ModuleListener listener) {
		//Not applicable for environment
	}
	
	@Override
	public void decayModule(long ticks) {
		//Decay module appropriately
	}
	
}