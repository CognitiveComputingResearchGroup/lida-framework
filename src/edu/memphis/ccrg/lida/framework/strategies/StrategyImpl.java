/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of {@link Strategy}
 * @author Javier Snaider
 *
 */
public abstract class StrategyImpl implements Strategy {

	private Map<String, ?> parameters = new HashMap<String, Object>();
	
	@Override
	public void init(Map<String, ?> params) {
		this.parameters = params;
		init();
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

}
