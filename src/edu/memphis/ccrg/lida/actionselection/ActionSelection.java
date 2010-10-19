/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener;
import edu.memphis.ccrg.lida.actionselection.triggers.TriggerListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Interface for the action selection module
 * @author Ryan J McCall
 *
 */
public interface ActionSelection extends LidaModule, TriggerListener, Saveable {
	
	/**
	 * Those classes that should be receiving selected actions from Action Selection
	 * @param listener listener of this action selection
	 */
	public abstract void addActionSelectionListener(ActionSelectionListener listener);
	
	/**
	 * 
	 * @param listener preafference listeners of this action selection
	 */
	public abstract void addPreafferenceListener(PreafferenceListener listener);
	
	/**
	 * Choose a single action among those activated and whose context are satisfied to execute
	 */
	public abstract void selectAction();

	/**
	 * TODO maybe all lidamodules should have setTaskSpawner
	 * @param taskSpawner the actionselectiondriver
	 */
	public abstract void setTaskSpawner(TaskSpawner taskSpawner);

}
