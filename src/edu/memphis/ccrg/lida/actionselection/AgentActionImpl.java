/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.framework.FrameworkModule;

/**
 * Abstract implmementation of {@link AgentAction}
 * 
 * @author Javier Snaider
 * 
 */
public abstract class AgentActionImpl implements AgentAction {


	private static long idGenerator = 0;
	private long id;
	private Object content;
	protected FrameworkModule module;
	private String label;

	public AgentActionImpl() {
		id = idGenerator++;
	}

	/**
	 * 
	 * @param label label
	 */
	public AgentActionImpl(String label) {
		this();
		this.label = label;
	}

	@Override
	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public Object getContent() {
		return content;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void setAssociatedModule(FrameworkModule module) {
		this.module = module;
	}

	/**
	 * @return the associated module
	 */
	@Override
	public FrameworkModule getAssociatedModule() {
		return module;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getId() {
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
}
