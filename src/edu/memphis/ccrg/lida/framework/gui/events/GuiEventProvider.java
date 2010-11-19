/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.events;

/**
 * A GuiContentProvider is a class that provides content
 * to a GUI.  The GUIs listen to providers, following Observer pattern.
 * Example content providers are PAMImpl.java and PerceptualBufferImpl.java
 *
 * @author Ryan J McCall
 *
 */
public interface GuiEventProvider {
	
	/**
	 * Must be able to register FrameworkGuiEvent listeners
	 * 
	 * @param listener receiver of GuiEvents, typically a LidaPanel
	 */
	public abstract void addFrameworkGuiEventListener(FrameworkGuiEventListener listener);

	/**
	 * A GuiEventProvider may want to send different kinds of events at different
	 * times so it should implement and use this method to do so.
	 * 
	 * @param evt GuiEvent
	 */
	public abstract void sendEventToGui(FrameworkGuiEvent evt);
}
