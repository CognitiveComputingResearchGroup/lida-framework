/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.io.Serializable;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * Encapsulation of an action to be executed.
 * 
 * @author Ryan McCall, Javier Snaider
 * 
 */
public interface LidaAction extends Serializable {

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
	 * @return the subActions
	 */
	public List<LidaAction> getSubActions();

	/**
	 * @return the topology of the subActions.
	 */
	public Topology getTopology();

	public void addSubAction(LidaAction action, Topology topology);

	/**
	 * Sets an associated LidaModule.
	 * @param module the module to be associated.
	 */
	public void setAssociatedModule(LidaModule module);

	/**
	 * @return the module
	 */
	public LidaModule getModule();

	/**
	 * @return the LidaAction id
	 */
	public long getId();

	/**
	 * @param id the LidaAction id to set. it should be unique
	 */
	public void setId(long id);
}
