/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.gui.events;

import edu.memphis.ccrg.lida.framework.ModuleName;

/**
 * Used by global workspace
 * @author Javier Snaider
 *
 */
public class TaskCountEvent extends FrameworkGuiEvent{

	public TaskCountEvent(ModuleName m, String count) {
		super(m, count, null);
	}
	public TaskCountEvent(ModuleName m, int count) {
		super(m, count + "", null);
	}

}
