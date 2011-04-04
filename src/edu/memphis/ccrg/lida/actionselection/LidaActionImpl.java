/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * Abstract implmementation of {@link LidaAction}
 * 
 * @author Javier Snaider
 * 
 */
public abstract class LidaActionImpl implements LidaAction {

	private long id;
	private Object content;
	protected LidaModule module;
	private String label;
	private List<LidaAction> subActions;
	private Topology topology = Topology.BASIC;

	/**
	 * 
	 */
	public LidaActionImpl() {
	}

	/**
	 * 
	 * @param label label
	 */
	public LidaActionImpl(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.actionselection.LidaAction#setContent(java.lang
	 * .Object)
	 */
	@Override
	public void setContent(Object content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#getContent()
	 */
	@Override
	public Object getContent() {
		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.actionselection.LidaAction#setLabel(java.lang.String
	 * )
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.actionselection.LidaAction#setAssociatedModule(
	 * edu.memphis.ccrg.lida.framework.LidaModule)
	 */
	@Override
	public void setAssociatedModule(LidaModule module) {
		this.module = module;
	}

	/**
	 * @return the associated module
	 */
	@Override
	public LidaModule getAssociatedModule() {
		return module;
	}

	/**
	 * @return the subActions
	 */
	@Override
	public List<LidaAction> getSubActions() {
		return Collections.unmodifiableList(subActions);
	}

	/**
	 * @return the topology of the subActions.
	 */
	@Override
	public Topology getTopology() {
		return topology;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void addSubAction(LidaAction action, Topology topology) {
		switch (topology) {
		case BASIC:
			// error TODO: throw an exception
			break;
		case PARALLEL:
		case SEQUENCIAL:
			if (this.topology == Topology.BASIC || topology == this.topology) {
				if(subActions==null){
					subActions=new ArrayList<LidaAction>();
				}
				subActions.add(action);
				this.topology=topology;
			}
		}
	}
}
