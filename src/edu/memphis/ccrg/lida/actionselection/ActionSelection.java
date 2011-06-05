/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

/**
 * Interface for the action selection module of LIDA.
 * 
 * @author Ryan J McCall
 * 
 */
public interface ActionSelection extends FrameworkModule, BroadcastListener{

	/**
	 * Those classes that should be receiving selected actions from Action
	 * Selection
	 * 
	 * @param listener
	 *            listener of this action selection
	 */
	public void addActionSelectionListener(ActionSelectionListener listener);

	/**
	 * 
	 * @param listener
	 *            preafference listeners of this action selection
	 */
	public void addPreafferenceListener(PreafferenceListener listener);

	/**
	 * Choose a single action among those activated and whose context are
	 * satisfied to execute
	 * @return The selected {@link AgentAction} or null if no action was selected.
	 */
	public AgentAction selectAction();

}
