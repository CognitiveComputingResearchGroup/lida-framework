/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.Map;

/**
 * Default implementation of {@link Action}
 * 
 * @author Ryan McCall
 * @author Javier Snaider
 * 
 */
public class ActionImpl implements Action {

	private static int idGenerator = 0;
	private int id;
	private String label;
	private Map<String, ?> parameters;

	/**
	 * Default constructor
	 */
	public ActionImpl() {
		id = idGenerator++;
	}

	/**
	 * Convenience constructor that set the Action's label 
	 * @param label the label to set
	 */
	public ActionImpl(String label) {
		this();
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String name) {
		this.label = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = parameters.get(name);
		if(value == null){
			return defaultValue;
		}
		return value;
	}

	@Override
	public void init(Map<String, ?> params) {
		parameters = params;		
	}

	@Override
	public void init() {		
	}
	
}
