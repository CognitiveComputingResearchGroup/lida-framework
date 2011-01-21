/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.events;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;


/**
 * A GuiContentProvider is a class that provides content
 * for FrameworkGui's.   GUIs listen to providers using Observer pattern.
 * Examples include PAMImpl.java and PerceptualBufferImpl.java
 *
 * @author Ryan J. McCall
 *
 */
public interface GuiContentProvider {

	public abstract void sendEvent();
	public abstract void addFrameworkGuiEventListener(FrameworkGuiEventListener listener);
}
