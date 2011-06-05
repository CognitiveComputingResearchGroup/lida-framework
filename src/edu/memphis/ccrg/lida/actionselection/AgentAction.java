/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.io.Serializable;

import edu.memphis.ccrg.lida.framework.FrameworkModule;

/**
 * Encapsulation of an action to be executed.
 * 
 * @author Ryan J. McCall
 * @author Javier Snaider
 * 
 */
public interface AgentAction extends Serializable {

	@SuppressWarnings(value = { "all" })
	public enum Topology {
		BASIC, PARALLEL, SEQUENCIAL
	}

	/**
	 * The actual action that should be performed.
	 * The action can interact directly with any module in LIDA, specially the
	 * SensoryMotorMemory.
	 */
	public void performAction();
	
	/**
	 * @return the action content.
	 */
	public Object getContent();
	
	/**
	 * @param content the content to set.
	 */
	public void setContent(Object content);
	
	/**
	 * @return the action label.
	 */
	public String getLabel();
	
	/**
	 * @param label the action label to set.
	 */
	public void setLabel(String label);

	/**
	 * Sets an associated FrameworkModule.
	 * @param module the module to be associated.
	 */
	public void setAssociatedModule(FrameworkModule module);

	/**
	 * @return the module
	 */
	public FrameworkModule getAssociatedModule();

	/**
	 * @return the {@link AgentAction} id
	 */
	public long getId();
	
	/**
	 * Useful to implement this for the display of tasks in the GUI
	 * @return {@link String}
	 */
	@Override
	public String toString();

}
